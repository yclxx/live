package org.dromara.live.domain.vo;

import org.dromara.live.domain.Product;
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
 * 产品管理视图对象 live_product
 *
 * @author xx
 * @date 2024-05-28
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Product.class)
public class ProductVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
     * 状态
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_normal_disable")
    private String status;

    /**
     * 类型
     */
    @ExcelProperty(value = "类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "live_product_type")
    private String productType;

    /**
     * 排序
     */
    private Long sort;

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
