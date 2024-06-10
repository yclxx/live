package org.dromara.live.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

import java.io.Serial;

/**
 * 产品记录对象 live_product_log
 *
 * @author xx
 * @date 2024-06-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("live_product_log")
public class ProductLog extends TenantEntity {

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
     * 开盘价
     */
    private BigDecimal f17;

    /**
     * 收盘价
     */
    private BigDecimal f2;

    /**
     * 最高价
     */
    private BigDecimal f15;

    /**
     * 最低价
     */
    private BigDecimal f16;

    /**
     * 成交量（手）
     */
    private Long f5;

    /**
     * 成交额（元）
     */
    private BigDecimal f6;

    /**
     * 振幅(%)
     */
    private BigDecimal f7;

    /**
     * 涨跌幅(%)
     */
    private BigDecimal f3;

    /**
     * 涨跌额
     */
    private BigDecimal f4;

    /**
     * 换手率
     */
    private BigDecimal f8;

    /**
     * 5日均价
     */
    private BigDecimal ma5;

    /**
     * 10日均价
     */
    private BigDecimal ma10;

    /**
     * 20日均价
     */
    private BigDecimal ma20;


}
