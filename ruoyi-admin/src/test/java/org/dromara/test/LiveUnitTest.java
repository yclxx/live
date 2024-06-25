package org.dromara.test;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.live.domain.vo.ProductLogVo;
import org.dromara.live.domain.vo.PushListVo;
import org.dromara.live.service.IProductLogService;
import org.dromara.live.service.IProductPushService;
import org.dromara.live.utils.LiveUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
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
//        List<String> infoDate = productLogService.queryInfoDate();
//        for (String date : infoDate) {
//            productPushService.push20001(date);
//        }
//        log.info("执行完成");
        testTest20002();
    }

    public void testTest20002() {
        List<String> infoDate = productLogService.queryInfoDate();
        List<PushListVo> pushListVos = new ArrayList<>(infoDate.size());
        for (String date : infoDate) {
            // 查询
            List<ProductLogVo> productLogVos = productLogService.queryBy20002(date);
            List<String> collect = new ArrayList<>();
            for (ProductLogVo productLogVo : productLogVos) {
                if (productLogVo.getF3().compareTo(BigDecimal.ZERO) < 0) {
                    continue;
                }
                // 查询前一天数据
                String firstInfoDate = productLogService.queryFirstInfoDate(productLogVo.getInfoDate());
                ProductLogVo infoDateLogVo = productLogService.queryByProductCodeAndInfoDate(productLogVo.getProductCode(), firstInfoDate);
                if (null == infoDateLogVo) {
                    continue;
                }
                // 判断时间是否在13:30之前
                if (DateUtils.isBefore11(new Date())) {
                    if (productLogVo.getF6().compareTo(infoDateLogVo.getF6()) < 0) {
                        continue;
                    }
                } else {
                    if (productLogVo.getF6().compareTo(infoDateLogVo.getF6().multiply(new BigDecimal("1.5"))) < 0) {
                        continue;
                    }
                }
                collect.add(productLogVo.getProductCode());
            }
            if (ObjectUtil.isEmpty(collect)) {
                continue;
            }
            // 验证成功率
            List<String> vaList = productLogService.queryAnalyseVerify(date, collect);
            List<ProductLogVo> okList = productLogService.queryListByProductCodeList(productLogService.queryNextInfoDate(date), collect);
            if (ObjectUtil.isEmpty(vaList) || ObjectUtil.isEmpty(okList)) {
                continue;
            }
            BigDecimal divide = okList.stream().map(ProductLogVo::getF3).toList().stream().reduce(BigDecimal.ZERO, BigDecimal::add).divide(new BigDecimal(okList.size()), 2, RoundingMode.HALF_UP);

            PushListVo pushListVo = getPushListVo(date, collect, vaList, divide);

            pushListVos.add(pushListVo);
        }
        int hg = 0;
        int bhg = 0;
        for (PushListVo pushListVo : pushListVos) {
            log.info("日期: {}，推荐数量：{}，正确数量：{}，正确率：{}，平均涨幅：{}，推荐内容：{}，正确内容：{}", pushListVo.getInfoDate(), pushListVo.getPushList().size(), pushListVo.getOkList().size(), pushListVo.getOkRate(), pushListVo.getAvgRate(), pushListVo.getPushList(), pushListVo.getOkList());
            if (pushListVo.getOkRate().compareTo(new BigDecimal("70")) > 0) {
                hg++;
            } else {
                bhg++;
            }
        }
        log.info("总计：{}，合格数据：{}，不合格：{}", pushListVos.size(), hg, bhg);
    }

    public void testTest20001() {
        List<String> infoDate = productLogService.queryInfoDate();
        List<PushListVo> pushListVos = new ArrayList<>(infoDate.size());
        for (String date : infoDate) {
            // 查询
            List<ProductLogVo> productLogVos = productLogService.queryBy20001(date);
            List<String> collect = new ArrayList<>();
            for (ProductLogVo productLogVo : productLogVos) {
                List<ProductLogVo> afterList = productLogService.queryBy20001AfterList(date, productLogVo.getProductCode(), 20);
                if (afterList.size() < 20) {
                    continue;
                }
                boolean tg = true;
                for (ProductLogVo logVo : afterList) {
                    if (logVo.getF3().compareTo(new BigDecimal("9.5")) > 0) {
                        tg = false;
                        break;
                    }
                }
                if (tg) {
                    continue;
                }
                List<ProductLogVo> productLogVos1 = afterList.subList(0, 5);
                boolean f3tg = false;
                for (ProductLogVo logVo : productLogVos1) {
                    if (logVo.getF3().compareTo(new BigDecimal("3")) > 0 || !LiveUtils.checkMa20F16(logVo, new BigDecimal("0.01"))) {
                        f3tg = true;
                        break;
                    }
                }
                if (f3tg) {
                    continue;
                }
                int ma20_10count = 0;
                int sz = 0;
                BigDecimal f3add = new BigDecimal("0");
                List<ProductLogVo> ma20_10 = afterList.subList(0, 10);
                for (int i = 0; i < ma20_10.size(); i++) {
                    ProductLogVo logVo = ma20_10.get(i);
                    if (logVo.getF16().compareTo(logVo.getMa20()) < 0) {
                        ma20_10count++;
                    }
                    if (i < 3 && logVo.getF3().compareTo(new BigDecimal("1")) > 0) {
                        sz++;
                    }
                    if (i < 4 && logVo.getF3().compareTo(new BigDecimal("0")) > 0) {
                        f3add = f3add.add(logVo.getF3());
                    }
                }
                if (ma20_10count > 4 || sz > 1 || f3add.compareTo(new BigDecimal("4")) > 0) {
                    continue;
                }
                collect.add(productLogVo.getProductCode());
            }
            if (ObjectUtil.isEmpty(collect)) {
                continue;
            }
            // 验证成功率
            List<String> vaList = productLogService.queryAnalyseVerify(date, collect);
            List<ProductLogVo> okList = productLogService.queryListByProductCodeList(productLogService.queryNextInfoDate(date), collect);
            BigDecimal divide = okList.stream().map(ProductLogVo::getF3).toList().stream().reduce(BigDecimal.ZERO, BigDecimal::add).divide(new BigDecimal(okList.size()), 2, RoundingMode.HALF_UP);

            PushListVo pushListVo = getPushListVo(date, collect, vaList, divide);

            pushListVos.add(pushListVo);
        }
        int hg = 0;
        int bhg = 0;
        for (PushListVo pushListVo : pushListVos) {
            log.info("日期: {}，推荐数量：{}，正确数量：{}，正确率：{}，平均涨幅：{}，推荐内容：{}，正确内容：{}", pushListVo.getInfoDate(), pushListVo.getPushList().size(), pushListVo.getOkList().size(), pushListVo.getOkRate(), pushListVo.getAvgRate(), pushListVo.getPushList(), pushListVo.getOkList());
            if (pushListVo.getOkRate().compareTo(new BigDecimal("70")) > 0) {
                hg++;
            } else {
                bhg++;
            }
        }
        log.info("总计：{}，合格数据：{}，不合格：{}", pushListVos.size(), hg, bhg);
    }

    private static @NotNull PushListVo getPushListVo(String date, List<String> pushList, List<String> okList, BigDecimal divide) {
        PushListVo pushListVo = new PushListVo();
        pushListVo.setInfoDate(date);
        pushListVo.setPushList(pushList);
        pushListVo.setOkList(okList);
        pushListVo.setAvgRate(divide);
        BigDecimal verifySize = new BigDecimal(pushListVo.getOkList().size());
        BigDecimal productSize = new BigDecimal(pushListVo.getPushList().size());
        BigDecimal multiply = verifySize.divide(productSize, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
        pushListVo.setOkRate(multiply);
        return pushListVo;
    }

}
