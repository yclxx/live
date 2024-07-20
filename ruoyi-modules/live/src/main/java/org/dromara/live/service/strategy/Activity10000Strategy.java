package org.dromara.live.service.strategy;

import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.live.domain.vo.ProductLogVo;
import org.dromara.live.service.IProductActivityService;
import org.dromara.live.service.IProductLogService;
import org.dromara.live.service.HandleStrategy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 策略10000， 规则找出30天内涨停过的票，判断今天的价格是否在20日均线附近(正负1.5%)，且今天的20日均线大于涨停当天的20日均线
 *
 * @author xiexi
 * @description
 * @date 2024/7/14 17:05
 */
@Slf4j
@Service("Activity10000Strategy")
@RequiredArgsConstructor
public class Activity10000Strategy implements HandleStrategy {

    private final IProductLogService productLogService;
    private final IProductActivityService productActivityService;

    @Async
    @Override
    public void handlePush(Long activityId, String infoDate) {
        Date date = new Date();
        if (StringUtils.isNotBlank(infoDate)) {
            date = DateUtil.parse(infoDate);
        }
        List<ProductLogVo> productLogVos = productLogService.queryActivity10000(date);
        for (ProductLogVo productLogVo : productLogVos) {
            try {
                ProductLogVo logVo = productLogService.checkActivity10000(date, productLogVo.getProductCode(), productLogVo.getProductName());
                if (null != logVo) {
                    productActivityService.insertByProductLog(activityId, logVo);
                }
            } catch (Exception e) {
                log.error("{}，获取异常", productLogVo.getProductName(), e);
                throw new RuntimeException(e);
            }
        }
        log.info("策略{}执行完成", activityId);
    }
}
