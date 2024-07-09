package org.dromara.live.service;

/**
 * 20001推荐
 *
 * @author 25487
 */
public interface IProductPushService {

    /**
     * 策略10000， 规则找出30天内涨停过的票，判断今天的价格是否在20日均线附近，且今天的20日均线大于涨停当天的20日均线
     *
     * @param infoDate 需要校验的日期
     */
    void push10000(String infoDate);
}
