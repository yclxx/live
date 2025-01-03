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
import org.dromara.common.tenant.helper.TenantHelper;
import org.dromara.common.web.core.BaseController;
import org.dromara.live.domain.bo.ProductActivityBo;
import org.dromara.live.domain.vo.ActivityVo;
import org.dromara.live.domain.vo.GpInfoVo;
import org.dromara.live.domain.vo.ProductActivityVo;
import org.dromara.live.factory.StrategyFactory;
import org.dromara.live.service.HandleStrategy;
import org.dromara.live.service.IActivityService;
import org.dromara.live.service.IProductActivityService;
import org.dromara.live.service.IProductPushService;
import org.dromara.live.service.strategy.Activity99999Strategy;
import org.dromara.live.utils.LiveUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    private final IActivityService activityService;

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
                        this.setGpInfoVo(productActivityVo);
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
     * 获取今日幸运推荐
     */
    @SaCheckPermission("live:productActivity:lucky")
    @GetMapping("/lucky")
    public R<ProductActivityVo> lucky() {
        long activityId = 99999;
        ActivityVo activityVo = activityService.queryById(activityId);
        try {
            // 获取策略类
            HandleStrategy instance = StrategyFactory.instance(activityVo.getClassName());
            if (instance instanceof Activity99999Strategy strategy) {
                String luckyKey = strategy.getLuckyKey();
                Long id = RedisUtils.getCacheObject(luckyKey);
                if (null == id) {
                    Future<String> stringFuture = strategy.handlePush(TenantHelper.getTenantId(), activityId, null);
                    String s = stringFuture.get();
                    if (StringUtils.isNotBlank(s)) {
                        id = RedisUtils.getCacheObject(luckyKey);
                        if (null != id && id > 0) {
                            ProductActivityVo productActivityVo = productActivityService.queryById(id);
                            // 获取最新价
                            this.setGpInfoVo(productActivityVo);
                            return R.ok(productActivityVo);
                        }
                    }
                } else if (id == -1) {
                    return R.ok("操作成功", null);
                } else {
                    ProductActivityVo productActivityVo = productActivityService.queryById(id);
                    // 获取最新价
                    this.setGpInfoVo(productActivityVo);
                    return R.ok(productActivityVo);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return R.ok("操作成功", null);
    }

    private void setGpInfoVo(ProductActivityVo productActivityVo) {
        List<GpInfoVo> gpInfoVoList = LiveUtils.getGpInfoVoList(productActivityVo.getProductCode(), productActivityVo.getProductName());
        if (null != gpInfoVoList && !gpInfoVoList.isEmpty()) {
            productActivityVo.setGpInfoVo(gpInfoVoList.getLast());
        }
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
        productPushService.push();
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
        // 异步执行
        productPushService.updateActivityAmount();
        return R.ok();
    }

    /**
     * 修改产品活动
     */
    @SaCheckPermission("live:productActivity:edit")
    @Log(title = "产品活动", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/edit")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ProductActivityBo bo) {
        return toAjax(productActivityService.updateByBo(bo));
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
