package org.dromara.live.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
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
import org.dromara.live.domain.vo.GpInfoVo;
import org.dromara.live.domain.vo.ProductLogVo;
import org.dromara.live.mapper.ProductLogMapper;
import org.dromara.live.service.IProductLogService;
import org.dromara.live.utils.LiveUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        lqw.last("order by info_date desc limit 1");
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
        return baseMapper.updateById(update) > 0;
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
     */
    @Override
    public void deleteByProductCode(String productCode) {
        baseMapper.delete(Wrappers.<ProductLog>lambdaQuery().eq(ProductLog::getProductCode, productCode));
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

    /**
     * 查询30天前涨停过的票
     *
     * @param date 需要查询的日期，系统会自动计算前30天的数据, 为null时，默认为当前日期
     * @return 符合条件的票
     */
    @Override
    public List<ProductLogVo> queryActivity10000(Date date) {
        if (null == date) {
            date = new Date();
        }
        // 查询截止时间
        String maxDate = DateUtil.format(date, DatePattern.NORM_DATE_PATTERN);
        // 前30天
        DateTime dateTime = DateUtil.offsetDay(date, -30);
        String infoDate = DateUtil.format(dateTime, DatePattern.NORM_DATE_PATTERN);
        LambdaQueryWrapper<ProductLog> queryLqw = Wrappers.lambdaQuery();
        queryLqw.apply("f2 = f15");
        queryLqw.gt(ProductLog::getF3, 9.5);
        queryLqw.gt(ProductLog::getMa20, 0);
        queryLqw.gt(ProductLog::getInfoDate, infoDate);
        queryLqw.le(ProductLog::getInfoDate, maxDate);

        return baseMapper.selectVoList(queryLqw);
    }

    /**
     * 校验指定日期的数据是否在20日均线正负1.5%
     *
     * @param date 指定的日期，为null时，默认为当前日期
     * @return true 符合条件，false 不符合条件
     */
    @Override
    public ProductLogVo checkActivity10000(Date date, String productCode, String productName) {
        if (null == date) {
            date = new Date();
        }
        String infoDate = DateUtil.format(date, DatePattern.NORM_DATE_PATTERN);
        // 判断是否是今天，如果是今天，则请求接口查询实时数据
        if (infoDate.equals(DateUtil.today())) {
            List<GpInfoVo> gpInfoVoList = LiveUtils.getGpInfoVoList(productCode, productName);
            if (null == gpInfoVoList || gpInfoVoList.isEmpty()) {
                return null;
            }
            GpInfoVo last = gpInfoVoList.getLast();
            if (null == last.getMa20() || LiveUtils.checkMa20(last, new BigDecimal("0.015"))) {
                return null;
            }
            if (last.getMa10().compareTo(last.getMa20()) < 0) {
                return null;
            }
            if (last.getMa10().compareTo(last.getMa5()) < 0) {
                return null;
            }
            return MapstructUtils.convert(last, ProductLogVo.class);
        } else {
            // 不是今天，查询数据库数据
            LambdaQueryWrapper<ProductLog> queryLqw = Wrappers.lambdaQuery();
            queryLqw.eq(ProductLog::getProductCode, productCode);
            queryLqw.eq(ProductLog::getInfoDate, infoDate);
            queryLqw.apply("ma10 > ma20");
            queryLqw.apply("ma10 > ma5");
            queryLqw.apply("abs((f2 - ma20) / ma20) < 0.015");
            return baseMapper.selectVoOne(queryLqw);
        }
    }

    /**
     * 校验指定日期的数据是否在20日均线正负1%
     *
     * @param date 指定的日期，为null时，默认为当前日期
     * @return true 符合条件，false 不符合条件
     */
    @Override
    public ProductLogVo checkMa20(Date date, String productCode, String productName) {
        BigDecimal ma20Ratio = new BigDecimal("0.01");
        if (null == date) {
            date = new Date();
        }
        String infoDate = DateUtil.format(date, DatePattern.NORM_DATE_PATTERN);
        // 判断是否是今天，如果是今天，则请求接口查询实时数据
        if (infoDate.equals(DateUtil.today())) {
            List<GpInfoVo> gpInfoVoList = LiveUtils.getGpInfoVoList(productCode, productName);
            if (null == gpInfoVoList || gpInfoVoList.isEmpty()) {
                return null;
            }
            GpInfoVo last = gpInfoVoList.getLast();
            if (null == last.getMa20() || LiveUtils.checkMa20(last, ma20Ratio)) {
                return null;
            }
            return MapstructUtils.convert(last, ProductLogVo.class);
        } else {
            // 不是今天，查询数据库数据
            LambdaQueryWrapper<ProductLog> queryLqw = Wrappers.lambdaQuery();
            queryLqw.eq(ProductLog::getProductCode, productCode);
            queryLqw.eq(ProductLog::getInfoDate, infoDate);
            queryLqw.apply("abs((f2 - ma20) / ma20) < " + ma20Ratio);
            return baseMapper.selectVoOne(queryLqw);
        }
    }

    /**
     * 查询指定日期前指定天数的最高价最低价浮动比例
     *
     * @return 浮动比例
     */
    @Override
    public BigDecimal queryFloatByDays(String productCode, String infoDate, int days) {
        List<BigDecimal> f2List = baseMapper.selectObjs(Wrappers.<ProductLog>lambdaQuery().select(ProductLog::getF2).eq(ProductLog::getProductCode, productCode).lt(ProductLog::getInfoDate, infoDate).last("order by info_date desc limit " + days));
        if (f2List.size() != days) {
            return new BigDecimal("0");
        }
        // 排序 从小到大
        f2List.sort(Comparator.naturalOrder());
        // 最大值减去最小值 除以最大值
        BigDecimal subtract = f2List.getLast().subtract(f2List.getFirst());
        return subtract.divide(f2List.getLast(), 4, RoundingMode.HALF_UP);
    }

    /**
     * 查询指定日期前指定天数内的涨跌幅总和
     *
     * @param productCode 产品编号
     * @param infoDate    日期
     * @param days        指定天数
     * @return 涨跌幅总和
     */
    @Override
    public BigDecimal sumDaysF3(String productCode, String infoDate, int days) {
        List<BigDecimal> f3List = baseMapper.selectObjs(Wrappers.<ProductLog>lambdaQuery().select(ProductLog::getF3).eq(ProductLog::getProductCode, productCode).lt(ProductLog::getInfoDate, infoDate).last("order by info_date desc limit " + days));
        if (f3List.size() != days) {
            return new BigDecimal("0");
        }
        // 求和
        return f3List.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 查询指定日期 最低价在5日均线正负1%附近
     *
     * @param infoDate        需要查询的日期
     * @param productCodeList 从指定的产品代码中查询，为空查询全部
     * @return 产品代码
     */
    @Override
    public List<String> queryProductCodeByInfoDateAndF16(String infoDate, List<String> productCodeList) {
        LambdaQueryWrapper<ProductLog> lqw = Wrappers.lambdaQuery();
        lqw.select(ProductLog::getProductCode);
        lqw.eq(ProductLog::getInfoDate, infoDate);
        lqw.gt(ProductLog::getF6, new BigDecimal("100000000"));
        lqw.in(ObjectUtil.isNotEmpty(productCodeList), ProductLog::getProductCode, productCodeList);
        lqw.apply("abs((f16 - ma5) / ma5) < 0.01");
        return baseMapper.selectObjs(lqw);
    }
}
