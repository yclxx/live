package org.dromara.live.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

import java.io.Serial;

/**
 * 统计分析对象 live_product_analyse
 *
 * @author xx
 * @date 2024-06-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("live_product_analyse")
public class ProductAnalyse extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 日期
     */
    private String infoDate;

    /**
     * 规则Id
     */
    private String analyseNo;

    /**
     * 统计结果
     */
    private String analyseJson;

    /**
     * 验证结果
     */
    private String verifyJson;

    /**
     * 正确率
     */
    private BigDecimal accuracy;


}
