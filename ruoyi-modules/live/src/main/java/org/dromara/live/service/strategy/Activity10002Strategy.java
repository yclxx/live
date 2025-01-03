package org.dromara.live.service.strategy;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.tenant.helper.TenantHelper;
import org.dromara.live.domain.vo.ProductLogVo;
import org.dromara.live.service.HandleStrategy;
import org.dromara.live.service.IProductActivityService;
import org.dromara.live.service.IProductLogService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * 策略10002， 持续上涨，并且每天最低点在5日均线附近（正负1%）
 *
 * @author xiexi
 * @description
 * @date 2024/7/14 17:05
 */
@Slf4j
@Service("Activity10002Strategy")
@RequiredArgsConstructor
public class Activity10002Strategy implements HandleStrategy {

    private final IProductLogService productLogService;
    private final IProductActivityService productActivityService;

    @Async
    @Override
    public Future<String> handlePush(String tenantId, Long activityId, String pushInfoDate) {
        TenantHelper.dynamic(tenantId, () -> {
            this.handle(activityId, pushInfoDate);
        });
        log.info("策略{}执行完成", activityId);
        return CompletableFuture.completedFuture("执行完成");
    }

    private void handle(Long activityId, String queryDate) {
        if (StringUtils.isBlank(queryDate)) {
            queryDate = DateUtil.today();
        }
        String infoDate = queryDate;
        List<String> productCodeList = productLogService.queryProductCodeByInfoDateAndF16(infoDate, null);
        if (ObjectUtil.isEmpty(productCodeList)) {
            return;
        }
        for (int i = 0; i < 4; i++) {
            // 查询前一天
            infoDate = productLogService.queryFirstInfoDate(infoDate);
            if (null == infoDate) {
                return;
            }
            // 查询前一天数据
            productCodeList = productLogService.queryProductCodeByInfoDateAndF16(infoDate, productCodeList);
            if (ObjectUtil.isEmpty(productCodeList)) {
                return;
            }
        }
        for (String productCode : productCodeList) {
            ProductLogVo productLogVo = productLogService.queryByProductCodeAndInfoDate(productCode, queryDate);
            if (null == productLogVo) {
                continue;
            }
            productActivityService.insertByProductLog(activityId, productLogVo);
        }
    }
}
