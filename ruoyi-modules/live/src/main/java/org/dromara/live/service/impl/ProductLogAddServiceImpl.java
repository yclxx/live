package org.dromara.live.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.live.domain.ProductLog;
import org.dromara.live.domain.ProductMoneyLog;
import org.dromara.live.domain.bo.ProductBo;
import org.dromara.live.domain.bo.ProductLogBo;
import org.dromara.live.domain.bo.ProductMoneyLogBo;
import org.dromara.live.domain.vo.*;
import org.dromara.live.service.IProductLogAddService;
import org.dromara.live.service.IProductLogService;
import org.dromara.live.service.IProductMoneyLogService;
import org.dromara.live.service.IProductService;
import org.dromara.live.utils.LiveUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xiexi
 * @description
 * @date 2024/7/13 10:23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ProductLogAddServiceImpl implements IProductLogAddService {

    private final IProductMoneyLogService productMoneyLogService;
    private final IProductService productService;
    private final IProductLogService productLogService;

    @Async
    @Override
    public void add() {
        String cacheKey = "productLog:add";
        String cacheObject = RedisUtils.getCacheObject(cacheKey);
        if (StringUtils.isNotBlank(cacheObject)) {
            return;
        }
        RedisUtils.setCacheObject(cacheKey, DateUtil.now(), Duration.ofMinutes(3));
        // 查询需要执行的产品
        ProductBo productBo = new ProductBo();
        productBo.setProductType("0,1");
        List<ProductVo> productVos = productService.queryList(productBo);
        try (ExecutorService es = Executors.newVirtualThreadPerTaskExecutor()) {
            for (ProductVo productVo : productVos) {
                if (productVo.getProductName().contains("ST") || productVo.getProductName().contains("PT") || productVo.getProductName().contains("退")) {
                    continue;
                }
                es.submit(() -> {
                    // 获取产品记录
                    saveProductLog(productVo);
                });
                es.submit(() -> {
                    // 获取产品资金记录
                    saveProductMoneyLog(productVo);
                });
                es.submit(() -> {
                    // 更新产品市值
                    updateProductMarketValue(productVo);
                });
            }
        }
    }

    /**
     * 补充遗漏的产品记录
     */
    @Async
    @Override
    public void productLogSupplement() {
        // 补充产品记录遗漏的
        String infoDate = productLogService.queryLastInfoDate();
        // info_date = '" + infoDate + "'"  需要增加'' 否则会存在执行语句变成info_date = 2024 - 07 - 17
        List<ProductVo> productVos = productService.querySupplementList("select product_code from live_product_log where info_date = '" + infoDate + "'");
        for (ProductVo productVo : productVos) {
            // 获取产品记录
            saveProductLog(productVo);
        }
        productVos = productService.querySupplementList("select product_code from live_product_money_log where info_date = '" + infoDate + "'");
        for (ProductVo productVo : productVos) {
            // 获取产品资金记录
            saveProductMoneyLog(productVo);
        }
    }

    /**
     * 更新产品市值
     *
     * @param productVo 产品信息
     */
    private void updateProductMarketValue(final ProductVo productVo) {
        // 异步执行
        GpMarketValue gpMarketValue = LiveUtils.getGpMarketValue(productVo.getProductCode());
        if (null == gpMarketValue) {
            return;
        }
        // 修改产品市值
        ProductBo pb = new ProductBo();
        pb.setProductCode(productVo.getProductCode());
        pb.setF116(gpMarketValue.getF116());
        pb.setF117(gpMarketValue.getF117());
        productService.updateByBo(pb);
    }

    /**
     * 获取产品记录
     *
     * @param productVo 产品信息
     */
    private void saveProductLog(final ProductVo productVo) {
        // 异步执行
        List<GpInfoVo> gpInfoVoList = LiveUtils.getGpInfoVoList(productVo.getProductCode(), productVo.getProductName());
        if (null == gpInfoVoList || gpInfoVoList.isEmpty()) {
            // 休眠1分钟，再次获取
            ThreadUtil.sleep(1000 * 60);
            gpInfoVoList = LiveUtils.getGpInfoVoList(productVo.getProductCode(), productVo.getProductName());
            if (null == gpInfoVoList || gpInfoVoList.isEmpty()) {
                return;
            }
        }
        GpInfoVo last = gpInfoVoList.getLast();
        if (LiveUtils.checkDate(last, 15)) {
            if ("0".equals(productVo.getStatus())) {
                // 并设置成停用状态
                ProductBo pb = new ProductBo();
                pb.setProductCode(productVo.getProductCode());
                pb.setStatus("1");
                productService.updateByBo(pb);
                // 删除存储的数据
                productLogService.deleteByProductCode(productVo.getProductCode());
            }
            return;
        } else {
            // 查询是否停用，如果停用了，启用
            if ("1".equals(productVo.getStatus())) {
                if (!productVo.getProductName().contains("ST") && !productVo.getProductName().contains("PT") && !productVo.getProductName().contains("退")) {
                    // 并设置成正常状态
                    ProductBo pb = new ProductBo();
                    pb.setProductCode(productVo.getProductCode());
                    pb.setStatus("0");
                    productService.updateByBo(pb);
                }
            }
        }
        GpInfoVo first = gpInfoVoList.getFirst();
        // 删除日期小于first的数据
        productLogService.deleteByProductCodeAndInfoDateLessThan(productVo.getProductCode(), first.getInfoDate());
        // 查询最早的一天数据
        ProductLogVo firstLogVo = productLogService.queryByProductCodeAndInfoDate(first.getProductCode(), first.getInfoDate());
        if (null != firstLogVo && first.getF2().compareTo(firstLogVo.getF2()) != 0) {
            // 删除因为分红等原因，价格变动问题
            productLogService.deleteByProductCode(first.getProductCode());
        }
        ProductLogVo productLogVo = productLogService.queryLastByProductCode(productVo.getProductCode());
        if (null == productLogVo) {
            List<ProductLog> productLogs = BeanUtil.copyToList(gpInfoVoList, ProductLog.class);
            productLogService.insertBatch(productLogs);
        } else {
            for (int i = 1; i <= gpInfoVoList.size(); i++) {
                // 倒着取
                GpInfoVo gpInfoVo = gpInfoVoList.get(gpInfoVoList.size() - i);
                ProductLogBo bean = BeanUtil.toBean(gpInfoVo, ProductLogBo.class);
                if (productLogVo.getInfoDate().equals(gpInfoVo.getInfoDate())) {
                    productLogService.updateByBo(bean);
                    // 结束本层循环
                    break;
                } else {
                    productLogService.insertByBo(bean);
                }
            }
        }
    }

    /**
     * 获取产品资金流向记录
     *
     * @param productVo 产品信息
     */
    private void saveProductMoneyLog(final ProductVo productVo) {
        List<GpMoneyVo> gpInfoMoneyList = LiveUtils.getGpInfoMoneyList(productVo.getProductCode(), productVo.getProductName());
        if (null == gpInfoMoneyList || gpInfoMoneyList.isEmpty()) {
            return;
        }
        GpMoneyVo first = gpInfoMoneyList.getFirst();
        // 删除日期小于first的数据
        productMoneyLogService.deleteByProductCodeAndInfoDateLessThan(productVo.getProductCode(), first.getInfoDate());
        ProductMoneyLogVo productMoneyLogVo = productMoneyLogService.queryLastByProductCode(productVo.getProductCode());
        if (null == productMoneyLogVo) {
            List<ProductMoneyLog> productMoneyLogs = BeanUtil.copyToList(gpInfoMoneyList, ProductMoneyLog.class);
            productMoneyLogService.insertBatch(productMoneyLogs);
        } else {
            for (int i = 1; i <= gpInfoMoneyList.size(); i++) {
                // 倒着取
                GpMoneyVo gpMoneyVo = gpInfoMoneyList.get(gpInfoMoneyList.size() - i);
                if (productMoneyLogVo.getInfoDate().equals(gpMoneyVo.getInfoDate())) {
                    ProductMoneyLogBo bean = BeanUtil.toBean(gpMoneyVo, ProductMoneyLogBo.class);
                    productMoneyLogService.updateByBo(bean);
                    // 结束本层循环
                    break;
                } else {
                    ProductMoneyLogBo bean = BeanUtil.toBean(gpMoneyVo, ProductMoneyLogBo.class);
                    productMoneyLogService.insertByBo(bean);
                }
            }
        }
    }
}
