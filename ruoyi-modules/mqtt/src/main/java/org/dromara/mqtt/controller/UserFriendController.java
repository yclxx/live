package org.dromara.mqtt.controller;

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
import org.dromara.mqtt.domain.bo.UserFriendBo;
import org.dromara.mqtt.domain.vo.UserFriendVo;
import org.dromara.mqtt.service.IUserFriendService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 好友管理
 *
 * @author xx
 * @date 2024-11-26
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mqtt/userFriend")
public class UserFriendController extends BaseController {

    private final IUserFriendService userFriendService;

    /**
     * 查询好友管理列表
     */
    @SaCheckPermission("mqtt:userFriend:list")
    @GetMapping("/list")
    public TableDataInfo<UserFriendVo> list(UserFriendBo bo, PageQuery pageQuery) {
        return userFriendService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出好友管理列表
     */
    @SaCheckPermission("mqtt:userFriend:export")
    @Log(title = "好友管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(UserFriendBo bo, HttpServletResponse response) {
        List<UserFriendVo> list = userFriendService.queryList(bo);
        ExcelUtil.exportExcel(list, "好友管理", UserFriendVo.class, response);
    }

    /**
     * 获取好友管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mqtt:userFriend:query")
    @GetMapping("/{id}")
    public R<UserFriendVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(userFriendService.queryById(id));
    }

    /**
     * 新增好友管理
     */
    @SaCheckPermission("mqtt:userFriend:add")
    @Log(title = "好友管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody UserFriendBo bo) {
        return toAjax(userFriendService.insertByBo(bo));
    }

    /**
     * 修改好友管理
     */
    @SaCheckPermission("mqtt:userFriend:edit")
    @Log(title = "好友管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody UserFriendBo bo) {
        return toAjax(userFriendService.updateByBo(bo));
    }

    /**
     * 删除好友管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mqtt:userFriend:remove")
    @Log(title = "好友管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(userFriendService.deleteWithValidByIds(List.of(ids), true));
    }
}
