package org.dromara.live.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import org.dromara.live.domain.ProductLog;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 产品记录视图对象 live_product_log
 *
 * @author xx
 * @date 2024-06-02
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ProductLog.class)
public class ProductLogVo implements Serializable {

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
     * 开盘价
     */
    @ExcelProperty(value = "开盘价")
    private BigDecimal f17;

    /**
     * 收盘价
     */
    @ExcelProperty(value = "收盘价")
    private BigDecimal f2;

    /**
     * 最高价
     */
    @ExcelProperty(value = "最高价")
    private BigDecimal f15;

    /**
     * 最低价
     */
    @ExcelProperty(value = "最低价")
    private BigDecimal f16;

    /**
     * 成交量（手）
     */
    @ExcelProperty(value = "成交量", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "手=")
    private Long f5;

    /**
     * 成交额（元）
     */
    @ExcelProperty(value = "成交额", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "元=")
    private BigDecimal f6;

    /**
     * 振幅(%)
     */
    @ExcelProperty(value = "振幅(%)")
    private BigDecimal f7;

    /**
     * 涨跌幅(%)
     */
    @ExcelProperty(value = "涨跌幅(%)")
    private BigDecimal f3;

    /**
     * 涨跌额
     */
    @ExcelProperty(value = "涨跌额")
    private BigDecimal f4;

    /**
     * 换手率
     */
    @ExcelProperty(value = "换手率")
    private BigDecimal f8;

    /**
     * 5日均价
     */
    @ExcelProperty(value = "5日均价")
    private BigDecimal ma5;

    /**
     * 10日均价
     */
    @ExcelProperty(value = "10日均价")
    private BigDecimal ma10;

    /**
     * 20日均价
     */
    @ExcelProperty(value = "20日均价")
    private BigDecimal ma20;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;


}
