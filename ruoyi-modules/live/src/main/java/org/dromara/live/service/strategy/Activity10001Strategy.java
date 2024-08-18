package org.dromara.live.service.strategy;

import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.tenant.helper.TenantHelper;
import org.dromara.live.domain.bo.ProductBo;
import org.dromara.live.domain.vo.ProductLogVo;
import org.dromara.live.domain.vo.ProductVo;
import org.dromara.live.service.HandleStrategy;
import org.dromara.live.service.IProductActivityService;
import org.dromara.live.service.IProductLogService;
import org.dromara.live.service.IProductService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * 策略10001， 规则找出市值超过300亿的票，判断今天的价格是否在20日均线附近(正负1%)
 *
 * @author xiexi
 * @description
 * @date 2024/7/14 17:05
 */
@Slf4j
@Service("Activity10001Strategy")
@RequiredArgsConstructor
public class Activity10001Strategy implements HandleStrategy {

    private final IProductLogService productLogService;
    private final IProductActivityService productActivityService;
    private final IProductService productService;

    @Async
    @Override
    public Future<String> handlePush(String tenantId, Long activityId, String pushInfoDate) {
        TenantHelper.dynamic(tenantId, () -> {
            String infoDate = pushInfoDate;
            Date date = new Date();
            if (StringUtils.isNotBlank(infoDate)) {
                date = DateUtil.parse(infoDate);
            } else {
                infoDate = DateUtil.format(date, "yyyy-MM-dd");
            }
            ProductBo queryBo = new ProductBo();
            queryBo.setStatus("0");
            queryBo.setProductType("0,1");
            // 市值大于300亿
            Map<String, Object> params = new HashMap<>();
            params.put("minF116", new BigDecimal("30000000000"));
            queryBo.setParams(params);
            List<ProductVo> productVos = productService.queryList(queryBo);
            for (ProductVo productVo : productVos) {
                try {
                    ProductLogVo logVo = productLogService.checkMa20(date, productVo.getProductCode(), productVo.getProductName());
                    if (null == logVo) {
                        continue;
                    }
                    // 横盘不要
                    BigDecimal f2Float = productLogService.queryFloatByDays(productVo.getProductCode(), infoDate, 20);
                    if (f2Float.compareTo(new BigDecimal("0.05")) < 0) {
                        continue;
                    }
                    // 下跌趋势不要
                    BigDecimal sumDaysF3 = productLogService.sumDaysF3(productVo.getProductCode(), infoDate, 20);
                    if (sumDaysF3.compareTo(new BigDecimal("0.01")) < 0) {
                        continue;
                    }
                    productActivityService.insertByProductLog(activityId, logVo);
                } catch (Exception e) {
                    log.error("{}，获取异常", productVo.getProductName(), e);
                    throw new RuntimeException(e);
                }
            }
        });
        log.info("策略{}执行完成", activityId);
        return CompletableFuture.completedFuture("执行完成");
    }
}
