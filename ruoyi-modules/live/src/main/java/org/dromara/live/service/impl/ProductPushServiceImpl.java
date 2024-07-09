package org.dromara.live.service.impl;

import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.live.domain.bo.ProductActivityBo;
import org.dromara.live.domain.vo.ProductActivityVo;
import org.dromara.live.domain.vo.ProductLogVo;
import org.dromara.live.service.IProductActivityService;
import org.dromara.live.service.IProductLogService;
import org.dromara.live.service.IProductPushService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author 25487
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ProductPushServiceImpl implements IProductPushService {

    private final IProductActivityService productActivityService;
    private final IProductLogService productLogService;

    /**
     * 策略10000， 规则找出30天内涨停过的票，判断今天的价格是否在20日均线附近，且今天的20日均线大于涨停当天的20日均线
     *
     * @param infoDate 需要校验的日期
     */
    @Override
    public void push10000(String infoDate) {
        Date date = new Date();
        if (StringUtils.isNotBlank(infoDate)) {
            date = DateUtil.parse(infoDate);
        }
        List<ProductLogVo> productLogVos = productLogService.queryActivity10000(date);
        for (ProductLogVo productLogVo : productLogVos) {
            try {
                ProductLogVo logVo = productLogService.checkActivity10000(date, productLogVo.getProductCode(), productLogVo.getProductName());
                if (null != logVo) {
                    insertByProductLog(10000L, logVo);
                }
            } catch (Exception e) {
                log.error("{}，获取异常", productLogVo.getProductName(), e);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 新增推荐数据
     *
     * @param activityId   活动id
     * @param productLogVo 产品
     */
    private void insertByProductLog(long activityId, ProductLogVo productLogVo) {
        ProductActivityBo productActivityBo = new ProductActivityBo();
        productActivityBo.setProductCode(productLogVo.getProductCode());
        productActivityBo.setProductName(productLogVo.getProductName());
        productActivityBo.setActivityId(activityId);
        productActivityBo.setProductDate(productLogVo.getInfoDate());
        productActivityBo.setProductAmount(productLogVo.getF2());
        ProductActivityVo productActivityVo = productActivityService.queryByProductCodeAndActivityId(productActivityBo);
        if (null == productActivityVo) {
            productActivityService.insertByBo(productActivityBo);
        }
    }
}
