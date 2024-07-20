package org.dromara.live.service;

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
    void handlePush(Long activityId, String infoDate);
}
