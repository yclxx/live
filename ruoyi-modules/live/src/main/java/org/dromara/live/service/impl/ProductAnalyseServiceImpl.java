package org.dromara.live.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.dromara.live.domain.bo.ProductAnalyseBo;
import org.dromara.live.domain.vo.ProductAnalyseVo;
import org.dromara.live.domain.ProductAnalyse;
import org.dromara.live.mapper.ProductAnalyseMapper;
import org.dromara.live.service.IProductAnalyseService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 统计分析Service业务层处理
 *
 * @author xx
 * @date 2024-06-04
 */
@RequiredArgsConstructor
@Service
public class ProductAnalyseServiceImpl implements IProductAnalyseService {

    private final ProductAnalyseMapper baseMapper;

    /**
     * 查询统计分析
     *
     * @param id 主键
     * @return 统计分析
     */
    @Override
    public ProductAnalyseVo queryById(String id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询统计分析
     *
     * @return 统计分析
     */
    @Override
    public ProductAnalyseVo queryByUnique(String infoDate, String analyseNo) {
        LambdaQueryWrapper<ProductAnalyse> lqw = Wrappers.lambdaQuery();
        lqw.eq(ProductAnalyse::getInfoDate, infoDate);
        lqw.eq(ProductAnalyse::getAnalyseNo, analyseNo);
        lqw.last("limit 1");
        return baseMapper.selectVoOne(lqw);
    }

    /**
     * 分页查询统计分析列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 统计分析分页列表
     */
    @Override
    public TableDataInfo<ProductAnalyseVo> queryPageList(ProductAnalyseBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ProductAnalyse> lqw = buildQueryWrapper(bo);
        Page<ProductAnalyseVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的统计分析列表
     *
     * @param bo 查询条件
     * @return 统计分析列表
     */
    @Override
    public List<ProductAnalyseVo> queryList(ProductAnalyseBo bo) {
        LambdaQueryWrapper<ProductAnalyse> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 查询符合条件的统计分析列表
     *
     * @return 统计分析列表
     */
    @Override
    public List<ProductAnalyseVo> queryListByEdit() {
        LambdaQueryWrapper<ProductAnalyse> lqw = Wrappers.lambdaQuery();
        lqw.isNull(ProductAnalyse::getVerifyJson).or().isNotNull(ProductAnalyse::getAccuracy);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ProductAnalyse> buildQueryWrapper(ProductAnalyseBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ProductAnalyse> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getInfoDate()), ProductAnalyse::getInfoDate, bo.getInfoDate());
        lqw.eq(StringUtils.isNotBlank(bo.getAnalyseNo()), ProductAnalyse::getAnalyseNo, bo.getAnalyseNo());
        return lqw;
    }

    /**
     * 新增统计分析
     *
     * @param bo 统计分析
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(ProductAnalyseBo bo) {
        ProductAnalyse add = MapstructUtils.convert(bo, ProductAnalyse.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改统计分析
     *
     * @param bo 统计分析
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(ProductAnalyseBo bo) {
        ProductAnalyse update = MapstructUtils.convert(bo, ProductAnalyse.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ProductAnalyse entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除统计分析信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
