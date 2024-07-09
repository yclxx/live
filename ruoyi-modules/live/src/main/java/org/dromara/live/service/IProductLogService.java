package org.dromara.live.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.live.domain.ProductLog;
import org.dromara.live.domain.bo.ProductLogBo;
import org.dromara.live.domain.vo.ProductLogVo;

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
     * @return 是否新增成功
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
     * @return 是否删除成功
     */
    Boolean deleteByProductCode(String productCode);

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

    /**
     * 统计数据
     *
     * @param queryInfoDate   统计日期
     * @param productCodeList 需要统计的产品代码
     * @return 结果
     */
    List<ProductLogVo> queryListByProductCodeList(String queryInfoDate, List<String> productCodeList);

    String queryNextInfoDate(String infoDate);

    String queryFirstInfoDate(String infoDate);

    List<ProductLogVo> queryBy20001(String infoDate);

    List<ProductLogVo> queryBy20001AfterList(String infoDate, String productCode, int afterListCount);

    List<ProductLogVo> queryBy20002(String infoDate);

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
    ProductLogVo checkActivity10000(Date date, String productCode,String productName);
}
