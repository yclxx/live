package org.dromara.live.service;

import org.dromara.live.domain.vo.ProductAnalyseVo;
import org.dromara.live.domain.bo.ProductAnalyseBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 统计分析Service接口
 *
 * @author xx
 * @date 2024-06-04
 */
public interface IProductAnalyseService {

    /**
     * 查询统计分析
     *
     * @param id 主键
     * @return 统计分析
     */
    ProductAnalyseVo queryById(String id);

    /**
     * 查询统计分析
     *
     * @return 统计分析
     */
    ProductAnalyseVo queryByUnique(String infoDate,String analyseNo);

    /**
     * 分页查询统计分析列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 统计分析分页列表
     */
    TableDataInfo<ProductAnalyseVo> queryPageList(ProductAnalyseBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的统计分析列表
     *
     * @param bo 查询条件
     * @return 统计分析列表
     */
    List<ProductAnalyseVo> queryList(ProductAnalyseBo bo);

    /**
     * 查询符合条件的统计分析列表
     *
     * @return 统计分析列表
     */
    List<ProductAnalyseVo> queryListByEdit();

    /**
     * 新增统计分析
     *
     * @param bo 统计分析
     * @return 是否新增成功
     */
    Boolean insertByBo(ProductAnalyseBo bo);

    /**
     * 修改统计分析
     *
     * @param bo 统计分析
     * @return 是否修改成功
     */
    Boolean updateByBo(ProductAnalyseBo bo);

    /**
     * 校验并批量删除统计分析信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
