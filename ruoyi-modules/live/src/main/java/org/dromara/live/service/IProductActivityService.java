package org.dromara.live.service;

import org.dromara.live.domain.vo.ProductActivityVo;
import org.dromara.live.domain.bo.ProductActivityBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 产品活动Service接口
 *
 * @author xx
 * @date 2024-05-28
 */
public interface IProductActivityService {

    /**
     * 查询产品活动
     *
     * @param id 主键
     * @return 产品活动
     */
    ProductActivityVo queryById(Long id);

    /**
     * 查询产品活动
     *
     * @param bo 主键
     * @return 产品活动
     */
    ProductActivityVo queryByProductCodeAndActivityId(ProductActivityBo bo);

    /**
     * 分页查询产品活动列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 产品活动分页列表
     */
    TableDataInfo<ProductActivityVo> queryPageList(ProductActivityBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的产品活动列表
     *
     * @param bo 查询条件
     * @return 产品活动列表
     */
    List<ProductActivityVo> queryList(ProductActivityBo bo);

    /**
     * 查询最近7天新增产品活动列表
     *
     * @return 产品活动列表
     */
    List<ProductActivityVo> queryListByThreeDay();

    /**
     * 新增产品活动
     *
     * @param bo 产品活动
     * @return 是否新增成功
     */
    Boolean insertByBo(ProductActivityBo bo);

    /**
     * 修改产品活动
     *
     * @param bo 产品活动
     * @return 是否修改成功
     */
    Boolean updateByBo(ProductActivityBo bo);

    /**
     * 校验并批量删除产品活动信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
