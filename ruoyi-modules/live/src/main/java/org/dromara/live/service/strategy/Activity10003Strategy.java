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

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * 策略10003， 持续放量上涨
 *
 * @author xiexi
 * @description
 * @date 2024/7/14 17:05
 */
@Slf4j
@Service("Activity10003Strategy")
@RequiredArgsConstructor
public class Activity10003Strategy implements HandleStrategy {

    private final IProductLogService productLogService;
    private final IProductActivityService productActivityService;

    @Async
    @Override
    public Future<String> handlePush(String tenantId, Long activityId, String pushInfoDate) {
        TenantHelper.dynamic(tenantId, () -> {
            String queryDate = pushInfoDate;
            if (StringUtils.isBlank(queryDate)) {
                queryDate = DateUtil.today();
            }
            String infoDate = queryDate;
            // 查询涨幅没超过5%的票
            List<ProductLogVo> productLogVos = productLogService.queryListByInfoDate(infoDate);
            for (ProductLogVo productLogVo : productLogVos) {
                // 校验并添加
                this.checkAndInsert(activityId, productLogVo);
            }
        });
        log.info("策略{}执行完成", activityId);
        return CompletableFuture.completedFuture("执行完成");
    }

    private void checkAndInsert(Long activityId, ProductLogVo productLogVo) {
        int days = 5;
        // 查询前5个交易日的日期
        List<ProductLogVo> logVos = productLogService.queryBeforeInfoDate(productLogVo.getProductCode(), productLogVo.getInfoDate(), days);
        if (ObjectUtil.isEmpty(logVos)) {
            return;
        }
        Long f5 = productLogVo.getF5();
        int count = 0;
        for (ProductLogVo logVo : logVos) {
            // 如果涨幅超过5%，则剔除
            if (logVo.getF3().compareTo(new BigDecimal("5")) > 0) {
                return;
            }
            // 如果成交量一天比一天放大则选中
            if (f5.compareTo(logVo.getF5()) < 0) {
                return;
            }
            f5 = logVo.getF5();
            count++;
        }
        if (count >= days) {
            productActivityService.insertByProductLog(activityId, productLogVo);
        }
    }
}
