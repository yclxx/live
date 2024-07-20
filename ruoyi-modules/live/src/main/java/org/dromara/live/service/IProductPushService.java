package org.dromara.live.service;

/**
 * 活动推荐
 *
 * @author 25487
 */
public interface IProductPushService {

    /**
     * 执行策略
     */
    void push();

    /**
     * 更新推荐产品价格
     */
    void updateActivityAmount();
}
