package server.auth;

import net.dreamlu.iot.mqtt.core.server.auth.IMqttServerAuthHandler;
import org.dromara.common.core.service.AdminTokenService;
import org.dromara.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.tio.core.ChannelContext;

/**
 * 示例 mqtt tcp、websocket 认证，请按照自己的需求和业务进行扩展
 *
 * @author L.cm
 */
@Configuration(proxyBeanMethods = false)
public class MqttAuthHandler implements IMqttServerAuthHandler {

    @Autowired
    private AdminTokenService adminTokenService;

    @Override
    public boolean authenticate(ChannelContext context, String uniqueId, String clientId, String userName, String password) {
        // 客户端认证逻辑实现
        logger.info("uniqueId：{}，clientId：{}，userName：{}，password：{}", uniqueId, clientId, userName, password);
        if (!"token".equals(userName)) {
            logger.error("不支持非token验证方式，uniqueId：{}，clientId：{}", uniqueId, clientId);
            return false;
        }
        if (StringUtils.isBlank(password)) {
            logger.error("token不能为空，uniqueId：{}，clientId：{}", uniqueId, clientId);
            return false;
        }
        // 校验token是否有效
        try {
            Boolean checkToken = adminTokenService.checkToken(password);
            if (checkToken) {
                logger.info("token有效");
            } else {
                logger.error("token无效，password：{}", password);
            }
        } catch (Exception e) {
            logger.error("鉴权失败", e);
        }
        if (StringUtils.isNotBlank(password) && "123456".equals(password)) {
            return false;
        }
        return true;
    }

}
