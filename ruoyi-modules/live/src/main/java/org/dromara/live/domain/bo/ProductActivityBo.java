package org.dromara.live.domain.bo;

import org.dromara.live.domain.ProductActivity;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 产品活动业务对象 live_product_activity
 *
 * @author xx
 * @date 2024-05-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ProductActivity.class, reverseConvertGenerate = false)
public class ProductActivityBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 活动编号
     */
    @NotNull(message = "活动编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long activityId;

    /**
     * 产品代码
     */
    @NotBlank(message = "产品代码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String productCode;

    /**
     * 产品名称
     */
    @NotBlank(message = "产品名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String productName;

    /**
     * 入选日期
     */
    @NotBlank(message = "入选日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String productDate;

    /**
     * 入选价格
     */
    @NotNull(message = "入选价格不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal productAmount;

    /**
     * 入选后当天价格
     */
    private BigDecimal productAmountNow;

    /**
     * 入选后1天价格
     */
    private BigDecimal productAmount1;

    /**
     * 入选后2天价格
     */
    private BigDecimal productAmount2;

    /**
     * 入选后3天价格
     */
    private BigDecimal productAmount3;

    /**
     * 查询条件
     */
    private String queryType;
}
