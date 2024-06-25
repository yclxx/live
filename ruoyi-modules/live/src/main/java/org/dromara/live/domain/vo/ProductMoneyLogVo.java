package org.dromara.live.domain.vo;

import java.math.BigDecimal;
import org.dromara.live.domain.ProductMoneyLog;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 资金流向视图对象 live_product_money_log
 *
 * @author xx
 * @date 2024-06-11
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ProductMoneyLog.class)
public class ProductMoneyLogVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private String id;

    /**
     * 产品代码
     */
    @ExcelProperty(value = "产品代码")
    private String productCode;

    /**
     * 产品名称
     */
    @ExcelProperty(value = "产品名称")
    private String productName;

    /**
     * 日期
     */
    @ExcelProperty(value = "日期")
    private String infoDate;

    /**
     * 主力净流入
     */
    @ExcelProperty(value = "主力净流入")
    private BigDecimal f62;

    /**
     * 小单净流入
     */
    @ExcelProperty(value = "小单净流入")
    private BigDecimal f84;

    /**
     * 中单净流入
     */
    @ExcelProperty(value = "中单净流入")
    private BigDecimal f78;

    /**
     * 大单净流入
     */
    @ExcelProperty(value = "大单净流入")
    private BigDecimal f72;

    /**
     * 超大单净流入
     */
    @ExcelProperty(value = "超大单净流入")
    private BigDecimal f66;

    /**
     * 主力净流入 净占比
     */
    @ExcelProperty(value = "主力净流入 净占比")
    private BigDecimal f184;

    /**
     * 小单净流入 净占比
     */
    @ExcelProperty(value = "小单净流入 净占比")
    private BigDecimal f87;

    /**
     * 中单净流入 净占比
     */
    @ExcelProperty(value = "中单净流入 净占比")
    private BigDecimal f81;

    /**
     * 大单净流入 净占比
     */
    @ExcelProperty(value = "大单净流入 净占比")
    private BigDecimal f75;

    /**
     * 超大单净流入 净占比
     */
    @ExcelProperty(value = "超大单净流入 净占比")
    private BigDecimal f69;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private Date updateTime;


}
