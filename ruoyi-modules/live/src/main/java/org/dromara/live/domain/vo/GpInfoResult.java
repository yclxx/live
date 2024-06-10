package org.dromara.live.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author 25487
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ExcelIgnoreUnannotated
@Data
public class GpInfoResult {
    @ExcelProperty(value = "代码")
    private String code;
    private Long market;
    @ExcelProperty(value = "名称")
    private String name;
    private Long decimal;
    private Long dktotal;
    private Long preKPrice;
    private List<String> klines;
}
