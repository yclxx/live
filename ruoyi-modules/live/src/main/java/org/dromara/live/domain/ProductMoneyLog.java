package org.dromara.live.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

import java.io.Serial;

/**
 * 资金流向对象 live_product_money_log
 *
 * @author xx
 * @date 2024-06-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("live_product_money_log")
public class ProductMoneyLog extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 产品代码
     */
    private String productCode;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 日期
     */
    private String infoDate;

    /**
     * 主力净流入
     */
    private BigDecimal f62;

    /**
     * 小单净流入
     */
    private BigDecimal f84;

    /**
     * 中单净流入
     */
    private BigDecimal f78;

    /**
     * 大单净流入
     */
    private BigDecimal f72;

    /**
     * 超大单净流入
     */
    private BigDecimal f66;

    /**
     * 主力净流入 净占比
     */
    private BigDecimal f184;

    /**
     * 小单净流入 净占比
     */
    private BigDecimal f87;

    /**
     * 中单净流入 净占比
     */
    private BigDecimal f81;

    /**
     * 大单净流入 净占比
     */
    private BigDecimal f75;

    /**
     * 超大单净流入 净占比
     */
    private BigDecimal f69;


}
