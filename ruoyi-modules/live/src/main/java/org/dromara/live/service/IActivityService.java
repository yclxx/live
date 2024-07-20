package org.dromara.live.service;

import org.dromara.live.domain.vo.ActivityVo;
import org.dromara.live.domain.bo.ActivityBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 活动管理Service接口
 *
 * @author xx
 * @date 2024-07-14
 */
public interface IActivityService {

    /**
     * 查询活动管理
     *
     * @param activityId 主键
     * @return 活动管理
     */
    ActivityVo queryById(Long activityId);

    /**
     * 分页查询活动管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 活动管理分页列表
     */
    TableDataInfo<ActivityVo> queryPageList(ActivityBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的活动管理列表
     *
     * @param bo 查询条件
     * @return 活动管理列表
     */
    List<ActivityVo> queryList(ActivityBo bo);

    /**
     * 新增活动管理
     *
     * @param bo 活动管理
     * @return 是否新增成功
     */
    Boolean insertByBo(ActivityBo bo);

    /**
     * 修改活动管理
     *
     * @param bo 活动管理
     * @return 是否修改成功
     */
    Boolean updateByBo(ActivityBo bo);

    /**
     * 校验并批量删除活动管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
