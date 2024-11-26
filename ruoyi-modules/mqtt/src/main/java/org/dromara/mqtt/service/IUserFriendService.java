package org.dromara.mqtt.service;

import org.dromara.mqtt.domain.vo.UserFriendVo;
import org.dromara.mqtt.domain.bo.UserFriendBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 好友管理Service接口
 *
 * @author xx
 * @date 2024-11-26
 */
public interface IUserFriendService {

    /**
     * 查询好友管理
     *
     * @param id 主键
     * @return 好友管理
     */
    UserFriendVo queryById(Long id);

    /**
     * 分页查询好友管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 好友管理分页列表
     */
    TableDataInfo<UserFriendVo> queryPageList(UserFriendBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的好友管理列表
     *
     * @param bo 查询条件
     * @return 好友管理列表
     */
    List<UserFriendVo> queryList(UserFriendBo bo);

    /**
     * 新增好友管理
     *
     * @param bo 好友管理
     * @return 是否新增成功
     */
    Boolean insertByBo(UserFriendBo bo);

    /**
     * 修改好友管理
     *
     * @param bo 好友管理
     * @return 是否修改成功
     */
    Boolean updateByBo(UserFriendBo bo);

    /**
     * 校验并批量删除好友管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
