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
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.common.web.core.BaseController;
import org.dromara.live.domain.bo.ProductActivityBo;
import org.dromara.live.domain.vo.GpInfoVo;
import org.dromara.live.domain.vo.ProductActivityVo;
import org.dromara.live.service.IProductActivityService;
import org.dromara.live.service.IProductPushService;
import org.dromara.live.utils.LiveUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 产品活动
 *
 * @author xx
 * @date 2024-05-28
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/live/productActivity")
public class ProductActivityController extends BaseController {

    private final IProductActivityService productActivityService;
    private final IProductPushService productPushService;

    /**
     * 查询产品活动列表
     */
    @SaCheckPermission("live:productActivity:list")
    @GetMapping("/list")
    public TableDataInfo<ProductActivityVo> list(ProductActivityBo bo, PageQuery pageQuery) {
        TableDataInfo<ProductActivityVo> productActivityVoTableDataInfo = productActivityService.queryPageList(bo, pageQuery);
        CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(productActivityVoTableDataInfo.getRows().size());
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            for (ProductActivityVo productActivityVo : productActivityVoTableDataInfo.getRows()) {
                executorService.submit(() -> {
                    try {
                        List<GpInfoVo> gpInfoVoList = LiveUtils.getGpInfoVoList(productActivityVo.getProductCode(), productActivityVo.getProductName());
                        if (null != gpInfoVoList && !gpInfoVoList.isEmpty()) {
                            productActivityVo.setGpInfoVo(gpInfoVoList.getLast());
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
        return productActivityVoTableDataInfo;
    }

    /**
     * 导出产品活动列表
     */
    @SaCheckPermission("live:productActivity:export")
    @Log(title = "产品活动", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ProductActivityBo bo, HttpServletResponse response) {
        List<ProductActivityVo> list = productActivityService.queryList(bo);
        ExcelUtil.exportExcel(list, "产品活动", ProductActivityVo.class, response);
    }

    /**
     * 获取产品活动详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("live:productActivity:query")
    @GetMapping("/{id}")
    public R<ProductActivityVo> getInfo(@NotNull(message = "主键不能为空")
                                        @PathVariable Long id) {
        return R.ok(productActivityService.queryById(id));
    }

    /**
     * 新增产品活动
     */
    @SaCheckPermission("live:productActivity:add")
    @Log(title = "产品活动", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add() {
        String cacheKey = "productActivity:add";
        String cacheObject = RedisUtils.getCacheObject(cacheKey);
        if (StringUtils.isNotBlank(cacheObject)) {
            return R.fail("已经在执行中，不可重复执行");
        }
        RedisUtils.setCacheObject(cacheKey, DateUtil.now(), Duration.ofMinutes(3));
        log.info("开始执行");
        // 异步执行
        ThreadUtil.execAsync(() -> {
            productPushService.push10000(null);
            log.info("执行完成");
        });
        return R.ok();
    }

    /**
     * 修改产品活动
     */
    @SaCheckPermission("live:productActivity:edit")
    @Log(title = "产品活动", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit() {
        String cacheKey = "productActivity:edit";
        String cacheObject = RedisUtils.getCacheObject(cacheKey);
        if (StringUtils.isNotBlank(cacheObject)) {
            return R.fail("已经在执行中，不可重复执行");
        }
        RedisUtils.setCacheObject(cacheKey, DateUtil.now(), Duration.ofMinutes(1));
        log.info("开始执行");
        // 异步执行
        ThreadUtil.execAsync(() -> {
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
        });
        return R.ok();
    }

    private static ProductActivityBo getProductActivityBo(ProductActivityVo productActivityVo, List<String> gpInfo, int index) {
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

    /**
     * 删除产品活动
     *
     * @param ids 主键串
     */
    @SaCheckPermission("live:productActivity:remove")
    @Log(title = "产品活动", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(productActivityService.deleteWithValidByIds(List.of(ids), true));
    }
}
