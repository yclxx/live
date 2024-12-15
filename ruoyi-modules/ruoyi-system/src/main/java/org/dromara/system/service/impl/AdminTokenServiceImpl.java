package org.dromara.system.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.model.LoginUser;
import org.dromara.common.core.service.AdminTokenService;
import org.dromara.common.satoken.utils.LoginHelper;
import org.springframework.stereotype.Service;

/**
 * @author xiexi
 * @description
 * @date 2024/12/15 20:38
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AdminTokenServiceImpl implements AdminTokenService {
    @Override
    public Boolean checkToken(String token) {
        try {
            LoginUser loginUser = LoginHelper.getLoginUser(token);
            if (null != loginUser) {
                return true;
            }
        } catch (Exception e) {
            log.info("获取用户失败，", e);
        }

        return false;
    }
}
