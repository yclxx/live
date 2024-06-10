package org.dromara.live.domain.bo;

import org.dromara.live.domain.ProductLog;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * 产品记录业务对象 live_product_log
 *
 * @author xx
 * @date 2024-06-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ProductLog.class, reverseConvertGenerate = false)
public class ProductLogBo extends BaseEntity {

    /**
     * 主键
     */
    @NotBlank(message = "主键不能为空", groups = {EditGroup.class})
    private String id;

    /**
     * 产品代码
     */
    @NotBlank(message = "产品代码不能为空", groups = {AddGroup.class, EditGroup.class})
    private String productCode;

    /**
     * 产品名称
     */
    @NotBlank(message = "产品名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String productName;

    /**
     * 日期
     */
    @NotBlank(message = "日期不能为空", groups = {AddGroup.class, EditGroup.class})
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
