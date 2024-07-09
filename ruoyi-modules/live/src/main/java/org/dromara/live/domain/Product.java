package org.dromara.live.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 产品管理对象 live_product
 *
 * @author xx
 * @date 2024-05-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("live_product")
public class Product extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 产品代码
     */
    @TableId(value = "product_code")
    private String productCode;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 状态
     */
    private String status;

    /**
     * 类型
     */
    private String productType;

    /**
     * 排序
     */
    private Long sort;
    /**
     * 总市值
     */
    private BigDecimal f116;
    /**
     * 流通市值
     */
    private BigDecimal f117;
}
