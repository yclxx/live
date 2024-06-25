package org.dromara.live.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 分时成交结果对象
 * @author 25487
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GpTimeSharingDataVo {
    private String code;
    private Long market;
    private Long decimal;
    private BigDecimal prePrice;
    private String[] details;
}
