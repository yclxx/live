package server.service;

import lombok.RequiredArgsConstructor;
import net.dreamlu.iot.mqtt.spring.server.MqttServerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * @author wsq
 */
@RequiredArgsConstructor
@Service
public class ServerService {
    private static final Logger logger = LoggerFactory.getLogger(ServerService.class);
    private final MqttServerTemplate server;

    public boolean publish(String body) {
        boolean result = server.publishAll("/test/123", body.getBytes(StandardCharsets.UTF_8));
        logger.info("Mqtt publishAll result:{}", result);
        return result;
    }
}
