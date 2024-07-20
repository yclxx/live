package org.dromara.live.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

import java.io.Serial;

/**
 * 产品活动对象 live_product_activity
 *
 * @author xx
 * @date 2024-05-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("live_product_activity")
public class ProductActivity extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 活动编号
     */
    private Long activityId;

    /**
     * 产品代码
     */
    private String productCode;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 入选日期
     */
    private String productDate;

    /**
     * 入选价格
     */
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
     * 选择状态：1-选择，2-默认，3-丢弃
     */
    private String selectStatus;
}
