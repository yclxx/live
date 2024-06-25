package org.dromara.live.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.web.core.BaseController;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.live.domain.vo.ProductMoneyLogVo;
import org.dromara.live.domain.bo.ProductMoneyLogBo;
import org.dromara.live.service.IProductMoneyLogService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 资金流向
 *
 * @author xx
 * @date 2024-06-11
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/live/productMoneyLog")
public class ProductMoneyLogController extends BaseController {

    private final IProductMoneyLogService productMoneyLogService;

    /**
     * 查询资金流向列表
     */
    @SaCheckPermission("live:productMoneyLog:list")
    @GetMapping("/list")
    public TableDataInfo<ProductMoneyLogVo> list(ProductMoneyLogBo bo, PageQuery pageQuery) {
        return productMoneyLogService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出资金流向列表
     */
    @SaCheckPermission("live:productMoneyLog:export")
    @Log(title = "资金流向", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ProductMoneyLogBo bo, HttpServletResponse response) {
        List<ProductMoneyLogVo> list = productMoneyLogService.queryList(bo);
        ExcelUtil.exportExcel(list, "资金流向", ProductMoneyLogVo.class, response);
    }

    /**
     * 获取资金流向详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("live:productMoneyLog:query")
    @GetMapping("/{id}")
    public R<ProductMoneyLogVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(productMoneyLogService.queryById(id));
    }

    /**
     * 新增资金流向
     */
    @SaCheckPermission("live:productMoneyLog:add")
    @Log(title = "资金流向", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ProductMoneyLogBo bo) {
        return toAjax(productMoneyLogService.insertByBo(bo));
    }

    /**
     * 修改资金流向
     */
    @SaCheckPermission("live:productMoneyLog:edit")
    @Log(title = "资金流向", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ProductMoneyLogBo bo) {
        return toAjax(productMoneyLogService.updateByBo(bo));
    }

    /**
     * 删除资金流向
     *
     * @param ids 主键串
     */
    @SaCheckPermission("live:productMoneyLog:remove")
    @Log(title = "资金流向", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(productMoneyLogService.deleteWithValidByIds(List.of(ids), true));
    }
}
