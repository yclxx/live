package org.dromara.live.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.live.domain.Product;
import org.dromara.live.domain.bo.ProductBo;
import org.dromara.live.domain.vo.ProductVo;

import java.util.Collection;
import java.util.List;

/**
 * 产品管理Service接口
 *
 * @author xx
 * @date 2024-05-28
 */
public interface IProductService {

    /**
     * 查询产品管理
     *
     * @param productCode 主键
     * @return 产品管理
     */
    ProductVo queryById(String productCode);

    /**
     * 分页查询产品管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 产品管理分页列表
     */
    TableDataInfo<ProductVo> queryPageList(ProductBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的产品管理列表
     *
     * @param bo 查询条件
     * @return 产品管理列表
     */
    List<ProductVo> queryList(ProductBo bo);

    /**
     * 新增产品管理
     *
     * @param productList 产品管理
     */
    void insertByBo(List<Product> productList);

    /**
     * 修改产品管理
     *
     * @param bo 产品管理
     * @return 是否修改成功
     */
    Boolean updateByBo(ProductBo bo);

    /**
     * 校验并批量删除产品管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);

    /**
     * 获取最靠前的排序值
     *
     * @return 排序值
     */
    Long getMaxSort();

    /**
     * 查询产品记录遗漏的数据
     *
     * @return 产品记录
     */
    List<ProductVo> querySupplementList(String notInSql);
}
