package org.dromara.live.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import org.dromara.common.tenant.core.TenantEntity;
import org.dromara.live.domain.Activity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 活动管理视图对象 live_activity
 *
 * @author xx
 * @date 2024-07-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Activity.class)
public class ActivityVo extends TenantEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 活动编号
     */
    @ExcelProperty(value = "活动编号")
    private Long activityId;

    /**
     * 活动名称
     */
    @ExcelProperty(value = "活动名称")
    private String activityName;

    /**
     * 活动说明
     */
    @ExcelProperty(value = "活动说明")
    private String activityRemark;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_normal_disable")
    private String status;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 策略类
     */
    private String className;
}
