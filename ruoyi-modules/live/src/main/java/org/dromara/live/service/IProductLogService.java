package org.dromara.live.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.live.domain.ProductLog;
import org.dromara.live.domain.bo.ProductLogBo;
import org.dromara.live.domain.vo.ProductLogVo;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 产品记录Service接口
 *
 * @author xx
 * @date 2024-06-02
 */
public interface IProductLogService {

    /**
     * 查询产品记录
     *
     * @param id 主键
     * @return 产品记录
     */
    ProductLogVo queryById(String id);

    /**
     * 查询产品记录
     *
     * @param productCode 产品代码
     * @return 产品记录
     */
    ProductLogVo queryLastByProductCode(String productCode);

    /**
     * 查询产品记录
     *
     * @param productCode 产品代码
     * @param infoDate    日期
     * @return 产品记录
     */
    ProductLogVo queryByProductCodeAndInfoDate(String productCode, String infoDate);

    /**
     * 分页查询产品记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 产品记录分页列表
     */
    TableDataInfo<ProductLogVo> queryPageList(ProductLogBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的产品记录列表
     *
     * @param bo 查询条件
     * @return 产品记录列表
     */
    List<ProductLogVo> queryList(ProductLogBo bo);

    /**
     * 新增产品记录
     *
     * @param bo 产品记录
     * @return 是否新增成功
     */
    Boolean insertByBo(ProductLogBo bo);

    /**
     * 新增产品记录
     *
     * @param bo 产品记录
     */
    void insertBatch(List<ProductLog> bo);

    /**
     * 修改产品记录
     *
     * @param bo 产品记录
     * @return 是否修改成功
     */
    Boolean updateByBo(ProductLogBo bo);

    /**
     * 校验并批量删除产品记录信息
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
     */
    void deleteByProductCode(String productCode);

    /**
     * 校验并批量删除产品记录信息
     *
     * @param productCode 产品代码
     */
    void deleteByProductCodeAndInfoDateLessThan(String productCode, String infoDate);

    /**
     * 获取开盘日期
     *
     * @return 开盘日期
     */
    List<String> queryInfoDate();

    /**
     * 获取最新日期
     *
     * @return 开盘日期
     */
    String queryLastInfoDate();

    /**
     * 统计数据
     *
     * @param queryInfoDate   统计日期
     * @param productCodeList 需要统计的产品代码
     * @return 结果
     */
    List<String> queryAnalyse(String queryInfoDate, List<String> productCodeList);

    /**
     * 统计数据
     *
     * @param queryInfoDate   统计日期
     * @param productCodeList 需要统计的产品代码
     * @return 结果
     */
    List<String> queryAnalyseVerify(String queryInfoDate, List<String> productCodeList);

    String queryNextInfoDate(String infoDate);

    /**
     * 获取前一天交易日期
     *
     * @param infoDate 需要查询的日期
     * @return 前一天交易日期
     */
    String queryFirstInfoDate(String infoDate);

    /**
     * 查询30天前涨停过的票
     *
     * @param date 需要查询的日期，系统会自动计算前30天的数据, 为null时，默认为当前日期
     * @return 符合条件的票
     */
    List<ProductLogVo> queryActivity10000(Date date);

    /**
     * 校验指定日期的数据是否在20日均线正负1.5%
     *
     * @param date 指定的日期，为null时，默认为当前日期
     * @return true 符合条件，false 不符合条件
     */
    ProductLogVo checkActivity10000(Date date, String productCode, String productName);

    /**
     * 校验指定日期的数据是否在20日均线正负1%
     *
     * @param date        指定的日期，为null时，默认为当前日期
     * @param productCode 产品编号
     * @param productName 产品名称
     * @return true 符合条件，false 不符合条件
     */
    ProductLogVo checkMa20(Date date, String productCode, String productName);

    /**
     * 查询指定日期前指定天数的最高价最低价浮动比例
     *
     * @return 浮动比例
     */
    BigDecimal queryFloatByDays(String productCode, String infoDate, int days);

    /**
     * 查询指定日期前指定天数内的涨跌幅总和
     *
     * @param productCode 产品编号
     * @param infoDate    日期
     * @param days        指定天数
     * @return 涨跌幅总和
     */
    BigDecimal sumDaysF3(String productCode, String infoDate, int days);

    /**
     * 查询指定日期 最低价在5日均线正负1%附近
     *
     * @param infoDate        需要查询的日期
     * @param productCodeList 从指定的产品代码中查询，为空查询全部
     * @return 产品代码
     */
    List<String> queryProductCodeByInfoDateAndF16(String infoDate, List<String> productCodeList);

    /**
     * 查询指定天数前的交易数据
     *
     * @param productCode 产品编号
     * @param infoDate    指定日期
     * @param days        指定天数
     * @return 交易数据
     */
    List<ProductLogVo> queryBeforeInfoDate(String productCode, String infoDate, int days);

    /**
     * 查询当天涨幅低于5%的票
     *
     * @param infoDate 指定日期
     * @return 交易数据
     */
    List<ProductLogVo> queryListByInfoDate(String infoDate);
}
