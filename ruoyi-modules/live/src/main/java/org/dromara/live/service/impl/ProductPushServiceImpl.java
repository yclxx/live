package org.dromara.live.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.live.domain.bo.ProductActivityBo;
import org.dromara.live.domain.vo.ProductActivityVo;
import org.dromara.live.domain.vo.ProductLogVo;
import org.dromara.live.service.IProductActivityService;
import org.dromara.live.service.IProductLogService;
import org.dromara.live.service.IProductPushService;
import org.dromara.live.utils.LiveUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    @Override
    public void push20001(String infoDate) {
        List<ProductLogVo> productLogVos = productLogService.queryBy10000(infoDate);
        for (ProductLogVo productLogVo : productLogVos) {
            List<ProductLogVo> afterList = productLogService.queryBy10000AfterList(infoDate, productLogVo.getProductCode(), 20);
            if (afterList.size() < 20) {
                continue;
            }
            boolean tg = true;
            for (ProductLogVo logVo : afterList) {
                if (logVo.getF3().compareTo(new BigDecimal("9.5")) > 0) {
                    tg = false;
                    break;
                }
            }
            if (tg) {
                continue;
            }
            List<ProductLogVo> productLogVos1 = afterList.subList(0, 5);
            boolean f3tg = false;
            for (ProductLogVo logVo : productLogVos1) {
                if (logVo.getF3().compareTo(new BigDecimal("3")) > 0 || !LiveUtils.checkMa20F16(logVo, new BigDecimal("0.01"))) {
                    f3tg = true;
                    break;
                }
            }
            if (f3tg) {
                continue;
            }
            int ma20_10count = 0;
            int sz = 0;
            BigDecimal f3add = new BigDecimal("0");
            List<ProductLogVo> ma20_10 = afterList.subList(0, 10);
            for (int i = 0; i < ma20_10.size(); i++) {
                ProductLogVo logVo = ma20_10.get(i);
                if (logVo.getF16().compareTo(logVo.getMa20()) < 0) {
                    ma20_10count++;
                }
                if (i < 3 && logVo.getF3().compareTo(new BigDecimal("1")) > 0) {
                    sz++;
                }
                if (i < 4 && logVo.getF3().compareTo(new BigDecimal("0")) > 0) {
                    f3add = f3add.add(logVo.getF3());
                }
            }
            if (ma20_10count > 4 || sz > 1 || f3add.compareTo(new BigDecimal("4")) > 0) {
                continue;
            }
            // 新增
            insertByProductLog(20001, productLogVo);
            log.info("推荐任务2执行完成");
        }
    }

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
