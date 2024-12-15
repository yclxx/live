package org.dromara.common.core.service;

/**
 * @author xiexi
 * @description
 * @date 2024/12/15 20:34
 */
public interface AdminTokenService {

    /**
     * 校验token是否有效
     *
     * @param token token
     * @return true 有效、false无效
     */
    Boolean checkToken(String token);
}
