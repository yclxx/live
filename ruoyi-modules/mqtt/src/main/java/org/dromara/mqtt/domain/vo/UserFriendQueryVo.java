package org.dromara.mqtt.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.domain.dto.UserFriendDTO;
import org.dromara.common.sensitive.annotation.Sensitive;
import org.dromara.common.sensitive.core.SensitiveStrategy;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;

/**
 * @author xiexi
 * @description
 * @date 2024/11/27 22:23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserFriendQueryVo extends UserFriendDTO {

    @Translation(type = TransConstant.OSS_ID_TO_URL)
    private Long avatar;

    /**
     * 用户邮箱
     */
    @Sensitive(strategy = SensitiveStrategy.EMAIL)
    private String email;

    /**
     * 手机号码
     */
    @Sensitive(strategy = SensitiveStrategy.PHONE)
    private String phonenumber;
}
