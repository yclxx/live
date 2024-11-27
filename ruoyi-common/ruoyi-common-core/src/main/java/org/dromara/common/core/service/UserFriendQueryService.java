package org.dromara.common.core.service;

import org.dromara.common.core.domain.dto.UserFriendDTO;

import java.util.Collection;
import java.util.List;

/**
 * 用户好友查询服务
 *
 * @author xiexi
 * @description
 * @date 2024/11/27 21:42
 */
public interface UserFriendQueryService {

    /**
     * 根据用户id集合查询用户信息
     *
     * @param userIds 用户id集合
     * @return 用户信息
     */
    List<UserFriendDTO> queryListByUserIds(Collection<Long> userIds);
}
