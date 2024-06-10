package org.dromara.live.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author 25487
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GpInfoResultAll {
    private Long rc;
    private Long rt;
    private Long svr;
    private Long lt;
    private Long full;
    private String dlmkts;
    private GpInfoResult data;
}
