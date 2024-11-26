package org.dromara.mqtt.domain.bo;

import org.dromara.mqtt.domain.UserFriend;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 好友管理业务对象 mqtt_user_friend
 *
 * @author xx
 * @date 2024-11-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = UserFriend.class, reverseConvertGenerate = false)
public class UserFriendBo extends BaseEntity {

    /**
     * ID
     */
    @NotNull(message = "ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 好友用户ID
     */
    @NotNull(message = "好友用户ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long friendUserId;


}
