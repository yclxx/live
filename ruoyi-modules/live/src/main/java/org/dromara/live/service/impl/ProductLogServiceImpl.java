package org.dromara.live.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.live.domain.ProductLog;
import org.dromara.live.domain.bo.ProductLogBo;
import org.dromara.live.domain.vo.ProductLogVo;
import org.dromara.live.mapper.ProductLogMapper;
import org.dromara.live.service.IProductLogService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 产品记录Service业务层处理
 *
 * @author xx
 * @date 2024-06-02
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ProductLogServiceImpl implements IProductLogService {

    private final ProductLogMapper baseMapper;

    /**
     * 查询产品记录
     *
     * @param id 主键
     * @return 产品记录
     */
    @Override
    public ProductLogVo queryById(String id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询产品记录
     *
     * @param productCode 产品代码
     * @return 产品记录
     */
    @Override
    public ProductLogVo queryLastByProductCode(String productCode) {
        LambdaQueryWrapper<ProductLog> lqw = Wrappers.lambdaQuery();
        lqw.eq(ProductLog::getProductCode, productCode);
        lqw.last("order by info_date desc limit 1");
        return baseMapper.selectVoOne(lqw);
    }

    /**
     * 查询产品记录
     *
     * @param productCode 产品代码
     * @return 产品记录
     */
    @Override
    public ProductLogVo queryByProductCodeAndInfoDate(String productCode, String infoDate) {
        LambdaQueryWrapper<ProductLog> lqw = Wrappers.lambdaQuery();
        lqw.eq(ProductLog::getProductCode, productCode);
        lqw.eq(ProductLog::getInfoDate, infoDate);
        lqw.last("limit 1");
        return baseMapper.selectVoOne(lqw);
    }

    /**
     * 分页查询产品记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 产品记录分页列表
     */
    @Override
    public TableDataInfo<ProductLogVo> queryPageList(ProductLogBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ProductLog> lqw = buildQueryWrapper(bo);
        Page<ProductLogVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的产品记录列表
     *
     * @param bo 查询条件
     * @return 产品记录列表
     */
    @Override
    public List<ProductLogVo> queryList(ProductLogBo bo) {
        LambdaQueryWrapper<ProductLog> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ProductLog> buildQueryWrapper(ProductLogBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ProductLog> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getProductCode()), ProductLog::getProductCode, bo.getProductCode());
        lqw.like(StringUtils.isNotBlank(bo.getProductName()), ProductLog::getProductName, bo.getProductName());
        lqw.eq(StringUtils.isNotBlank(bo.getInfoDate()), ProductLog::getInfoDate, bo.getInfoDate());
        return lqw;
    }

    /**
     * 新增产品记录
     *
     * @param bo 产品记录
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(ProductLogBo bo) {
        ProductLog add = MapstructUtils.convert(bo, ProductLog.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 新增产品记录
     *
     * @param bo 产品记录
     * @return 是否新增成功
     */
    @Override
    public void insertBatch(List<ProductLog> bo) {
        baseMapper.insertBatch(bo);
    }

    /**
     * 修改产品记录
     *
     * @param bo 产品记录
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(ProductLogBo bo) {
        ProductLog update = MapstructUtils.convert(bo, ProductLog.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ProductLog entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除产品记录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 校验并批量删除产品记录信息
     *
     * @param productCode 产品代码
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteByProductCode(String productCode) {
        return baseMapper.delete(Wrappers.<ProductLog>lambdaQuery().eq(ProductLog::getProductCode, productCode)) > 0;
    }

    /**
     * 校验并批量删除产品记录信息
     *
     * @param productCode 产品代码
     */
    @Override
    public void deleteByProductCodeAndInfoDateLessThan(String productCode, String infoDate) {
        baseMapper.delete(Wrappers.<ProductLog>lambdaQuery().eq(ProductLog::getProductCode, productCode).lt(ProductLog::getInfoDate, infoDate));
    }

    @Override
    public List<String> queryInfoDate() {
        LambdaQueryWrapper<ProductLog> infoDateLqw = Wrappers.lambdaQuery();
        infoDateLqw.select(ProductLog::getInfoDate);
        infoDateLqw.groupBy(ProductLog::getInfoDate);
        return baseMapper.selectObjs(infoDateLqw);
    }

    /**
     * 获取最新日期
     *
     * @return 开盘日期
     */
    @Override
    public String queryLastInfoDate() {
        LambdaQueryWrapper<ProductLog> infoDateLqw = Wrappers.lambdaQuery();
        infoDateLqw.select(ProductLog::getInfoDate);
        infoDateLqw.groupBy(ProductLog::getInfoDate);
        infoDateLqw.last("order by info_date desc limit 1");
        ProductLogVo productLogVo = baseMapper.selectVoOne(infoDateLqw);
        if (null == productLogVo) {
            return null;
        }
        return productLogVo.getInfoDate();
    }

    @Override
    public String queryNextInfoDate(String infoDate) {
        LambdaQueryWrapper<ProductLog> infoDateLqw = Wrappers.lambdaQuery();
        infoDateLqw.gt(ProductLog::getInfoDate, infoDate);
        infoDateLqw.last("order by info_date asc limit 1");
        ProductLogVo productLogVo = baseMapper.selectVoOne(infoDateLqw);
        if (null == productLogVo) {
            return null;
        }
        return productLogVo.getInfoDate();
    }

    @Override
    public String queryFirstInfoDate(String infoDate) {
        LambdaQueryWrapper<ProductLog> infoDateLqw = Wrappers.lambdaQuery();
        infoDateLqw.lt(ProductLog::getInfoDate, infoDate);
        infoDateLqw.last("order by info_date desc limit 1");
        ProductLogVo productLogVo = baseMapper.selectVoOne(infoDateLqw);
        if (null == productLogVo) {
            return null;
        }
        return productLogVo.getInfoDate();
    }

    @Override
    public List<String> queryAnalyse(String queryInfoDate, List<String> productCodeList) {
        LambdaQueryWrapper<ProductLog> queryLqw = Wrappers.lambdaQuery();
        queryLqw.select(ProductLog::getProductCode);
        queryLqw.eq(ProductLog::getInfoDate, queryInfoDate);
        queryLqw.gt(ProductLog::getF6, 100000000);
        queryLqw.gt(ProductLog::getF3, 0);
        queryLqw.lt(ProductLog::getF3, 2);
        queryLqw.in(ObjectUtil.isNotEmpty(productCodeList), ProductLog::getProductCode, productCodeList);
        return baseMapper.selectObjs(queryLqw);
    }

    @Override
    public List<String> queryAnalyseVerify(String queryInfoDate, List<String> productCodeList) {
        String s = queryNextInfoDate(queryInfoDate);
        if (StringUtils.isBlank(s)) {
            return null;
        }
        LambdaQueryWrapper<ProductLog> queryLqw = Wrappers.lambdaQuery();
        queryLqw.select(ProductLog::getProductCode);
        queryLqw.eq(ProductLog::getInfoDate, s);
        queryLqw.gt(ProductLog::getF3, 0);
        queryLqw.in(ObjectUtil.isNotEmpty(productCodeList), ProductLog::getProductCode, productCodeList);
        return baseMapper.selectObjs(queryLqw);
    }

    @Override
    public List<ProductLogVo> queryListByProductCodeList(String queryInfoDate, List<String> productCodeList) {
        if (StringUtils.isBlank(queryInfoDate) || ObjectUtil.isEmpty(productCodeList)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<ProductLog> queryLqw = Wrappers.lambdaQuery();
        queryLqw.eq(ProductLog::getInfoDate, queryInfoDate);
        queryLqw.in(ObjectUtil.isNotEmpty(productCodeList), ProductLog::getProductCode, productCodeList);
        return baseMapper.selectVoList(queryLqw);
    }

    @Override
    public List<ProductLogVo> queryBy20001(String infoDate) {
        LambdaQueryWrapper<ProductLog> queryLqw = Wrappers.lambdaQuery();
        queryLqw.eq(ProductLog::getInfoDate, infoDate);
        queryLqw.gt(ProductLog::getMa20, 0);
        queryLqw.lt(ProductLog::getF3, 1);
        queryLqw.gt(ProductLog::getF3, -2);
        queryLqw.apply("f6 >= 100000000 and (abs(f2-ma20) / f2) <= 0.005");

        return baseMapper.selectVoList(queryLqw);
    }

    @Override
    public List<ProductLogVo> queryBy20001AfterList(String infoDate, String productCode, int afterListCount) {
        LambdaQueryWrapper<ProductLog> queryLqw = Wrappers.lambdaQuery();
        queryLqw.lt(ProductLog::getInfoDate, infoDate);
        queryLqw.eq(ProductLog::getProductCode, productCode);
        queryLqw.last("order by info_date desc limit " + afterListCount);

        return baseMapper.selectVoList(queryLqw);
    }

    @Override
    public List<ProductLogVo> queryBy20002(String infoDate) {
        String nextInfoDate = queryFirstInfoDate(infoDate);
        if (StringUtils.isBlank(nextInfoDate)) {
            return new ArrayList<>();
        }
        return baseMapper.queryBy20002(infoDate, nextInfoDate);
    }
}
