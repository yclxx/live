package org.dromara.live.controller;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.live.domain.vo.*;
import org.dromara.live.utils.LiveUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 25487
 */
@Slf4j
public class LiveController {
    /*
     * "沪深京A股": "f3&fs=m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048",
     * "上证A股": "f3&fs=m:1+t:2,m:1+t:23",
     * "深证A股": "f3&fs=m:0+t:6,m:0+t:80",
     * "北证A股": "f3&fs=m:0+t:81+s:2048",
     * "新股": "f26&fs=m:0+f:8,m:1+f:8",
     * "创业板": "f3&fs=m:0+t:80",
     * "科创板": "f3&fs=m:1+t:23",
     * "沪股通": "f26&fs=b:BK0707",
     * "深股通": "f26&fs=b:BK0804",
     * "B股": "f3&fs=m:0+t:7,m:1+t:3",
     * "风险警示板": "f3&fs=m:0+f:4,m:1+f:4",
     */

    /*
    格式：时间,开盘价,收盘价,最高价,最低价,成交量(手),成交额(元),振幅(%),涨跌幅(%),涨跌额(元),换手率(%),
    2024-05-23,22.67,22.19,22.75,22.09,28908,64456705.00,2.89,-2.72,-0.62,1.52
    2024-05-24,22.32,26.63,26.63,22.28,172434,433760762.90,19.60,20.01,4.44,9.06
     */

    /**
     * 发送请求获取数据
     *
     * @param url 请求url
     * @return 返回结果
     */
    private static String getDataString(String url) {
        String data = HttpUtil.createGet(url)
            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
            .header("Cookie", "qgqp_b_id=18c28b304dff3b8ce113d0cca03e6727; websitepoptg_api_time=1703860143525; st_si=92728505415389; st_asi=delete; HAList=ty-100-HSI-%u6052%u751F%u6307%u6570; st_pvi=46517537371152; st_sp=2023-10-29%2017%3A00%3A19; st_inirUrl=https%3A%2F%2Fcn.bing.com%2F; st_sn=8; st_psi=20231229230312485-113200301321-2076002087")
            .execute().body();
        // 找到第一个括号的位置
        int startIndex = data.indexOf("(");
        // 找到最后一个括号的位置
        int endIndex = data.lastIndexOf(")");
        if (startIndex != -1 && endIndex != -1) {
            // 截取括号内的内容
            data = data.substring(startIndex + 1, endIndex);
        }
        return data;
    }

    /**
     * 获取股票全部数据
     *
     * @param cmd  股票代码
     * @param page 页码
     * @return 结果
     */
    private static List<GpDayAllVo> getGpDayAllVo(String cmd, String page) {
        String url = "https://7.push2.eastmoney.com/api/qt/clist/get?cb=jQuery112409467675731682619_1703939377395&pn=" + page + "&pz=20&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&wbp2u=|0|0|0|web&fid=" + cmd + "&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152&_=1703939377396";
        String data = getDataString(url);
        GpDayAllTableData gpDayAllTableData = JsonUtils.parseObject(data, GpDayAllTableData.class);
        if (null == gpDayAllTableData) {
            return null;
        }
        GpTableData tableData = gpDayAllTableData.getData();
        if (null == tableData) {
            return null;
        }
        return tableData.getDiff();
    }

    public static List<String> getGpInfo(String code) {
        String url;
        if (code.startsWith("0") || code.startsWith("3")) {
            url = "https://push2his.eastmoney.com/api/qt/stock/kline/get?cb=jQuery35104266865986209545_1716729262976&secid=0." + code + "&ut=fa5fd1943c7b386f172d6893dbfba10b&fields1=f1,f2,f3,f4,f5,f6&fields2=f51,f52,f53,f54,f55,f56,f57,f58,f59,f60,f61&klt=101&fqt=1&end=20500101&lmt=120&_=1716729263096";
        } else if (code.startsWith("60")) {
            url = "https://push2his.eastmoney.com/api/qt/stock/kline/get?cb=jQuery35106941502834387754_1716732705746&secid=1." + code + "&ut=fa5fd1943c7b386f172d6893dbfba10b&fields1=f1,f2,f3,f4,f5,f6&fields2=f51,f52,f53,f54,f55,f56,f57,f58,f59,f60,f61&klt=101&fqt=1&end=20500101&lmt=120&_=1716732705754";
        } else {
            return null;
        }
        String data = getDataString(url);
        GpInfoResultAll gpInfoResultAll = JsonUtils.parseObject(data, GpInfoResultAll.class);
        if (gpInfoResultAll == null || null == gpInfoResultAll.getData()) {
            log.error("获取数据失败，code：{},返回结果：{}", code, data);
            return null;
        }
        return gpInfoResultAll.getData().getKlines();
    }

    /**
     * 获取20日均线附近的票
     */
    public static void getMaGp(String amount) {
        int hgCount = 0;
        // 读取A.xlsx文件
        ClassPathResource pathResource = new ClassPathResource("excel/A.xlsx");
        List<GpInfoResult> gpInfoResults = ExcelUtil.importExcel(pathResource.getStream(), GpInfoResult.class);
        for (GpInfoResult gpInfoResult : gpInfoResults) {
            if (gpInfoResult.getCode().startsWith("0") || gpInfoResult.getCode().startsWith("60")) {
                if (gpInfoResult.getName().contains("ST")) {
                    continue;
                }
                List<String> gpInfo = getGpInfo(gpInfoResult.getCode());
                if (null == gpInfo || gpInfo.isEmpty()) {
                    continue;
                }
                List<GpInfoVo> gpInfoVoList = new ArrayList<>(gpInfo.size());
                // 将数据转对象
                for (String s : gpInfo) {
                    gpInfoVoList.add(new GpInfoVo(s, gpInfoResult.getCode(), gpInfoResult.getName()));
                }
                // 计算当天日期的ma5 ma10 ma20
                GpInfoVo gpInfoVo = gpInfoVoList.get(gpInfoVoList.size() - 1);
                gpInfoVo.setMa(gpInfoVoList, gpInfoVoList.size() - 1);
                // 判断当天的股价和ma20相差在±1%以内的
                if (null != gpInfoVo.getF2() && null != gpInfoVo.getMa20() && gpInfoVo.getF6().compareTo(new BigDecimal(amount)) >= 0) {
                    if (null != gpInfoVo.getMax10()) {
                        // 如果10天最高价超过收盘价5%
                        if (gpInfoVo.getMax10().compareTo(gpInfoVo.getF2()) >= 0) {
                            // 最高价日期需要在30天内
                            int czDate = DateUtils.differentDaysByMillisecond(new Date(), DateUtils.parseDate(gpInfoVo.getMax10Date()));
                            if (czDate < 31 && czDate > 3) {
                                BigDecimal max10abs = gpInfoVo.getMax10().subtract(gpInfoVo.getF2()).abs();
                                BigDecimal max10divide = max10abs.divide(gpInfoVo.getF2(), 3, RoundingMode.HALF_UP);
                                if (max10divide.compareTo(new BigDecimal("0.1")) >= 0) {
                                    BigDecimal ma20abs = gpInfoVo.getF2().subtract(gpInfoVo.getMa20()).abs();
                                    BigDecimal ma20divide = ma20abs.divide(gpInfoVo.getF2(), 3, RoundingMode.HALF_UP);
                                    if (ma20divide.compareTo(new BigDecimal("0.01")) <= 0) {
                                        hgCount++;
                                        log.info("代码：{}，当前价：{}，ma20：{}，20日最高：{},最高日期：{},今日涨跌：{}", gpInfoResult.getCode(), gpInfoVo.getF2(), gpInfoVo.getMa20(), gpInfoVo.getMax10(), gpInfoVo.getMax10Date(), gpInfoVo.getF3());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        log.info("符合条件数量：{}", hgCount);
    }

    public static boolean isHg(GpInfoVo gpInfoVo, int bl) {
        BigDecimal subtract = gpInfoVo.getF2().subtract(gpInfoVo.getMa20());
        BigDecimal divide = subtract.divide(gpInfoVo.getMa20(), 3, RoundingMode.HALF_UP);
        return divide.compareTo(new BigDecimal(bl).multiply(new BigDecimal("0.01"))) <= 0;
    }

    /**
     * 获取20日均线附近的票 且没有跌破20日均线
     */
    public static void getMaGp2() {
        int hgCount = 0;
        // 读取A.xlsx文件
        ClassPathResource pathResource = new ClassPathResource("excel/A.xlsx");
        List<GpInfoResult> gpInfoResults = ExcelUtil.importExcel(pathResource.getStream(), GpInfoResult.class);
        for (GpInfoResult gpInfoResult : gpInfoResults) {
            if (gpInfoResult.getCode().startsWith("0") || gpInfoResult.getCode().startsWith("60")) {
                if (gpInfoResult.getName().contains("ST")) {
                    continue;
                }
                List<String> gpInfo = getGpInfo(gpInfoResult.getCode());
                if (null == gpInfo || gpInfo.isEmpty()) {
                    continue;
                }
                List<GpInfoVo> gpInfoVoList = new ArrayList<>(gpInfo.size());
                // 将数据转对象
                for (String s : gpInfo) {
                    gpInfoVoList.add(new GpInfoVo(s, gpInfoResult.getCode(), gpInfoResult.getName()));
                }
                for (int i = 0; i < gpInfoVoList.size(); i++) {
                    gpInfoVoList.get(i).setMa(gpInfoVoList, i);
                }
                // 计算当天日期的ma5 ma10 ma20
                GpInfoVo gpInfoVo = gpInfoVoList.get(gpInfoVoList.size() - 1);
                // 判断当天的股价和ma20相差在±1%以内的
                if (null != gpInfoVo.getF2() && null != gpInfoVo.getMa20() && gpInfoVo.getF6().compareTo(new BigDecimal("100000000")) >= 0) {
                    // 连续3天在20日均线上方
                    GpInfoVo gpInfoVo2 = gpInfoVoList.get(gpInfoVoList.size() - 2);
                    GpInfoVo gpInfoVo3 = gpInfoVoList.get(gpInfoVoList.size() - 3);
                    if (gpInfoVo.getF2().compareTo(gpInfoVo.getMa20()) >= 0 &&
                        gpInfoVo2.getF2().compareTo(gpInfoVo2.getMa20()) >= 0 &&
                        gpInfoVo3.getF2().compareTo(gpInfoVo3.getMa20()) >= 0) {
                        if (isHg(gpInfoVo, 3) && isHg(gpInfoVo2, 3) && isHg(gpInfoVo3, 3)) {
                            hgCount++;
                            log.info("代码：{}，当前价：{}，ma20：{}", gpInfoResult.getCode(), gpInfoVo.getF2(), gpInfoVo.getMa20());
                        }
                    }
                }
            }
        }
        log.info("符合条件数量：{}", hgCount);
    }

    /**
     * 获取20日均线附近的票
     */
    public static void getMaGpUpdate() {
        int hgCount = 0;
        // 读取A.xlsx文件
        ClassPathResource pathResource = new ClassPathResource("excel/A.xlsx");
        List<GpInfoResult> gpInfoResults = ExcelUtil.importExcel(pathResource.getStream(), GpInfoResult.class);
        for (GpInfoResult gpInfoResult : gpInfoResults) {
            if (gpInfoResult.getCode().startsWith("0") || gpInfoResult.getCode().startsWith("60")) {
                if (gpInfoResult.getName().contains("ST")) {
                    continue;
                }
                List<GpInfoVo> gpInfoVoList = LiveUtils.getGpInfoVoList(gpInfoResult.getCode(), gpInfoResult.getName());
                if (null == gpInfoVoList || gpInfoVoList.isEmpty()) {
                    continue;
                }
                // 计算当天日期的ma5 ma10 ma20
                GpInfoVo gpInfoVo = gpInfoVoList.getLast();
                // 基础校验
                if (LiveUtils.checkBase(gpInfoVo)) {
                    continue;
                }
                // 成交金额校验
                if (LiveUtils.checkAmount(gpInfoVo, null)) {
                    continue;
                }
                if (LiveUtils.check10MaxAmount(gpInfoVo, new BigDecimal("0.1"))) {
                    continue;
                }
                if (LiveUtils.checkMa20(gpInfoVo, new BigDecimal("0.01"))) {
                    continue;
                }
                hgCount++;
                log.info("代码：{}，当前价：{}，ma20：{}，20日最高：{},最高日期：{},今日涨跌：{}", gpInfoResult.getCode(), gpInfoVo.getF2(), gpInfoVo.getMa20(), gpInfoVo.getMax10(), gpInfoVo.getMax10Date(), gpInfoVo.getF3());
            }
        }
        log.info("符合条件数量：{}", hgCount);
    }

    public static void main(String[] args) {

//        getMaGpUpdate();

//        List<GpInfoVo> gpInfo = LiveUtils.getGpInfoVoList("002071", "002071");
//        int i = LiveUtils.check10002(gpInfo);
//        System.out.println(i);
//        log.info("接口返回结果：{}", gpInfo);

        // 获取20日均线附近的票
//        getMaGp("80000000");

        // 获取20日均线附近的票 且没有跌破20日均线
//        getMaGp2();

//        List<GpDayAllVo> gpDayAllVo = getGpDayAllVo("f3&fs=m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048", "281");
//        log.info("接口返回结果：{}", gpDayAllVo);

//        List<String> gpInfo = getGpInfo("601600");
//        if (null == gpInfo || gpInfo.isEmpty()) {
//            return;
//        }
//        List<GpInfoVo> gpInfoVoList = new ArrayList<>(gpInfo.size());
//        // 将数据转对象
//        for (String s : gpInfo) {
//            gpInfoVoList.add(new GpInfoVo(s));
//        }
//        // 计算所有日期的ma5 ma10 ma20
//        for (int i = 0; i < gpInfoVoList.size(); i++) {
//            GpInfoVo gpInfoVo = gpInfoVoList.get(i);
//            gpInfoVo.setMa(gpInfoVoList, i);
//        }
//        log.info("结果：{}", gpInfoVoList);

//        BigDecimal cz = new BigDecimal("0.01");
//        BigDecimal ma20 = new BigDecimal("10");
//        BigDecimal f2 = new BigDecimal("9.89");
//
//        BigDecimal abs = f2.subtract(ma20).abs();
//        log.info("abs：{}", abs);
//        BigDecimal divide = abs.divide(f2, 3, RoundingMode.HALF_UP);
//        log.info("divide：{}", divide);
    }
}
