package org.dromara.live.service.strategy;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.common.tenant.helper.TenantHelper;
import org.dromara.live.domain.bo.ActivityBo;
import org.dromara.live.domain.bo.ProductActivityBo;
import org.dromara.live.domain.vo.ActivityVo;
import org.dromara.live.domain.vo.GpInfoVo;
import org.dromara.live.domain.vo.ProductActivityVo;
import org.dromara.live.domain.vo.ProductLogVo;
import org.dromara.live.service.HandleStrategy;
import org.dromara.live.service.IActivityService;
import org.dromara.live.service.IProductActivityService;
import org.dromara.live.service.IProductLogService;
import org.dromara.live.utils.LiveUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * 美好生活，幸运每一天
 *
 * @author xiexi
 * @description
 * @date 2024/7/14 17:05
 */
@Slf4j
@Service("Activity99999Strategy")
@RequiredArgsConstructor
public class Activity99999Strategy implements HandleStrategy {

    private final IProductLogService productLogService;
    private final IProductActivityService productActivityService;
    private final IActivityService activityService;

    @Async
    @Override
    public Future<String> handlePush(String tenantId, Long activityId, String queryDate) {
        TenantHelper.dynamic(tenantId, () -> {
            this.handle(activityId);
        });

        log.info("策略{}执行完成", activityId);
        return CompletableFuture.completedFuture("执行完成");
    }

    private void handle(Long activityId) {
        ActivityBo activityBo = new ActivityBo();
        activityBo.setStatus("0");
        List<ActivityVo> activityVos = activityService.queryList(activityBo);
        int activityIndex = RandomUtil.randomInt(activityVos.size());
        ActivityVo activityVo = activityVos.get(activityIndex);
        if (null == activityVo) {
            return;
        }
        Long randomActivityId = activityVo.getActivityId();
        // 取当天
        String infoDate = DateUtil.today();
        String luckyKey = this.getLuckyKey();
        // 默认-1 代表今日已执行
        RedisUtils.setCacheObject(luckyKey, -1L, Duration.ofDays(10));
        // 查询前一个交易日
        String firstInfoDate = productLogService.queryFirstInfoDate(infoDate);
        // 查询前一个交易日推荐数据
        ProductActivityBo productActivityBo = new ProductActivityBo();
        productActivityBo.setProductDate(firstInfoDate);
        productActivityBo.setActivityId(randomActivityId);
        List<ProductActivityVo> productActivityVos = productActivityService.queryList(productActivityBo);
        if (ObjectUtil.isEmpty(productActivityVos)) {
            return;
        }
        // 随机选中数据
        int i = RandomUtil.randomInt(productActivityVos.size());
        ProductActivityVo productActivityVo = productActivityVos.get(i);
        // 查询最新价格
        List<GpInfoVo> gpInfoVoList = LiveUtils.getGpInfoVoList(productActivityVo.getProductCode(), productActivityVo.getProductName());
        if (null == gpInfoVoList || gpInfoVoList.isEmpty()) {
            return;
        }
        GpInfoVo last = gpInfoVoList.getLast();
        ProductLogVo convert = MapstructUtils.convert(last, ProductLogVo.class);
        if (null == convert) {
            return;
        }
        String redisKey = "lucky:" + convert.getProductCode() + ":" + convert.getInfoDate();
        String cacheObject = RedisUtils.getCacheObject(redisKey);
        if (StringUtils.isNotBlank(cacheObject)) {
            return;
        }
        RedisUtils.setCacheObject(redisKey, convert.getProductCode(), Duration.ofDays(10));
        // 新增数据
        productActivityBo = new ProductActivityBo();
        productActivityBo.setActivityId(activityId);
        productActivityBo.setProductCode(convert.getProductCode());
        productActivityBo.setProductName(convert.getProductName());
        productActivityBo.setProductDate(infoDate);
        productActivityBo.setProductAmount(convert.getF2());
        productActivityService.insertByBo(productActivityBo);
        // 缓存幸运号id
        RedisUtils.setCacheObject(luckyKey, productActivityBo.getId(), Duration.ofDays(10));
    }

    public String getLuckyKey() {
        return "lucky:" + DateUtil.today();
    }
}
