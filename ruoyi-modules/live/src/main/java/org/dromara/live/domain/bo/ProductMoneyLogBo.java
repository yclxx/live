package org.dromara.live.domain.bo;

import org.dromara.live.domain.ProductMoneyLog;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 资金流向业务对象 live_product_money_log
 *
 * @author xx
 * @date 2024-06-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ProductMoneyLog.class, reverseConvertGenerate = false)
public class ProductMoneyLogBo extends BaseEntity {

    /**
     * 主键
     */
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
