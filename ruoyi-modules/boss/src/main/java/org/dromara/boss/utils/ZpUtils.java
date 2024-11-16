package org.dromara.boss.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.dromara.boss.domain.vo.ZpAddResultDataVo;
import org.dromara.boss.domain.vo.ZpDetailInfoVo;
import org.dromara.boss.domain.vo.ZpListDataVo;
import org.dromara.boss.domain.vo.ZpResultVo;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.json.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiexi
 * @description
 * @date 2024/11/9 19:23
 */
@Slf4j
public class ZpUtils {
    /**
     * 请求头，全局配置
     */
    private Map<String, String> headers;
    private static volatile ZpUtils instance;

    private ZpUtils() {
    }

    private void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * 查询招聘列表
     *
     * @param encryptExpectId 选择的城市
     * @param page            页码
     * @return 招聘列表
     */
    public ZpListDataVo queryZpList(String encryptExpectId, Integer page) {
        // 请求地址
        String url = "https://www.zhipin.com/wapi/zpgeek/pc/recommend/job/list.json";
        // ?city=&experience=&payType=&partTime=&degree=&industry=&scale=&salary=&jobType=
        // &encryptExpectId=56aa55211a9b5edc1nR93tm4FlNZxA~~&page=1&pageSize=15
        Map<String, Object> params = new HashMap<>();
        params.put("city", "");
        params.put("experience", "");
        params.put("payType", "");
        params.put("partTime", "");
        params.put("degree", "");
        params.put("industry", "");
        params.put("scale", "");
        params.put("salary", "");
        params.put("jobType", "");
        params.put("encryptExpectId", encryptExpectId);
        params.put("page", page);
        params.put("pageSize", 15);

        return sendHttpRequest(Method.GET, url, params, ZpListDataVo.class);
    }

    public ZpDetailInfoVo queryZpInfoVo(String securityId, String lid) {
        // 请求地址
        // https://www.zhipin.com/wapi/zpgeek/job/detail.json
        // 参数
        // ?securityId=Yl6DUfvvEpj8b-31YuVndKl6Xx3VYm-81e0uQHCmBLaL8INLJtE0QTepE6JmuZcaVbM5ySzSeX8WcxPMr2L7roqh75DyfcpMD8l_Sw1kuAGxCPhB_Jmx2Fz96orKL21d425vQu53QJn2OZ3umkNn1t5JxfZe5Hsiuz4EGXJ6aB9V9dE51p00G5Ijdz8FCUpsvYRMT3L7-ocfv48kB65_-jOXaybRzCHp3dGJPQ~~&lid=6465a9ca-09f3-4da1-8e31-57ce56756fd7.f1:common.eyJzZXNzaW9uSWQiOiI0Yzc0Y2UwZC1jOTU3LTRlOTgtYmM1OS0yNGU5OWNjM2RmMTMiLCJyY2RCelR5cGUiOiJmMV9ncmNkIn0.1

        // 返回结果
        // {"code":0,"message":"Success",
        // "zpData":{"pageType":0,"selfAccess":false,"securityId":"sDM8Qq0Qp6He_-21HVG-LEOkL1sx5s6xhoSCC6myW0p0rmg4PrMJKgUXgTPHIRBmEOqCGPak_1gVtEXOMpOyNKAixMFbdJirLuw0IJsMMf-p1-Q8MA7Jue3lcPC33hCel6jnruTx4SDf0D2qvwvxSbxShBYZ6qt9YK9bCAdSywd7aKgwMmsnLz3rygErZJlYrmKUK5vyz_5MqXTQBvx28hZcAad2nuTDj6t4Uw~~"
        // ,"sessionId":null,"lid":"f6c2d8a7-8ab4-4382-8558-360153b21adf.f1:common.eyJzZXNzaW9uSWQiOiI5MWRhMTc4ZC02NzdlLTQ0NjItOTIzYS1lODM5OGIzY2QyYzUiLCJyY2RCelR5cGUiOiJmMV9ncmNkIn0.1"
        // ,"jobInfo":{"encryptId":"40e30d5b49f355531H1y29-7FFtS","encryptUserId":"3ed72fe18cbbb5521XR_3dq1Elc~"
        // ,"invalidStatus":false,"jobName":"Android应用工程师","position":100202,"positionName":"Android"
        // ,"location":101280100,"locationName":"广州","experienceName":"1-3年","degreeName":"本科"
        // ,"jobType":0,"proxyJob":0,"proxyType":0,"salaryDesc":"12-15K·13薪","payTypeDesc":null
        // ,"postDescription":"工作职责：\n1、负责Android和IOS应用开发与迭代，完成软件设计、开发、调试等工作。\n2、负责项目的质量优化、架构和难点攻关等工作，提升产品基础质量。\n3、研究行业新兴技术，满足产品需求。\n\n岗位职责：\n1、具有1年以上安卓平台开发经验、计算机相关专业、本科及以上学历。\n2、熟悉Java，在数据结构、软件设计方等方面有扎实的技术功底。\n3、熟悉Android SDK，熟悉Android UI设计规范和系统UI实现原理。\n4、熟悉网络开发、多线程开发、进程间通信等知识。"
        // ,"encryptAddressId":"9f839c3f2797d9e40XR929-0Elc~","address":"广州白云区广州飞傲电子科技有限公司白云区龙归街夏良龙良路21号"
        // ,"longitude":113.324126,"latitude":23.283803,"staticMapUrl":"https://img.bosszhipin.com/beijin/amap/staticMap/e0fc42066597daefea2bd7435052c017"
        // ,"pcStaticMapUrl":"https://img.bosszhipin.com/beijin/amap/staticMap/86d1df74fd944818798e908e38e4815a"
        // ,"overseasAddressList":[],"overseasInfo":null,"showSkills":["Java","Android客户端产品研发"]
        // ,"anonymous":0,"jobStatusDesc":"最新"}
        // ,"bossInfo":{"name":"何柳艳","title":"招聘职位","tiny":"https://img.bosszhipin.com/beijin/upload/avatar/20240622/607f1f3d68754fd00a4c8ea88c5b3b7355ea2440dbf28f2aa3b593af0cd47c75b555cfb87793e4e0_s.jpg.webp"
        // ,"large":"https://img.bosszhipin.com/beijin/upload/avatar/20240622/607f1f3d68754fd00a4c8ea88c5b3b7355ea2440dbf28f2aa3b593af0cd47c75b555cfb87793e4e0.jpg.webp","activeTimeDesc":"3日内活跃","bossOnline":false,"brandName":"广州飞傲电子科技...","bossSource":0,"certificated":true,"tagIconUrl":null,"avatarStickerUrl":null},"brandComInfo":{"encryptBrandId":"e638385a55d4ca590nB62ty0FQ~~","brandName":"广州飞傲电子科技...","logo":"https://img.bosszhipin.com/beijin/mcs/chatphoto/20190809/6821bf6c65abefb0468f3926d5669a8fa3b593af0cd47c75b555cfb87793e4e0_s.jpg","stage":801,"stageName":"未融资","scale":303,"scaleName":"100-499人","industry":101304,"industryName":"其他行业","introduce":"广州飞傲电子科技有限公司成立于2007年，是一家专注于高品质HiFi设备的创新型科技企业。一直以来，飞傲都致力于自主研发生产，目前拥有品牌“FiiO（飞傲）”和主打年轻、时尚、高性价比的电子科技品牌“JadeAudio（翡声）”。\n         目前公司有300多员工，核心研发团队拥有近30年音频产品研发经验的核心研发团队，包括软件、硬件、工业设计、结构、电声等多个专业的研发队伍达到了100多人。\n         公司2022年迁入15000平方米的独立工业园，包含2000平研发中心、2000平营销中心、8000平制造中心。在产品生产过程中，我们追求高效化、科技化，配备先进齐全的设备，如价值20万的示波器、30万元的B&K仿真人头、50万元AP测试仪、百万级的OTA微波暗室等。公司全新升级的无尘车间，4条双向总装流水线，年产量可达200万台，并预留了200万台的产能扩充场地。\n          飞傲不仅致力于自主研发生产，还在海内外发展本地代理商，目前已覆盖包括亚洲、美洲、欧洲、大洋洲和非洲的五个大洲60多个国家和地区，并在行业内取得了良好的口碑。\n            公司关注每一位员工的成长，尊重员工、关爱员工，通过丰富多彩的员工集体活动，形成良好企业文化氛围，与员工共谋发展、共享美好。自公司创办以来，公司内部员工稳定性高，人员流动率较低。随着公司规模的扩大，在维持优秀老员工稳定性的同时，我们也在不断注入新鲜血液，广纳贤才，欢迎有志之士加入飞傲大家庭。","labels":["五险一金","定期体检","年终奖","带薪年假","员工旅游","节日福利"],"activeTime":1731059980000,"visibleBrandInfo":true,"focusBrand":false,"customerBrandName":"广州飞傲电子科技...","customerBrandStageName":"未融资"},"oneKeyResumeInfo":{"inviteType":0,"alreadySend":false,"canSendResume":true,"canSendPhone":false,"canSendWechat":false},"relationInfo":{"interestJob":false,"beFriend":false},"handicappedInfo":null,"appendixInfo":{"canFeedback":true,"chatBubble":null},"atsOnlineApplyInfo":{"inviteType":0,"alreadyApply":false}}}

        String url = "https://www.zhipin.com/wapi/zpgeek/job/detail.json";

        Map<String, Object> params = new HashMap<>();
        params.put("securityId", securityId);
        params.put("lid", lid);

        return sendHttpRequest(Method.GET, url, params, ZpDetailInfoVo.class);
    }

    public ZpAddResultDataVo addZp(String securityId, String jobId, String lid) {
        // 返回结果
        //        "showGreeting": true,
        //        "greeting": "您好，我对这个岗位以及贵公司都很有兴趣，也觉得岗位非常适合自己，相信自己也能为贵公司提供价值。可以查看我的简历，如果您觉得合适，可以随时联系我，感谢",
        //        "securityId": "3T1vPCUqhocWo-b1NQs23aHSFvsL5jnJlwzoVlyMS9sTqQBWg9NTja_QlQYXWO_gnxgQKeTJpwqChpAorzTEIVfFpls8WS_ul-qfORURtEKuwitp6TBPMA~~",
        //        "bossSource": 0,
        //        "source": "",
        //        "encBossId": "5bbf6635f516a3071XR82tS4EVU~"

        // 请求地址
        // https://www.zhipin.com/wapi/zpgeek/friend/add.json
        // ?securityId=3T1vPCUqhocWo-b1NQs23aHSFvsL5jnJlwzoVlyMS9sTqQBWg9NTja_QlQYXWO_gnxgQKeTJpwqChpAorzTEIVfFpls8WS_ul-qfORURtEKuwitp6TBPMA~~&jobId=c101d100fc253e5f1nB80t-4EFRX&lid=7VagaMw8aHA.search.1

        String url = "https://www.zhipin.com/wapi/zpgeek/friend/add.json";

        Map<String, Object> params = new HashMap<>();
        params.put("securityId", securityId);
        params.put("lid", lid);
        params.put("jobId", jobId);

        try {
            return sendHttpRequest(Method.POST, url, params, ZpAddResultDataVo.class);
        } catch (Exception e) {
            log.error("投递失败，", e);
            return null;
        }
    }

    /**
     * 发送get 请求
     *
     * @param method 请求方式
     * @param url    请求地址
     * @param params 请求参数
     * @param type   返回数据类型
     * @return 返回结果
     */
    private <T> T sendHttpRequest(Method method, String url, Map<String, Object> params, Class<T> type) {
        HttpRequest request = HttpUtil.createRequest(method, url);
        request.addHeaders(headers);

        String result = request.form(params).execute().body();
        log.info("请求url：{}，请求参数：{}，返回结果：{}", url, params, result);

        ZpResultVo zpResult = JsonUtils.parseObject(result, ZpResultVo.class);

        if (null == zpResult) {
            throw new ServiceException("请求boos直聘接口失败，返回结果为空");
        }
        if (0 != zpResult.getCode()) {
            throw new ServiceException(zpResult.getMessage());
        }
        return zpResult.getZpData(type);
    }

    public static ZpUtils getInstance() {
        return getInstance(null);
    }

    public static ZpUtils getInstance(Map<String, String> headers) {
        if (null == instance) {
            synchronized (ZpUtils.class) {
                if (null == instance) {
                    instance = new ZpUtils();
                }
            }
        }
        if (ObjectUtil.isNotEmpty(headers)) {
            instance.setHeaders(headers);
        }
        return instance;
    }
}