package org.dromara.live.domain.vo;

import com.alibaba.excel.util.StringUtils;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 资金流向
 *
 * @author 25487
 */
@Data
public class GpMoneyVo {
    private String id;
    /**
     * 产品代码
     */
    private String productCode;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 日期 2024-05-24
     */
    private String infoDate;
    /**
     * 主力净流入
     */
    private BigDecimal f62;
    /**
     * 小单净流入
     */
    private BigDecimal f84;
    /**
     * 中单净流入
     */
    private BigDecimal f78;
    /**
     * 大单净流入
     */
    private BigDecimal f72;
    /**
     * 超大单净流入
     */
    private BigDecimal f66;
    /**
     * 主力净流入 净占比
     */
    private BigDecimal f184;
    /**
     * 小单净流入 净占比
     */
    private BigDecimal f87;
    /**
     * 中单净流入 净占比
     */
    private BigDecimal f81;
    /**
     * 大单净流入 净占比
     */
    private BigDecimal f75;
    /**
     * 超大单净流入 净占比
     */
    private BigDecimal f69;

    /**
     * 初始化数据
     *
     * @param str 当日数据
     *            格式：时间,主力净流入,小单净流入,中单净流入,大单净流入,超大单净流入,主力净流入 净占比,小单净流入 净占比,中单净流入 净占比,大单净流入 净占比,超大单净流入 净占比
     *            2024-06-11,-16561726.0,23630790.0,-7069064.0,-1585349.0,-14976377.0,-7.97,11.37,-3.40,-0.76,-7.21
     */
    public GpMoneyVo(String str, String code, String name) {
        if (StringUtils.isBlank(str)) {
            throw new RuntimeException("数据为空");
        }
        this.productCode = code;
        this.productName = name;
        String[] split = str.split(",");
        for (int i = 0; i < split.length; i++) {
            switch (i) {
                case 0:
                    this.infoDate = split[i];
                    break;
                case 1:
                    this.f62 = new BigDecimal(split[i]);
                    break;
                case 2:
                    this.f84 = new BigDecimal(split[i]);
                    break;
                case 3:
                    this.f78 = new BigDecimal(split[i]);
                    break;
                case 4:
                    this.f72 = new BigDecimal(split[i]);
                    break;
                case 5:
                    this.f66 = new BigDecimal(split[i]);
                    break;
                case 6:
                    this.f184 = new BigDecimal(split[i]);
                    break;
                case 7:
                    this.f87 = new BigDecimal(split[i]);
                    break;
                case 8:
                    this.f81 = new BigDecimal(split[i]);
                    break;
                case 9:
                    this.f75 = new BigDecimal(split[i]);
                    break;
                case 10:
                    this.f69 = new BigDecimal(split[i]);
                    break;
            }
        }
        this.id = this.infoDate + this.productCode;
    }
}
