package org.dromara.job.snailjob;

import com.aizuda.snailjob.client.job.core.dto.JobArgs;
import com.aizuda.snailjob.client.job.core.executor.AbstractJobExecutor;
import com.aizuda.snailjob.client.model.ExecuteResult;
import com.aizuda.snailjob.common.core.util.JsonUtil;
import com.aizuda.snailjob.common.log.SnailJobLog;
import org.springframework.stereotype.Component;

/**
 * 继承AbstractJobExecutor模式: 使用类的全路径
 * @author opensnail
 * @date 2024-05-17
 */
@Component
public class TestClassJobExecutor extends AbstractJobExecutor {

    @Override
    protected ExecuteResult doJobExecute(JobArgs jobArgs) {
        SnailJobLog.LOCAL.info("TestClassJobExecutor. jobArgs:{}", JsonUtil.toJsonString(jobArgs));
        return ExecuteResult.success("TestJobExecutor测试成功");
    }
}
