package org.dromara.test;

import lombok.extern.slf4j.Slf4j;
import org.dromara.live.service.IProductLogService;
import org.dromara.live.service.IProductPushService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 单元测试案例
 *
 * @author Lion Li
 */
@Slf4j
@SpringBootTest // 此注解只能在 springboot 主包下使用 需包含 main 方法与 yml 配置文件
@DisplayName("单元测试案例")
public class LiveUnitTest {

    @Autowired
    private IProductLogService productLogService;
    @Autowired
    private IProductPushService productPushService;

    @Test
    public void testTest() {
        List<String> infoDate = productLogService.queryInfoDate();
        for (String date : infoDate) {
            productPushService.push10000(date);
        }
        log.info("执行完成");
    }
}
