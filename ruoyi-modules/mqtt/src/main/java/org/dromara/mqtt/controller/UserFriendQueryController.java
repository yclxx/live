package org.dromara.mqtt.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.domain.dto.UserFriendDTO;
import org.dromara.common.core.service.UserFriendQueryService;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.common.web.core.BaseController;
import org.dromara.mqtt.domain.bo.UserFriendBo;
import org.dromara.mqtt.domain.vo.UserFriendQueryVo;
import org.dromara.mqtt.domain.vo.UserFriendVo;
import org.dromara.mqtt.service.IUserFriendService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xiexi
 * @description
 * @date 2024/11/27 21:12
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mqtt/userFriend")
public class UserFriendQueryController extends BaseController {

    private final IUserFriendService userFriendService;
    private final UserFriendQueryService userFriendQueryService;

    @GetMapping("/queryUserFriendList")
    public R<List<UserFriendQueryVo>> list() {
        UserFriendBo bo = new UserFriendBo();
        bo.setUserId(LoginHelper.getUserId());
        List<UserFriendVo> userFriendVos = userFriendService.queryList(bo);
        // 用户好友ID
        List<Long> friendUserIds = userFriendVos.stream().map(UserFriendVo::getFriendUserId).toList();
        // 查询用户好友
        List<UserFriendDTO> userFriendDTOS = userFriendQueryService.queryListByUserIds(friendUserIds);
        // 转UserFriendQueryVo 头像翻译，脱敏等操作
        return R.ok(BeanUtil.copyToList(userFriendDTOS, UserFriendQueryVo.class));
    }

    @GetMapping("/queryUserFriendInfo/{friendUserId}")
    public R<UserFriendQueryVo> queryUserFriendInfo(@PathVariable Long friendUserId) {
        List<Long> friendUserIds = List.of(friendUserId);
        // 查询用户好友
        List<UserFriendDTO> userFriendDTOS = userFriendQueryService.queryListByUserIds(friendUserIds);
        if (ObjectUtil.isEmpty(userFriendDTOS)) {
            return R.ok();
        }
        // 转UserFriendQueryVo 头像翻译，脱敏等操作
        return R.ok(BeanUtil.toBean(userFriendDTOS.getFirst(), UserFriendQueryVo.class));
    }
}
