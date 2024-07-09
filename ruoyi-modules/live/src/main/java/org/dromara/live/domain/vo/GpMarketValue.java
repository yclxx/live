package org.dromara.live.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xiexi
 * @description
 * @date 2024/7/7 11:22
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GpMarketValue {
    /**
     * 编码
     */
    private String f57;
    /**
     * 产品名称
     */
    private String f58;
    /**
     * 总市值
     */
    private BigDecimal f116;
    /**
     * 流通市值
     */
    private BigDecimal f117;
}
