package org.dromara.live.service;

/**
 * @author xiexi
 * @description
 * @date 2024/7/13 10:22
 */
public interface IProductLogAddService {

    /**
     * 新增产品记录
     */
    void add();

    /**
     * 补充遗漏的产品记录
     */
    void productLogSupplement();
}
