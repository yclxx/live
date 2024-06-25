package org.dromara.live.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 分时成交结果对象
 * @author 25487
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GpTimeSharingResultVo {
    private Long rc;
    private Long rt;
    private Long svr;
    private Long lt;
    private Long full;
    private String dlmkts;
    private GpTimeSharingDataVo data;
}
