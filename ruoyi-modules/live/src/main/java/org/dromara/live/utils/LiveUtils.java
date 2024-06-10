package org.dromara.live.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.live.domain.vo.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xx
 */
@Slf4j
public class LiveUtils {
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
        String data = HttpUtil.createGet(url).header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36").header("Cookie", "qgqp_b_id=18c28b304dff3b8ce113d0cca03e6727; websitepoptg_api_time=1703860143525; st_si=92728505415389; st_asi=delete; HAList=ty-100-HSI-%u6052%u751F%u6307%u6570; st_pvi=46517537371152; st_sp=2023-10-29%2017%3A00%3A19; st_inirUrl=https%3A%2F%2Fcn.bing.com%2F; st_sn=8; st_psi=20231229230312485-113200301321-2076002087").execute().body();
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
     * 获取全部数据
     *
     * @param cmd  代码
     *             "沪深京A股": "f3&fs=m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048",
     *             "上证A股": "f3&fs=m:1+t:2,m:1+t:23",
     *             "深证A股": "f3&fs=m:0+t:6,m:0+t:80",
     *             "北证A股": "f3&fs=m:0+t:81+s:2048",
     *             "新股": "f26&fs=m:0+f:8,m:1+f:8",
     *             "创业板": "f3&fs=m:0+t:80",
     *             "科创板": "f3&fs=m:1+t:23",
     *             "沪股通": "f26&fs=b:BK0707",
     *             "深股通": "f26&fs=b:BK0804",
     *             "B股": "f3&fs=m:0+t:7,m:1+t:3",
     *             "风险警示板": "f3&fs=m:0+f:4,m:1+f:4",
     * @param page 页码
     * @return 结果
     */
    public static List<GpDayAllVo> getGpDayAllVo(String cmd, int page) {
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

    /**
     * 获取单个票近半年数据
     *
     * @param code 代码
     * @return 返回结果
     */
    public static List<GpInfoVo> getGpInfoVoList(String code, String name) {
        List<String> gpDateList = getGpInfoString(code);
        if (null == gpDateList || gpDateList.isEmpty()) {
            return null;
        }
        List<GpInfoVo> gpInfoVoList = new ArrayList<>(gpDateList.size());
        // 将数据转对象
        for (int i = 0; i < gpDateList.size(); i++) {
            GpInfoVo gpInfoVo = new GpInfoVo(gpDateList.get(i), code, name);
            gpInfoVoList.add(gpInfoVo);
            gpInfoVo.setMa(gpInfoVoList, i);
        }
        return gpInfoVoList;
    }

    /**
     * 获取单个票近半年数据
     *
     * @param code 代码
     * @return 返回结果
     */
    public static List<String> getGpInfoString(String code) {
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
     * 基础校验
     *
     * @param gpInfoVo 对象
     * @return true 校验不通过 false 校验通过
     */
    public static boolean checkBase(final GpInfoVo gpInfoVo) {
        return checkBase(gpInfoVo, 3);
    }

    /**
     * 基础校验
     *
     * @param gpInfoVo 对象
     * @return true 校验不通过 false 校验通过
     */
    public static boolean checkBase(final GpInfoVo gpInfoVo, int days) {
        // 校验时间不能和当天差值超过3天
        int czDate = DateUtils.differentDaysByMillisecond(new Date(), DateUtils.parseDate(gpInfoVo.getInfoDate()));
        if (czDate > days) {
            return true;
        }
        return null == gpInfoVo.getF2() || null == gpInfoVo.getMa20();
    }

    /**
     * 成交金额校验
     *
     * @param gpInfoVo 对象
     * @return true 校验不通过 false 校验通过
     */
    public static boolean checkAmount(final GpInfoVo gpInfoVo, BigDecimal amount) {
        // 默认1亿
        if (null == amount) {
            amount = new BigDecimal("100000000");
        }
        // 判断当前时间是否在9点35之前，如果是设置成交金额默认0
        String dateStr = DateUtils.getDate() + " 09:35:00";
        Date date = DateUtil.parse(dateStr);
        // 判断当前时间是否在10点，如果是设置成交金额默认50000000
        String dateStr10 = DateUtils.getDate() + " 10:00:00";
        Date date10 = DateUtil.parse(dateStr10);
        if (date.getTime() > System.currentTimeMillis()) {
            amount = new BigDecimal("0");
        } else if (date10.getTime() > System.currentTimeMillis()) {
            amount = new BigDecimal("50000000");
        }
        return gpInfoVo.getF6().compareTo(amount) < 0;
    }

    /**
     * 换手率校验
     *
     * @param gpInfoVo 对象
     * @return true 校验不通过 false 校验通过
     */
    public static boolean checkTurnoverRate(final GpInfoVo gpInfoVo, BigDecimal amount) {
        // 默认换手率低于2%的不要
        if (null == amount) {
            amount = new BigDecimal("2");
        }
        return gpInfoVo.getF8().compareTo(amount) < 0;
    }

    /**
     * 涨跌幅 >0 < 自定义参数时
     *
     * @param gpInfoVo 对象
     * @return true 校验不通过 false 校验通过
     */
    public static boolean checkF3(final GpInfoVo gpInfoVo, BigDecimal amount) {
        // 涨跌幅不在0到2%的不要
        if (null == amount) {
            amount = new BigDecimal("2");
        }
        return gpInfoVo.getF3().compareTo(amount) > 0 || gpInfoVo.getF3().compareTo(BigDecimal.ZERO) <= 0;
    }

    /**
     * 校验近10天最高价有没有超过当前价的10%
     *
     * @param gpInfoVo 对象
     * @return true 校验不通过 false 校验通过
     */
    public static boolean check10MaxAmount(final GpInfoVo gpInfoVo, BigDecimal bl) {
        if (null == gpInfoVo.getMax10() || gpInfoVo.getMax10().compareTo(gpInfoVo.getF2()) < 0) {
            return true;
        }
        // 最高价日期需要在30天内
        int czDate = DateUtils.differentDaysByMillisecond(new Date(), DateUtils.parseDate(gpInfoVo.getMax10Date()));
        if (czDate > 31 || czDate < 3) {
            return true;
        }
        BigDecimal max10abs = gpInfoVo.getMax10().subtract(gpInfoVo.getF2()).abs();
        BigDecimal max10divide = max10abs.divide(gpInfoVo.getF2(), 3, RoundingMode.HALF_UP);
        return max10divide.compareTo(bl) < 0;
    }

    /**
     * 校验20日均价和当前价上下浮动1%
     *
     * @param gpInfoVo 对象
     * @return true 校验不通过 false 校验通过
     */
    public static boolean checkMa20(final GpInfoVo gpInfoVo, BigDecimal bl) {
        BigDecimal ma20abs = gpInfoVo.getF2().subtract(gpInfoVo.getMa20()).abs();
        BigDecimal ma20divide = ma20abs.divide(gpInfoVo.getF2(), 3, RoundingMode.HALF_UP);
        return ma20divide.compareTo(bl) > 0;
    }

    /**
     * 校验20日均价和当前价上下浮动1%
     *
     * @param gpInfoVo 对象
     * @return true 校验不通过 false 校验通过
     */
    public static boolean checkMa20(final ProductLogVo gpInfoVo, BigDecimal bl) {
        BigDecimal ma20abs = gpInfoVo.getF2().subtract(gpInfoVo.getMa20()).abs();
        BigDecimal ma20divide = ma20abs.divide(gpInfoVo.getF2(), 3, RoundingMode.HALF_UP);
        return ma20divide.compareTo(bl) > 0;
    }

    /**
     * 校验20日均价和当前最低价上下浮动1%
     *
     * @param gpInfoVo 对象
     * @return true 校验不通过 false 校验通过
     */
    public static boolean checkMa20F16(final ProductLogVo gpInfoVo, BigDecimal bl) {
        BigDecimal ma20abs = gpInfoVo.getF16().subtract(gpInfoVo.getMa20()).abs();
        BigDecimal ma20divide = ma20abs.divide(gpInfoVo.getF2(), 3, RoundingMode.HALF_UP);
        return ma20divide.compareTo(bl) > 0;
    }

    public static boolean check10000(GpInfoVo gpInfoVo) {
        // 基础校验
        if (LiveUtils.checkBase(gpInfoVo)) {
            return false;
        }
        // 成交金额校验
        if (LiveUtils.checkAmount(gpInfoVo, null)) {
            return false;
        }
        if (LiveUtils.check10MaxAmount(gpInfoVo, new BigDecimal("0.1"))) {
            return false;
        }
        if (LiveUtils.checkMa20(gpInfoVo, new BigDecimal("0.01"))) {
            return false;
        }
        return true;
    }

    public static boolean check10001(List<GpInfoVo> gpInfoVoList) {
        if (null == gpInfoVoList || gpInfoVoList.isEmpty() || gpInfoVoList.size() < 4) {
            return false;
        }
        List<GpInfoVo> gpInfoVos = gpInfoVoList.subList(gpInfoVoList.size() - 3, gpInfoVoList.size());
        for (GpInfoVo gpInfoVo : gpInfoVos) {
            // 换手率校验
            if (LiveUtils.checkBase(gpInfoVo)) {
                return false;
            }
            if (LiveUtils.checkTurnoverRate(gpInfoVo, new BigDecimal("3"))) {
                return false;
            }
            if (LiveUtils.checkMa20(gpInfoVo, new BigDecimal("0.02"))) {
                return false;
            }
        }
        return true;
    }

    public static int check10002(List<GpInfoVo> gpInfoVoList) {
        int weight = 0;
        if (null == gpInfoVoList || gpInfoVoList.isEmpty() || gpInfoVoList.size() < 21) {
            return weight;
        }
        List<GpInfoVo> gpInfoVos = gpInfoVoList.subList(gpInfoVoList.size() - 20, gpInfoVoList.size());
        for (GpInfoVo gpInfoVo : gpInfoVos) {
            // 如果涨跌幅度大于0
            if (gpInfoVo.getF3().compareTo(BigDecimal.ZERO) > 0) {
                weight = weight + gpInfoVo.getF3().intValue();
            }
            if (gpInfoVo.getF2().compareTo(gpInfoVo.getMa20()) >= 0) {
                weight++;
            }
            int f8 = gpInfoVo.getF8().intValue();
            if (f8 < 20 && f8 > 10) {
                weight = weight + 3;
            } else if (f8 >= 20 || f8 < 5) {
                weight++;
            } else {
                weight = weight + 2;
            }
        }
        return weight;
    }

    public static boolean check10003(List<GpInfoVo> gpInfoVoList) {
        if (null == gpInfoVoList || gpInfoVoList.isEmpty() || gpInfoVoList.size() < 4) {
            return false;
        }
        List<GpInfoVo> gpInfoVos = gpInfoVoList.subList(gpInfoVoList.size() - 3, gpInfoVoList.size());
        for (GpInfoVo gpInfoVo : gpInfoVos) {
            if (LiveUtils.checkBase(gpInfoVo)) {
                return false;
            }
            // 成交金额校验
            if (LiveUtils.checkAmount(gpInfoVo, null)) {
                return false;
            }
            if (LiveUtils.checkF3(gpInfoVo, null)) {
                return false;
            }
        }
        return true;
    }
}
