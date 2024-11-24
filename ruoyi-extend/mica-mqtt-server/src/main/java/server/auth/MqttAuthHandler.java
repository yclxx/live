package server.auth;

import net.dreamlu.iot.mqtt.core.server.auth.IMqttServerAuthHandler;
import org.dromara.common.core.utils.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.tio.core.ChannelContext;

/**
 * 示例 mqtt tcp、websocket 认证，请按照自己的需求和业务进行扩展
 *
 * @author L.cm
 */
@Configuration(proxyBeanMethods = false)
public class MqttAuthHandler implements IMqttServerAuthHandler {

    @Override
    public boolean authenticate(ChannelContext context, String uniqueId, String clientId, String userName, String password) {
        // 客户端认证逻辑实现
        logger.info("uniqueId：{}，clientId：{}，userName：{}，password：{}", uniqueId, clientId, userName, password);
        if (StringUtils.isNotBlank(password) && "123456".equals(password)) {
            return false;
        }
        return true;
    }

}
