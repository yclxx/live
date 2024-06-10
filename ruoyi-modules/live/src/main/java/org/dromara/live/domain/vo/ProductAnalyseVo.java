package org.dromara.live.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.live.domain.ProductAnalyse;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 统计分析视图对象 live_product_analyse
 *
 * @author xx
 * @date 2024-06-04
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ProductAnalyse.class)
public class ProductAnalyseVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private String id;

    /**
     * 日期
     */
    @ExcelProperty(value = "日期")
    private String infoDate;

    /**
     * 规则Id
     */
    @ExcelProperty(value = "规则Id")
    private String analyseNo;

    /**
     * 统计结果
     */
    @ExcelProperty(value = "统计结果")
    private String analyseJson;

    /**
     * 最后推荐的数据
     */
    private List<String> analyseList;

    /**
     * 验证结果
     */
    @ExcelProperty(value = "验证结果")
    private String verifyJson;

    /**
     * 转json后数据
     */
    private List<String> verifyList;

    /**
     * 正确率
     */
    @ExcelProperty(value = "正确率")
    private BigDecimal accuracy;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private Date updateTime;

}
