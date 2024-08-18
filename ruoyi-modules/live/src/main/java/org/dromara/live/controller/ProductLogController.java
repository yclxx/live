package org.dromara.live.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.live.domain.bo.ProductLogBo;
import org.dromara.live.domain.vo.ProductLogVo;
import org.dromara.live.service.IProductLogAddService;
import org.dromara.live.service.IProductLogService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private final IProductLogAddService productLogAddService;

    /**
     * 查询产品记录列表
     */
    @SaCheckPermission("live:productLog:list")
    @GetMapping("/list")
    public TableDataInfo<ProductLogVo> list(ProductLogBo bo, PageQuery pageQuery) {
        return productLogService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询最新的交易日期
     */
    @SaCheckPermission("live:productLog:list")
    @GetMapping("/lastDate")
    public R<String> lastDate() {
        return R.ok("操作成功", productLogService.queryLastInfoDate());
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
        productLogAddService.add();
        return R.ok();
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
