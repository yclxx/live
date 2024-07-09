package org.dromara.live.domain.vo;

import java.math.BigDecimal;
import org.dromara.live.domain.ProductActivity;
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
 * 产品活动视图对象 live_product_activity
 *
 * @author xx
 * @date 2024-05-28
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ProductActivity.class)
public class ProductActivityVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 活动编号
     */
    @ExcelProperty(value = "活动编号")
    private Long activityId;

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
     * 入选日期
     */
    @ExcelProperty(value = "入选日期")
    private String productDate;

    /**
     * 入选价格
     */
    @ExcelProperty(value = "入选价格")
    private BigDecimal productAmount;

    /**
     * 入选后当天价格
     */
    @ExcelProperty(value = "入选后当天价格")
    private BigDecimal productAmountNow;

    /**
     * 入选后1天价格
     */
    @ExcelProperty(value = "入选后1天价格")
    private BigDecimal productAmount1;

    /**
     * 入选后2天价格
     */
    @ExcelProperty(value = "入选后2天价格")
    private BigDecimal productAmount2;

    /**
     * 入选后3天价格
     */
    @ExcelProperty(value = "入选后3天价格")
    private BigDecimal productAmount3;

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

    private GpInfoVo gpInfoVo;
}
