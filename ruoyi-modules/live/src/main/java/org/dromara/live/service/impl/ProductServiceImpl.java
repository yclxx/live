package org.dromara.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.live.domain.Product;
import org.dromara.live.domain.bo.ProductBo;
import org.dromara.live.domain.vo.ProductVo;
import org.dromara.live.mapper.ProductMapper;
import org.dromara.live.service.IProductService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 产品管理Service业务层处理
 *
 * @author xx
 * @date 2024-05-28
 */
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements IProductService {

    private final ProductMapper baseMapper;

    /**
     * 查询产品管理
     *
     * @param productCode 主键
     * @return 产品管理
     */
    @Override
    public ProductVo queryById(String productCode) {
        return baseMapper.selectVoById(productCode);
    }

    /**
     * 分页查询产品管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 产品管理分页列表
     */
    @Override
    public TableDataInfo<ProductVo> queryPageList(ProductBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Product> lqw = buildQueryWrapper(bo);
        Page<ProductVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的产品管理列表
     *
     * @param bo 查询条件
     * @return 产品管理列表
     */
    @Override
    public List<ProductVo> queryList(ProductBo bo) {
        LambdaQueryWrapper<Product> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Product> buildQueryWrapper(ProductBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Product> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getProductCode()), Product::getProductCode, bo.getProductCode());
        lqw.like(StringUtils.isNotBlank(bo.getProductName()), Product::getProductName, bo.getProductName());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), Product::getStatus, bo.getStatus());
        lqw.in(StringUtils.isNotBlank(bo.getProductType()), Product::getProductType, StringUtils.splitList(bo.getProductType()));
        lqw.gt(null != params.get("minF116"), Product::getF116, params.get("minF116"));
        lqw.lt(null != params.get("maxF116"), Product::getF116, params.get("maxF116"));
        return lqw;
    }

    /**
     * 新增产品管理
     *
     * @param productList 产品管理
     */
    @Override
    public void insertByBo(List<Product> productList) {
        baseMapper.insertOrUpdateBatch(productList);
    }

    /**
     * 修改产品管理
     *
     * @param bo 产品管理
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(ProductBo bo) {
        Product update = MapstructUtils.convert(bo, Product.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Product entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除产品管理信息
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
     * 获取最靠前的排序值
     *
     * @return 排序值
     */
    @Override
    public Long getMaxSort() {
        LambdaQueryWrapper<Product> lqw = Wrappers.lambdaQuery();
        lqw.last("order by sort desc limit 1");
        ProductVo productVo = baseMapper.selectVoOne(lqw);
        if (null == productVo) {
            return 0L;
        }
        return productVo.getSort();
    }

    /**
     * 查询产品记录遗漏的数据
     *
     * @return 产品记录
     */
    @Override
    public List<ProductVo> querySupplementList(String notInSql) {
        LambdaQueryWrapper<Product> lqw = Wrappers.lambdaQuery();
        lqw.in(Product::getProductType, "0", "1");
        lqw.eq(Product::getStatus, "0");
        lqw.notInSql(Product::getProductCode, notInSql);
        return baseMapper.selectVoList(lqw);
    }
}
