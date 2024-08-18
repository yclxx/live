package org.dromara.live.service;

import java.util.concurrent.Future;

/**
 * 策略类
 *
 * @author 25487
 */
public interface HandleStrategy {

    /**
     * 推荐策略
     *
     * @param activityId 活动id
     * @param infoDate   需要推荐的日期
     */
    Future<String> handlePush(String tenantId, Long activityId, String infoDate);
}
