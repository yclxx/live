package org.dromara.live.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.live.domain.ProductActivity;
import org.dromara.live.domain.bo.ProductActivityBo;
import org.dromara.live.domain.vo.ProductActivityVo;
import org.dromara.live.domain.vo.ProductLogVo;
import org.dromara.live.mapper.ProductActivityMapper;
import org.dromara.live.service.IProductActivityService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 产品活动Service业务层处理
 *
 * @author xx
 * @date 2024-05-28
 */
@RequiredArgsConstructor
@Service
public class ProductActivityServiceImpl implements IProductActivityService {

    private final ProductActivityMapper baseMapper;

    /**
     * 查询产品活动
     *
     * @param id 主键
     * @return 产品活动
     */
    @Override
    public ProductActivityVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询产品活动
     *
     * @param bo 主键
     * @return 产品活动
     */
    @Override
    public ProductActivityVo queryByProductCodeAndActivityId(ProductActivityBo bo) {
        LambdaQueryWrapper<ProductActivity> lqw = Wrappers.lambdaQuery();
        lqw.eq(ProductActivity::getActivityId, bo.getActivityId());
        lqw.eq(ProductActivity::getProductCode, bo.getProductCode());
        lqw.eq(ProductActivity::getProductDate, bo.getProductDate());
        lqw.last("limit 1");
        return baseMapper.selectVoOne(lqw);
    }

    /**
     * 分页查询产品活动列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 产品活动分页列表
     */
    @Override
    public TableDataInfo<ProductActivityVo> queryPageList(ProductActivityBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ProductActivity> lqw = buildQueryWrapper(bo);
        Page<ProductActivityVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的产品活动列表
     *
     * @param bo 查询条件
     * @return 产品活动列表
     */
    @Override
    public List<ProductActivityVo> queryList(ProductActivityBo bo) {
        LambdaQueryWrapper<ProductActivity> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 查询最近7天新增产品活动列表
     *
     * @return 产品活动列表
     */
    @Override
    public List<ProductActivityVo> queryListByThreeDay() {
        LambdaQueryWrapper<ProductActivity> lqw = Wrappers.lambdaQuery();
        lqw.gt(ProductActivity::getCreateTime, DateUtil.beginOfDay(DateUtil.offsetDay(DateUtil.date(), -7)));
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ProductActivity> buildQueryWrapper(ProductActivityBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ProductActivity> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getActivityId() != null, ProductActivity::getActivityId, bo.getActivityId());
        lqw.eq(StringUtils.isNotBlank(bo.getProductCode()), ProductActivity::getProductCode, bo.getProductCode());
        lqw.like(StringUtils.isNotBlank(bo.getProductName()), ProductActivity::getProductName, bo.getProductName());
        lqw.eq(StringUtils.isNotBlank(bo.getProductDate()), ProductActivity::getProductDate, bo.getProductDate());
        lqw.eq(StringUtils.isNotBlank(bo.getSelectStatus()), ProductActivity::getSelectStatus, bo.getSelectStatus());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            ProductActivity::getProductDate, params.get("beginCreateTime"), params.get("endCreateTime"));
        // 该段代码需放置最后
        if (StringUtils.isNotBlank(bo.getQueryType())) {
            if ("1".equals(bo.getQueryType())) {
                lqw.apply("(product_amount1 > product_amount  or product_amount2 > product_amount or product_amount3 > product_amount)");
            } else if ("2".equals(bo.getQueryType())) {
                lqw.apply("(product_amount1 < product_amount  and product_amount2 < product_amount and product_amount3 < product_amount)");
            }
        }
        return lqw;
    }

    /**
     * 新增产品活动
     *
     * @param bo 产品活动
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(ProductActivityBo bo) {
        ProductActivity add = MapstructUtils.convert(bo, ProductActivity.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改产品活动
     *
     * @param bo 产品活动
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(ProductActivityBo bo) {
        ProductActivity update = MapstructUtils.convert(bo, ProductActivity.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ProductActivity entity) {
        // 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除产品活动信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            // 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 新增推荐数据
     *
     * @param activityId   活动id
     * @param productLogVo 产品
     */
    @Override
    public void insertByProductLog(long activityId, ProductLogVo productLogVo) {
        ProductActivityBo productActivityBo = new ProductActivityBo();
        productActivityBo.setProductCode(productLogVo.getProductCode());
        productActivityBo.setProductName(productLogVo.getProductName());
        productActivityBo.setActivityId(activityId);
        productActivityBo.setProductDate(productLogVo.getInfoDate());
        productActivityBo.setProductAmount(productLogVo.getF2());
        ProductActivityVo productActivityVo = this.queryByProductCodeAndActivityId(productActivityBo);
        if (null == productActivityVo) {
            this.insertByBo(productActivityBo);
        }
    }
}
