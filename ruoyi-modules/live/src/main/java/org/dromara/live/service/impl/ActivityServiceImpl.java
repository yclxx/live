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
import org.dromara.live.domain.bo.ActivityBo;
import org.dromara.live.domain.vo.ActivityVo;
import org.dromara.live.domain.Activity;
import org.dromara.live.mapper.ActivityMapper;
import org.dromara.live.service.IActivityService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 活动管理Service业务层处理
 *
 * @author xx
 * @date 2024-07-14
 */
@RequiredArgsConstructor
@Service
public class ActivityServiceImpl implements IActivityService {

    private final ActivityMapper baseMapper;

    /**
     * 查询活动管理
     *
     * @param activityId 主键
     * @return 活动管理
     */
    @Override
    public ActivityVo queryById(Long activityId) {
        return baseMapper.selectVoById(activityId);
    }

    /**
     * 分页查询活动管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 活动管理分页列表
     */
    @Override
    public TableDataInfo<ActivityVo> queryPageList(ActivityBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Activity> lqw = buildQueryWrapper(bo);
        Page<ActivityVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的活动管理列表
     *
     * @param bo 查询条件
     * @return 活动管理列表
     */
    @Override
    public List<ActivityVo> queryList(ActivityBo bo) {
        LambdaQueryWrapper<Activity> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Activity> buildQueryWrapper(ActivityBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Activity> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getActivityId() != null, Activity::getActivityId, bo.getActivityId());
        lqw.like(StringUtils.isNotBlank(bo.getActivityName()), Activity::getActivityName, bo.getActivityName());
        lqw.eq(StringUtils.isNotBlank(bo.getActivityRemark()), Activity::getActivityRemark, bo.getActivityRemark());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), Activity::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增活动管理
     *
     * @param bo 活动管理
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(ActivityBo bo) {
        Activity add = MapstructUtils.convert(bo, Activity.class);
        return baseMapper.insertOrUpdate(add);
    }

    /**
     * 修改活动管理
     *
     * @param bo 活动管理
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(ActivityBo bo) {
        Activity update = MapstructUtils.convert(bo, Activity.class);
        return baseMapper.insertOrUpdate(update);
    }


    /**
     * 校验并批量删除活动管理信息
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
}
