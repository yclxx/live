package org.dromara.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.live.domain.ProductMoneyLog;
import org.dromara.live.domain.bo.ProductMoneyLogBo;
import org.dromara.live.domain.vo.ProductMoneyLogVo;
import org.dromara.live.mapper.ProductMoneyLogMapper;
import org.dromara.live.service.IProductMoneyLogService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 资金流向Service业务层处理
 *
 * @author xx
 * @date 2024-06-11
 */
@RequiredArgsConstructor
@Service
public class ProductMoneyLogServiceImpl implements IProductMoneyLogService {

    private final ProductMoneyLogMapper baseMapper;

    /**
     * 查询资金流向
     *
     * @param id 主键
     * @return 资金流向
     */
    @Override
    public ProductMoneyLogVo queryById(String id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询产品资金流向
     *
     * @param productCode 产品代码
     * @return 产品资金流向
     */
    @Override
    public ProductMoneyLogVo queryLastByProductCode(String productCode) {
        LambdaQueryWrapper<ProductMoneyLog> lqw = Wrappers.lambdaQuery();
        lqw.eq(ProductMoneyLog::getProductCode, productCode);
        lqw.last("order by info_date desc limit 1");
        return baseMapper.selectVoOne(lqw);
    }

    /**
     * 分页查询资金流向列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 资金流向分页列表
     */
    @Override
    public TableDataInfo<ProductMoneyLogVo> queryPageList(ProductMoneyLogBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ProductMoneyLog> lqw = buildQueryWrapper(bo);
        Page<ProductMoneyLogVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的资金流向列表
     *
     * @param bo 查询条件
     * @return 资金流向列表
     */
    @Override
    public List<ProductMoneyLogVo> queryList(ProductMoneyLogBo bo) {
        LambdaQueryWrapper<ProductMoneyLog> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ProductMoneyLog> buildQueryWrapper(ProductMoneyLogBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ProductMoneyLog> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getProductCode()), ProductMoneyLog::getProductCode, bo.getProductCode());
        lqw.like(StringUtils.isNotBlank(bo.getProductName()), ProductMoneyLog::getProductName, bo.getProductName());
        lqw.eq(StringUtils.isNotBlank(bo.getInfoDate()), ProductMoneyLog::getInfoDate, bo.getInfoDate());
        return lqw;
    }

    /**
     * 新增资金流向
     *
     * @param bo 资金流向
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(ProductMoneyLogBo bo) {
        ProductMoneyLog add = MapstructUtils.convert(bo, ProductMoneyLog.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 新增资金流向
     *
     * @param bo 资金流向
     */
    @Override
    public void insertBatch(List<ProductMoneyLog> bo) {
        baseMapper.insertBatch(bo);
    }

    /**
     * 修改资金流向
     *
     * @param bo 资金流向
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(ProductMoneyLogBo bo) {
        ProductMoneyLog update = MapstructUtils.convert(bo, ProductMoneyLog.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ProductMoneyLog entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除资金流向信息
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

    /**
     * 校验并批量删除产品记录信息
     *
     * @param productCode 产品代码
     */
    @Override
    public void deleteByProductCodeAndInfoDateLessThan(String productCode, String infoDate) {
        baseMapper.delete(Wrappers.<ProductMoneyLog>lambdaQuery().eq(ProductMoneyLog::getProductCode, productCode).lt(ProductMoneyLog::getInfoDate, infoDate));
    }
}
