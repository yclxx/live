package org.dromara.live.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.live.domain.bo.ActivityBo;
import org.dromara.live.domain.bo.ProductActivityBo;
import org.dromara.live.domain.vo.ActivityVo;
import org.dromara.live.domain.vo.GpInfoVo;
import org.dromara.live.domain.vo.ProductActivityVo;
import org.dromara.live.factory.StrategyFactory;
import org.dromara.live.service.IActivityService;
import org.dromara.live.service.IProductActivityService;
import org.dromara.live.service.IProductPushService;
import org.dromara.live.service.HandleStrategy;
import org.dromara.live.utils.LiveUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 25487
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ProductPushServiceImpl implements IProductPushService {

    private final IProductActivityService productActivityService;
    private final IActivityService activityService;

    /**
     * 执行策略
     */
    @Override
    public void push() {
        ActivityBo queryBo = new ActivityBo();
        queryBo.setStatus("0");
        List<ActivityVo> activityVos = activityService.queryList(queryBo);
        for (ActivityVo activityVo : activityVos) {
            if (StringUtils.isBlank(activityVo.getClassName())) {
                continue;
            }
            // 异步执行
            try {
                // 获取策略类
                HandleStrategy instance = StrategyFactory.instance(activityVo.getClassName());
                // 执行策略
                instance.handlePush(activityVo.getTenantId(), activityVo.getActivityId(), null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 更新推荐产品价格
     */
    @Async
    @Override
    public void updateActivityAmount() {
        List<ProductActivityVo> productActivityVos = productActivityService.queryListByThreeDay();
        for (ProductActivityVo productActivityVo : productActivityVos) {
            List<String> gpInfo = LiveUtils.getGpInfoString(productActivityVo.getProductCode());
            if (null == gpInfo || gpInfo.isEmpty()) {
                continue;
            }
            int index = -1;
            for (int i = 0; i < gpInfo.size(); i++) {
                String s = gpInfo.get(i);
                if (s.contains(productActivityVo.getProductDate())) {
                    index = i;
                }
            }
            if (index == -1) {
                continue;
            }
            ProductActivityBo productActivityBo = getProductActivityBo(productActivityVo, gpInfo, index);
            productActivityService.updateByBo(productActivityBo);
        }
        log.info("产品价格更新完成");
    }

    private ProductActivityBo getProductActivityBo(ProductActivityVo productActivityVo, List<String> gpInfo, int index) {
        ProductActivityBo productActivityBo = new ProductActivityBo();
        productActivityBo.setId(productActivityVo.getId());

        String s = gpInfo.get(index);
        GpInfoVo gpInfoVo = new GpInfoVo(s, productActivityVo.getProductCode(), productActivityVo.getProductName());
        productActivityBo.setProductAmountNow(gpInfoVo.getF2());
        // 后一条数据
        if (index + 1 < gpInfo.size()) {
            String s1 = gpInfo.get(index + 1);
            GpInfoVo gpInfoVo1 = new GpInfoVo(s1, productActivityVo.getProductCode(), productActivityVo.getProductName());
            productActivityBo.setProductAmount1(gpInfoVo1.getF2());
        }
        // 后第二条数据
        if (index + 2 < gpInfo.size()) {
            String s2 = gpInfo.get(index + 2);
            GpInfoVo gpInfoVo2 = new GpInfoVo(s2, productActivityVo.getProductCode(), productActivityVo.getProductName());
            productActivityBo.setProductAmount2(gpInfoVo2.getF2());
        }
        // 后第三条数据
        if (index + 3 < gpInfo.size()) {
            String s3 = gpInfo.get(index + 3);
            GpInfoVo gpInfoVo3 = new GpInfoVo(s3, productActivityVo.getProductCode(), productActivityVo.getProductName());
            productActivityBo.setProductAmount3(gpInfoVo3.getF2());
        }
        return productActivityBo;
    }
}
