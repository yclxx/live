package org.dromara.live.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
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
import org.dromara.live.domain.ProductLog;
import org.dromara.live.domain.ProductMoneyLog;
import org.dromara.live.domain.bo.ProductBo;
import org.dromara.live.domain.bo.ProductLogBo;
import org.dromara.live.domain.bo.ProductMoneyLogBo;
import org.dromara.live.domain.vo.*;
import org.dromara.live.service.IProductLogService;
import org.dromara.live.service.IProductMoneyLogService;
import org.dromara.live.service.IProductService;
import org.dromara.live.utils.LiveUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 产品记录
 *
 * @author xx
 * @date 2024-06-02
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/live/productLog")
public class ProductLogController extends BaseController {

    private final IProductLogService productLogService;
    private final IProductService productService;
    private final IProductMoneyLogService productMoneyLogService;
    ;

    /**
     * 查询产品记录列表
     */
    @SaCheckPermission("live:productLog:list")
    @GetMapping("/list")
    public TableDataInfo<ProductLogVo> list(ProductLogBo bo, PageQuery pageQuery) {
        return productLogService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出产品记录列表
     */
    @SaCheckPermission("live:productLog:export")
    @Log(title = "产品记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ProductLogBo bo, HttpServletResponse response) {
        List<ProductLogVo> list = productLogService.queryList(bo);
        ExcelUtil.exportExcel(list, "产品记录", ProductLogVo.class, response);
    }

    /**
     * 获取产品记录详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("live:productLog:query")
    @GetMapping("/{id}")
    public R<ProductLogVo> getInfo(@NotNull(message = "主键不能为空")
                                   @PathVariable String id) {
        return R.ok(productLogService.queryById(id));
    }

    /**
     * 新增产品记录
     */
    @SaCheckPermission("live:productLog:add")
    @Log(title = "产品记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add() {
        String cacheKey = "productLog:add";
        String cacheObject = RedisUtils.getCacheObject(cacheKey);
        if (StringUtils.isNotBlank(cacheObject)) {
            return R.fail("已经在执行中，不可重复执行");
        }
        RedisUtils.setCacheObject(cacheKey, DateUtil.now(), Duration.ofMinutes(3));
        log.info("开始执行");
        ProductBo productBo = new ProductBo();
        productBo.setProductType("0,1");
        productBo.setStatus("0");
        List<ProductVo> productVos = productService.queryList(productBo);
        // 获取产品记录
        saveProductLog(productVos);
        // 获取产品资金记录
        saveProductMoneyLog(productVos);
        return R.ok();
    }

    /**
     * 获取产品记录
     *
     * @param productVos 产品信息
     */
    private void saveProductLog(List<ProductVo> productVos) {
        // 异步执行
        try (ExecutorService es = Executors.newVirtualThreadPerTaskExecutor()) {
            for (ProductVo productVo : productVos) {
                es.submit(() -> {
                    List<GpInfoVo> gpInfoVoList = LiveUtils.getGpInfoVoList(productVo.getProductCode(), productVo.getProductName());
                    if (null == gpInfoVoList || gpInfoVoList.isEmpty()) {
                        return;
                    }
                    GpInfoVo last = gpInfoVoList.getLast();
                    if (LiveUtils.checkBase(last, 15)) {
                        // 并设置成停用状态
                        ProductBo pb = new ProductBo();
                        pb.setProductCode(productVo.getProductCode());
                        pb.setStatus("1");
                        productService.updateByBo(pb);
                        // 删除存储的数据
                        productLogService.deleteByProductCode(productVo.getProductCode());
                        return;
                    }
                    GpInfoVo first = gpInfoVoList.getFirst();
                    // 删除日期小于first的数据
                    productLogService.deleteByProductCodeAndInfoDateLessThan(productVo.getProductCode(), first.getInfoDate());
                    ProductLogVo productLogVo = productLogService.queryLastByProductCode(productVo.getProductCode());
                    if (null == productLogVo) {
                        List<ProductLog> productLogs = BeanUtil.copyToList(gpInfoVoList, ProductLog.class);
                        productLogService.insertBatch(productLogs);
                    } else {
                        for (int i = 1; i <= gpInfoVoList.size(); i++) {
                            // 倒着取
                            GpInfoVo gpInfoVo = gpInfoVoList.get(gpInfoVoList.size() - i);
                            if (productLogVo.getInfoDate().equals(gpInfoVo.getInfoDate())) {
                                ProductLogBo bean = BeanUtil.toBean(gpInfoVo, ProductLogBo.class);
                                productLogService.updateByBo(bean);
                                // 结束本层循环
                                break;
                            } else {
                                ProductLogBo bean = BeanUtil.toBean(gpInfoVo, ProductLogBo.class);
                                productLogService.insertByBo(bean);
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * 获取产品资金流向记录
     *
     * @param productVos 产品信息
     */
    private void saveProductMoneyLog(List<ProductVo> productVos) {
        try (ExecutorService es = Executors.newVirtualThreadPerTaskExecutor()) {
            // 异步执行
            for (ProductVo productVo : productVos) {
                es.submit(() -> {
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
                });
            }
        }
    }

    /**
     * 修改产品记录
     */
    @SaCheckPermission("live:productLog:edit")
    @Log(title = "产品记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ProductLogBo bo) {
        return toAjax(productLogService.updateByBo(bo));
    }

    /**
     * 删除产品记录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("live:productLog:remove")
    @Log(title = "产品记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(productLogService.deleteWithValidByIds(List.of(ids), true));
    }

    /**
     * 查询产品记录列表
     */
    @GetMapping("/ignore/listByD3")
    public R<Void> listByD3() {
        return R.ok();
    }
}
