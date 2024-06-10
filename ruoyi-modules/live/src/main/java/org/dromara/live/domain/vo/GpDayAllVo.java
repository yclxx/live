package org.dromara.live.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author 25487
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GpDayAllVo {
    /** 代码 */
    private String f12;
    /** 名称 */
    private String f14;
    /** 最新价 */
    private String f2;
    /** 涨跌幅 */
    private String f3;
    /** 涨跌额 */
    private String f4;
    /** 成交量（手） */
    private String f5;
    /** 成交额 */
    private String f6;
    /** 振幅(%) */
    private String f7;
    /** 最高 */
    private String f15;
    /** 最低 */
    private String f16;
    /** 今开 */
    private String f17;
    /** 昨收 */
    private String f18;
    /** 量比 */
    private String f10;
    /** 换手率 */
    private String f8;
}
