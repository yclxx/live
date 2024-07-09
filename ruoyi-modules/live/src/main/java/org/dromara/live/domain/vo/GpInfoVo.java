package org.dromara.live.domain.vo;

import com.alibaba.excel.util.StringUtils;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author 25487
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoMapper(target = ProductLogVo.class, reverseConvertGenerate = false)
public class GpInfoVo extends BaseEntity {

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
     * 开盘价
     */
    private BigDecimal f17;
    /**
     * 收盘价
     */
    private BigDecimal f2;
    /**
     * 最高价
     */
    private BigDecimal f15;
    /**
     * 最低
     */
    private BigDecimal f16;
    /**
     * 成交量（手）
     */
    private Long f5;
    /**
     * 成交额（元）
     */
    private BigDecimal f6;
    /**
     * 振幅(%)
     */
    private BigDecimal f7;
    /**
     * 涨跌幅(%)
     */
    private BigDecimal f3;
    /**
     * 涨跌额
     */
    private BigDecimal f4;
    /**
     * 换手率
     */
    private BigDecimal f8;
    /**
     * 5日均价
     */
    private BigDecimal ma5;
    /**
     * 10日均价
     */
    private BigDecimal ma10;
    /**
     * 20日均价
     */
    private BigDecimal ma20;

    /**
     * 10日最高价
     */
    private BigDecimal max10;

    /**
     * 10日最高价 是哪天
     */
    private String max10Date;

    /**
     * 权重
     */
    private Integer weight;

    public GpInfoVo() {
    }

    /**
     * 初始化数据
     *
     * @param str 当日数据
     *            格式：时间,开盘价,收盘价,最高价,最低价,成交量(手),成交额(元),振幅(%),涨跌幅(%),涨跌额(元),换手率(%),
     *            2024-05-23,22.67,22.19,22.75,22.09,28908,64456705.00,2.89,-2.72,-0.62,1.52
     *            2024-05-24,22.32,26.63,26.63,22.28,172434,433760762.90,19.60,20.01,4.44,9.06
     */
    public GpInfoVo(String str, String code, String name) {
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
                    this.f17 = new BigDecimal(split[i]);
                    break;
                case 2:
                    this.f2 = new BigDecimal(split[i]);
                    break;
                case 3:
                    this.f15 = new BigDecimal(split[i]);
                    break;
                case 4:
                    this.f16 = new BigDecimal(split[i]);
                    break;
                case 5:
                    this.f5 = Long.valueOf(split[i]);
                    break;
                case 6:
                    this.f6 = new BigDecimal(split[i]);
                    break;
                case 7:
                    this.f7 = new BigDecimal(split[i]);
                    break;
                case 8:
                    this.f3 = new BigDecimal(split[i]);
                    break;
                case 9:
                    this.f4 = new BigDecimal(split[i]);
                    break;
                case 10:
                    this.f8 = new BigDecimal(split[i]);
                    break;
            }
        }
        this.id = this.infoDate + this.productCode;
    }

    public void setMa(List<GpInfoVo> gpInfoVoList, int index) {
        if (index < 21 || gpInfoVoList.size() < 21) {
            return;
        }
        index = index + 1;
        // 取出最后5条数据
        List<GpInfoVo> ma5List = gpInfoVoList.subList(index - 5, index);
        // 计算ma5
        BigDecimal reduce = ma5List.stream().map(GpInfoVo::getF2).reduce(BigDecimal.ZERO, BigDecimal::add);
        this.ma5 = reduce.divide(new BigDecimal("5"), 2, RoundingMode.HALF_UP);
        // 取出最后10条数据
        List<GpInfoVo> ma10List = gpInfoVoList.subList(index - 10, index);
        BigDecimal reduce1 = ma10List.stream().map(GpInfoVo::getF2).reduce(BigDecimal.ZERO, BigDecimal::add);
        this.ma10 = reduce1.divide(new BigDecimal("10"), 2, RoundingMode.HALF_UP);
        // 取出最后20条数据
        List<GpInfoVo> ma20List = gpInfoVoList.subList(index - 20, index);
        BigDecimal reduce2 = ma20List.stream().map(GpInfoVo::getF2).reduce(BigDecimal.ZERO, BigDecimal::add);
        this.ma20 = reduce2.divide(new BigDecimal("20"), 2, RoundingMode.HALF_UP);
        // 计算10天的最高价
        this.max10 = ma10List.stream().map(GpInfoVo::getF15).reduce(BigDecimal.ZERO, BigDecimal::max);
        // 最高价是哪条数据
        this.max10Date = ma10List.stream().filter(e -> e.getF15().compareTo(this.max10) == 0).findFirst().get().getInfoDate();
    }
}
