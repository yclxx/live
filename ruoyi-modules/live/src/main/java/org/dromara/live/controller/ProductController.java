package org.dromara.live.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.common.web.core.BaseController;
import org.dromara.live.domain.Product;
import org.dromara.live.domain.bo.ProductBo;
import org.dromara.live.domain.vo.GpDayAllVo;
import org.dromara.live.domain.vo.GpInfoVo;
import org.dromara.live.domain.vo.ProductVo;
import org.dromara.live.service.IProductService;
import org.dromara.live.utils.LiveUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 产品管理
 *
 * @author xx
 * @date 2024-05-28
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/live/product")
public class ProductController extends BaseController {

    private final IProductService productService;

    /**
     * 查询产品管理列表
     */
    @SaCheckPermission("live:product:list")
    @GetMapping("/list")
    public TableDataInfo<ProductVo> list(ProductBo bo, PageQuery pageQuery) {
        TableDataInfo<ProductVo> productVoTableDataInfo = productService.queryPageList(bo, pageQuery);
        CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(productVoTableDataInfo.getRows().size());
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            for (ProductVo productVo : productVoTableDataInfo.getRows()) {
                if (!"0".equals(productVo.getStatus())) {
                    countDownLatch.countDown();
                    continue;
                }
                if (!"0".equals(productVo.getProductType()) && !"1".equals(productVo.getProductType()) && !"2".equals(productVo.getProductType())) {
                    countDownLatch.countDown();
                    continue;
                }
                executorService.submit(() -> {
                    try {
                        List<GpInfoVo> gpInfoVoList = LiveUtils.getGpInfoVoList(productVo.getProductCode(), productVo.getProductName());
                        if (null != gpInfoVoList && !gpInfoVoList.isEmpty()) {
                            productVo.setGpInfoVo(gpInfoVoList.getLast());
                        }
                    } finally {
                        countDownLatch.countDown();
                    }
                });
            }
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return productVoTableDataInfo;
    }

    /**
     * 导出产品管理列表
     */
    @SaCheckPermission("live:product:export")
    @Log(title = "产品管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ProductBo bo, HttpServletResponse response) {
        List<ProductVo> list = productService.queryList(bo);
        ExcelUtil.exportExcel(list, "产品管理", ProductVo.class, response);
    }

    /**
     * 获取产品管理详细信息
     *
     * @param productCode 主键
     */
    @SaCheckPermission("live:product:query")
    @GetMapping("/{productCode}")
    public R<ProductVo> getInfo(@NotNull(message = "主键不能为空")
                                @PathVariable String productCode) {
        return R.ok(productService.queryById(productCode));
    }

    /**
     * 新增产品管理
     */
    @SaCheckPermission("live:product:add")
    @Log(title = "产品管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add() {
        String cmd = "f3&fs=m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048";
        String cacheKey = "getGpAll:" + cmd;
        String cacheObject = RedisUtils.getCacheObject(cacheKey);
        if (StringUtils.isNotBlank(cacheObject)) {
            return R.fail("已经在执行中，不可重复执行");
        }
        RedisUtils.setCacheObject(cacheKey, DateUtil.now(), Duration.ofMinutes(30));
        log.info("开始执行");
        // 异步执行
        ThreadUtil.execAsync(() -> {
            int pageNum = 1;
            while (true) {
                List<GpDayAllVo> gpDayAllVo = LiveUtils.getGpDayAllVo(cmd, pageNum);
                if (null == gpDayAllVo || gpDayAllVo.isEmpty()) {
                    RedisUtils.deleteObject(cacheKey);
                    log.info("执行结束");
                    break;
                }
                List<Product> productList = getProductList(gpDayAllVo);
                productService.insertByBo(productList);
                pageNum++;
            }
        });
        return R.ok("操作成功");
    }

    private static List<Product> getProductList(List<GpDayAllVo> gpDayAllVo) {
        List<Product> productList = new ArrayList<>(gpDayAllVo.size());
        for (GpDayAllVo dayAllVo : gpDayAllVo) {
            Product product = new Product();
            product.setProductCode(dayAllVo.getF12());
            product.setProductName(dayAllVo.getF14());
            if (product.getProductName().contains("ST") || product.getProductName().contains("PT") || product.getProductName().contains("退")) {
                product.setStatus("1");
            } else {
                product.setStatus("0");
            }
            if (product.getProductCode().startsWith("0")) {
                product.setProductType("0");
            } else if (product.getProductCode().startsWith("60")) {
                product.setProductType("1");
            } else if (product.getProductCode().startsWith("3")) {
                product.setProductType("2");
            } else if (product.getProductCode().startsWith("68")) {
                product.setProductType("3");
            } else {
                product.setProductType("4");
            }
            productList.add(product);
        }
        return productList;
    }

    /**
     * 修改产品管理
     */
    @SaCheckPermission("live:product:edit")
    @Log(title = "产品管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ProductBo bo) {
        return toAjax(productService.updateByBo(bo));
    }

    /**
     * 修改产品管理
     */
    @SaCheckPermission("live:product:edit")
    @Log(title = "产品管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/editTop/{productCode}")
    public R<Void> editTop(@NotNull(message = "主键不能为空")
                           @PathVariable String productCode) {
        // 查询产品最大排序值
        ProductBo bo = new ProductBo();
        bo.setProductCode(productCode);
        bo.setSort(productService.getMaxSort() + 1);
        return toAjax(productService.updateByBo(bo));
    }

    /**
     * 删除产品管理
     *
     * @param productCodes 主键串
     */
    @SaCheckPermission("live:product:remove")
    @Log(title = "产品管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{productCodes}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] productCodes) {
        return toAjax(productService.deleteWithValidByIds(List.of(productCodes), true));
    }
}
