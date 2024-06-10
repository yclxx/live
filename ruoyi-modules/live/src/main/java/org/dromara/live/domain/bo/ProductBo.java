package org.dromara.live.domain.bo;

import org.dromara.live.domain.Product;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 产品管理业务对象 live_product
 *
 * @author xx
 * @date 2024-05-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Product.class, reverseConvertGenerate = false)
public class ProductBo extends BaseEntity {

    /**
     * 产品代码
     */
    @NotBlank(message = "产品代码不能为空", groups = { EditGroup.class })
    private String productCode;

    /**
     * 产品名称
     */
    @NotBlank(message = "产品名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String productName;

    /**
     * 状态
     */
    @NotBlank(message = "状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;

    /**
     * 类型
     */
    private String productType;

    /**
     * 排序
     */
    private Long sort;
}
