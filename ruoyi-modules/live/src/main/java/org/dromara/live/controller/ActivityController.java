package org.dromara.live.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.live.domain.bo.ActivityBo;
import org.dromara.live.domain.vo.ActivityVo;
import org.dromara.live.service.IActivityService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 活动管理
 *
 * @author xx
 * @date 2024-07-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/live/activity")
public class ActivityController extends BaseController {

    private final IActivityService activityService;

    /**
     * 查询活动管理列表
     */
    @SaCheckPermission("live:activity:list")
    @GetMapping("/list")
    public TableDataInfo<ActivityVo> list(ActivityBo bo, PageQuery pageQuery) {
        return activityService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出活动管理列表
     */
    @SaCheckPermission("live:activity:export")
    @Log(title = "活动管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ActivityBo bo, HttpServletResponse response) {
        List<ActivityVo> list = activityService.queryList(bo);
        ExcelUtil.exportExcel(list, "活动管理", ActivityVo.class, response);
    }

    /**
     * 获取活动管理详细信息
     *
     * @param activityId 主键
     */
    @SaCheckPermission("live:activity:query")
    @GetMapping("/{activityId}")
    public R<ActivityVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long activityId) {
        return R.ok(activityService.queryById(activityId));
    }

    /**
     * 新增活动管理
     */
    @SaCheckPermission("live:activity:add")
    @Log(title = "活动管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ActivityBo bo) {
        return toAjax(activityService.insertByBo(bo));
    }

    /**
     * 修改活动管理
     */
    @SaCheckPermission("live:activity:edit")
    @Log(title = "活动管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ActivityBo bo) {
        return toAjax(activityService.updateByBo(bo));
    }

    /**
     * 删除活动管理
     *
     * @param activityIds 主键串
     */
    @SaCheckPermission("live:activity:remove")
    @Log(title = "活动管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{activityIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] activityIds) {
        return toAjax(activityService.deleteWithValidByIds(List.of(activityIds), true));
    }
}
