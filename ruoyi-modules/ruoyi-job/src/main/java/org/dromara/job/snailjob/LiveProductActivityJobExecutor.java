package org.dromara.job.snailjob;

import com.aizuda.snailjob.client.job.core.annotation.JobExecutor;
import com.aizuda.snailjob.client.model.ExecuteResult;
import lombok.RequiredArgsConstructor;
import org.dromara.live.service.IProductPushService;
import org.springframework.stereotype.Component;

/**
 * 注解定时任务测试 @JobExecutor注解模式: 支持无参和JobArgs参数，同时可作用于类和方法上，作用与类上时可以指定方法名称
 *
 * @author opensnail
 * @date 2024-05-17
 */
@RequiredArgsConstructor
@Component
public class LiveProductActivityJobExecutor {

    private final IProductPushService productPushService;

    @JobExecutor(name = "productActivityAddJobExecutor")
    public ExecuteResult productActivityAddJobExecute() {
        // 执行策略推荐产品
        productPushService.push();
        return ExecuteResult.success("执行成功");
    }

    @JobExecutor(name = "productActivityUpdateJobExecutor")
    public ExecuteResult productActivityUpdateJobExecute() {
        // 更新价格
        productPushService.updateActivityAmount();
        return ExecuteResult.success("执行成功");
    }
}
