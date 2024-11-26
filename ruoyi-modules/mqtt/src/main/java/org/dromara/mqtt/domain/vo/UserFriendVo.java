package org.dromara.mqtt.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;
import org.dromara.mqtt.domain.UserFriend;

import java.io.Serial;
import java.io.Serializable;

/**
 * 好友管理视图对象 mqtt_user_friend
 *
 * @author xx
 * @date 2024-11-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = UserFriend.class)
public class UserFriendVo extends TenantEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    private Long id;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    private Long userId;

    /**
     * 好友用户ID
     */
    @ExcelProperty(value = "好友用户ID")
    private Long friendUserId;

}
