package org.dromara.live.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author 25487
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GpTableData {
    private Integer total;
    private List<GpDayAllVo> diff;
}
