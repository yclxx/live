package org.dromara.live.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.live.domain.ProductMoneyLog;
import org.dromara.live.domain.bo.ProductMoneyLogBo;
import org.dromara.live.domain.vo.ProductMoneyLogVo;

import java.util.Collection;
import java.util.List;

/**
 * 资金流向Service接口
 *
 * @author xx
 * @date 2024-06-11
 */
public interface IProductMoneyLogService {

    /**
     * 查询资金流向
     *
     * @param id 主键
     * @return 资金流向
     */
    ProductMoneyLogVo queryById(String id);

    /**
     * 查询产品资金流向
     *
     * @param productCode 产品代码
     * @return 产品资金流向
     */
    ProductMoneyLogVo queryLastByProductCode(String productCode);

    /**
     * 分页查询资金流向列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 资金流向分页列表
     */
    TableDataInfo<ProductMoneyLogVo> queryPageList(ProductMoneyLogBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的资金流向列表
     *
     * @param bo 查询条件
     * @return 资金流向列表
     */
    List<ProductMoneyLogVo> queryList(ProductMoneyLogBo bo);

    /**
     * 新增资金流向
     *
     * @param bo 资金流向
     * @return 是否新增成功
     */
    Boolean insertByBo(ProductMoneyLogBo bo);

    /**
     * 新增资金流向
     *
     * @param bo 资金流向
     * @return 是否新增成功
     */
    void insertBatch(List<ProductMoneyLog> bo);

    /**
     * 修改资金流向
     *
     * @param bo 资金流向
     * @return 是否修改成功
     */
    Boolean updateByBo(ProductMoneyLogBo bo);

    /**
     * 校验并批量删除资金流向信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);

    /**
     * 校验并批量删除产品记录信息
     *
     * @param productCode 产品代码
     * @return 是否删除成功
     */
    void deleteByProductCodeAndInfoDateLessThan(String productCode, String infoDate);
}
