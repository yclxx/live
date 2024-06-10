package org.dromara.live.domain.bo;

import org.dromara.live.domain.ProductAnalyse;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 统计分析业务对象 live_product_analyse
 *
 * @author xx
 * @date 2024-06-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ProductAnalyse.class, reverseConvertGenerate = false)
public class ProductAnalyseBo extends BaseEntity {

    /**
     * 主键
     */
    @NotBlank(message = "主键不能为空", groups = { EditGroup.class })
    private String id;

    /**
     * 日期
     */
    @NotBlank(message = "日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String infoDate;

    /**
     * 规则Id
     */
    @NotBlank(message = "规则Id不能为空", groups = { AddGroup.class, EditGroup.class })
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
