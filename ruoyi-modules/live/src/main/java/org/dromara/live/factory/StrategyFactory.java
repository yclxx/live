package org.dromara.live.factory;

import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.SpringUtils;
import org.dromara.live.service.HandleStrategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 策略Factory
 *
 * @author Lion Li
 */
@Slf4j
public class StrategyFactory {

    private static final Map<String, HandleStrategy> CLIENT_CACHE = new ConcurrentHashMap<>();
    private static final ReentrantLock LOCK = new ReentrantLock();

    /**
     * 根据类型获取实例
     */
    public static HandleStrategy instance(String className) {
        HandleStrategy strategy = CLIENT_CACHE.get(className);
        // 不存在
        if (strategy == null) {
            LOCK.lock();
            try {
                strategy = CLIENT_CACHE.get(className);
                if (strategy == null) {
                    CLIENT_CACHE.put(className, SpringUtils.getBean(className));
                    log.info("创建策略实例 className => {}", className);
                    return CLIENT_CACHE.get(className);
                }
            } finally {
                LOCK.unlock();
            }
        }
        return strategy;
    }

}
