package org.dromara.mqtt.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.dromara.mqtt.domain.bo.UserFriendBo;
import org.dromara.mqtt.domain.vo.UserFriendVo;
import org.dromara.mqtt.domain.UserFriend;
import org.dromara.mqtt.mapper.UserFriendMapper;
import org.dromara.mqtt.service.IUserFriendService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 好友管理Service业务层处理
 *
 * @author xx
 * @date 2024-11-26
 */
@RequiredArgsConstructor
@Service
public class UserFriendServiceImpl implements IUserFriendService {

    private final UserFriendMapper baseMapper;

    /**
     * 查询好友管理
     *
     * @param id 主键
     * @return 好友管理
     */
    @Override
    public UserFriendVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询好友管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 好友管理分页列表
     */
    @Override
    public TableDataInfo<UserFriendVo> queryPageList(UserFriendBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<UserFriend> lqw = buildQueryWrapper(bo);
        Page<UserFriendVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的好友管理列表
     *
     * @param bo 查询条件
     * @return 好友管理列表
     */
    @Override
    public List<UserFriendVo> queryList(UserFriendBo bo) {
        LambdaQueryWrapper<UserFriend> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<UserFriend> buildQueryWrapper(UserFriendBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<UserFriend> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, UserFriend::getUserId, bo.getUserId());
        lqw.eq(bo.getFriendUserId() != null, UserFriend::getFriendUserId, bo.getFriendUserId());
        return lqw;
    }

    /**
     * 新增好友管理
     *
     * @param bo 好友管理
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(UserFriendBo bo) {
        UserFriend add = MapstructUtils.convert(bo, UserFriend.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改好友管理
     *
     * @param bo 好友管理
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(UserFriendBo bo) {
        UserFriend update = MapstructUtils.convert(bo, UserFriend.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(UserFriend entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除好友管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
