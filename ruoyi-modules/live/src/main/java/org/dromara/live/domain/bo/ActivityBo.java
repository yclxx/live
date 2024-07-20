package org.dromara.live.domain.bo;

import org.dromara.live.domain.Activity;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 活动管理业务对象 live_activity
 *
 * @author xx
 * @date 2024-07-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Activity.class, reverseConvertGenerate = false)
public class ActivityBo extends BaseEntity {

    /**
     * 活动编号
     */
    @NotNull(message = "活动编号不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long activityId;

    /**
     * 活动名称
     */
    @NotBlank(message = "活动名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String activityName;

    /**
     * 活动说明
     */
    @NotBlank(message = "活动说明不能为空", groups = {AddGroup.class, EditGroup.class})
    private String activityRemark;

    /**
     * 状态
     */
    @NotBlank(message = "状态不能为空", groups = {AddGroup.class, EditGroup.class})
    private String status;

    /**
     * 策略类
     */
    @NotBlank(message = "策略类不能为空", groups = {AddGroup.class, EditGroup.class})
    private String className;
}
