package org.dromara.live.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 活动管理对象 live_activity
 *
 * @author xx
 * @date 2024-07-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("live_activity")
public class Activity extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 活动编号
     */
    @TableId(value = "activity_id", type = IdType.INPUT)
    private Long activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动说明
     */
    private String activityRemark;

    /**
     * 状态
     */
    private String status;

    /**
     * 策略类
     */
    private String className;

}
