package org.dromara.live.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 25487
 */
@Data
public class PushListVo {
    private String infoDate;
    private List<String> pushList;
    private List<String> okList;
    private BigDecimal okRate;
    private BigDecimal avgRate;
}
