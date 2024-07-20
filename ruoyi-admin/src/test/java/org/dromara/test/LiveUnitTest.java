package org.dromara.test;

import lombok.extern.slf4j.Slf4j;
import org.dromara.live.domain.vo.ActivityVo;
import org.dromara.live.factory.StrategyFactory;
import org.dromara.live.service.HandleStrategy;
import org.dromara.live.service.IActivityService;
import org.dromara.live.service.IProductLogService;
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
    private IActivityService activityService;

    @Test
    public void testTest() {
        ActivityVo activityVo = activityService.queryById(10002L);
//        // 获取策略类
//        HandleStrategy instance = StrategyFactory.instance(activityVo.getClassName());
//        // 执行策略
//        instance.handlePush(activityVo.getActivityId(), "2024-07-14");

        List<String> infoDate = productLogService.queryInfoDate();
        for (String date : infoDate) {
            try {
                // 获取策略类
                HandleStrategy instance = StrategyFactory.instance(activityVo.getClassName());
                // 执行策略
                instance.handlePush(activityVo.getActivityId(), date);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        log.info("执行完成");
    }
}
