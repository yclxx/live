package org.dromara.live.domain.vo;

import com.alibaba.excel.util.StringUtils;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 分时成交结果对象
 *
 * @author 25487
 */
@Data
public class GpTimeSharingInfoVo {
    /**
     * 分时秒
     */
    private String time;
    /**
     * 成交价
     */
    private BigDecimal f2;
    /**
     * 成交量（手）
     */
    private Integer f5;
    /**
     * 成交单数
     */
    private Integer n1;
    /**
     * 4 集合竞价
     * 2 主动性买盘 红
     * 1 主动性卖盘 绿
     */
    private Integer n2;

    /**
     * 初始化数据
     *
     * @param str 当日数据
     *            格式：时间,最新价,成交量（手）,成交单数,主动性买卖盘（1：主动性卖盘，2：主动性买盘）,
     *            13:23:35,22.43,99,7,1
     */
    public GpTimeSharingInfoVo(String str) {
        if (StringUtils.isBlank(str)) {
            throw new RuntimeException("数据为空");
        }
        String[] split = str.split(",");
        for (int i = 0; i < split.length; i++) {
            switch (i) {
                case 0:
                    this.time = split[i];
                    break;
                case 1:
                    this.f2 = new BigDecimal(split[i]);
                    break;
                case 2:
                    this.f5 = Integer.valueOf(split[i]);
                    break;
                case 3:
                    this.n1 = Integer.valueOf(split[i]);
                    break;
                case 4:
                    this.n2 = Integer.valueOf(split[i]);
                    break;
            }
        }
    }
}
