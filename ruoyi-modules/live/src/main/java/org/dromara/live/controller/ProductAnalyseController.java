package org.dromara.live.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.thread.ThreadUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.live.domain.bo.ProductAnalyseBo;
import org.dromara.live.domain.vo.ProductAnalyseVo;
import org.dromara.live.service.IProductAnalyseService;
import org.dromara.live.service.IProductLogService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计分析
 *
 * @author xx
 * @date 2024-06-04
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/live/productAnalyse")
public class ProductAnalyseController extends BaseController {

    private final IProductAnalyseService productAnalyseService;
    private final IProductLogService productLogService;

    /**
     * 查询统计分析列表
     */
    @SaCheckPermission("live:productAnalyse:list")
    @GetMapping("/list")
    public TableDataInfo<ProductAnalyseVo> list(ProductAnalyseBo bo, PageQuery pageQuery) {
        TableDataInfo<ProductAnalyseVo> productAnalyseVoTableDataInfo = productAnalyseService.queryPageList(bo, pageQuery);
        for (ProductAnalyseVo row : productAnalyseVoTableDataInfo.getRows()) {
            row.setVerifyList(JsonUtils.parseArray(row.getVerifyJson(), String.class));
            row.setAnalyseList(JsonUtils.parseArray(row.getAnalyseJson(), String.class));
        }
        return productAnalyseVoTableDataInfo;
    }

    /**
     * 导出统计分析列表
     */
    @SaCheckPermission("live:productAnalyse:export")
    @Log(title = "统计分析", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ProductAnalyseBo bo, HttpServletResponse response) {
        List<ProductAnalyseVo> list = productAnalyseService.queryList(bo);
        ExcelUtil.exportExcel(list, "统计分析", ProductAnalyseVo.class, response);
    }

    /**
     * 获取统计分析详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("live:productAnalyse:query")
    @GetMapping("/{id}")
    public R<ProductAnalyseVo> getInfo(@NotNull(message = "主键不能为空")
                                       @PathVariable String id) {
        return R.ok(productAnalyseService.queryById(id));
    }

    /**
     * 新增统计分析
     */
    @SaCheckPermission("live:productAnalyse:add")
    @Log(title = "统计分析", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add() {
        ThreadUtil.execAsync(() -> {
            final String analyseNo = "D3_001";
            List<String> infoDateList = productLogService.queryInfoDate();
            for (int i = 0; i < infoDateList.size(); i++) {
                int endIndex = i + 3;
                if (endIndex > infoDateList.size()) {
                    break;
                }
                List<String> queryInfoDateList = infoDateList.subList(i, endIndex);
                // 查询是否推荐过了
                ProductAnalyseVo productAnalyseVo = productAnalyseService.queryByUnique(queryInfoDateList.getLast(), analyseNo);
                if (null != productAnalyseVo && StringUtils.isNotBlank(productAnalyseVo.getVerifyJson()) && null != productAnalyseVo.getAccuracy()) {
                    continue;
                }
                Map<String, List<String>> productCodeMap = new LinkedHashMap<>(queryInfoDateList.size());
                boolean add = true;
                for (int j = 0; j < queryInfoDateList.size(); j++) {
                    String queryInfoDate = queryInfoDateList.get(j);
                    List<String> list;
                    if (j > 0) {
                        list = productCodeMap.get(queryInfoDateList.get(j - 1));
                        if (list.isEmpty()) {
                            break;
                        }
                    } else {
                        list = new ArrayList<>();
                    }
                    List<String> productCodeList = productLogService.queryAnalyse(queryInfoDate, list);
                    if (productCodeList.isEmpty()) {
                        add = false;
                        break;
                    }
                    productCodeMap.put(queryInfoDate, productCodeList);
                }
                if (add) {
                    ProductAnalyseBo bo = new ProductAnalyseBo();
                    bo.setInfoDate(queryInfoDateList.getLast());
                    bo.setAnalyseNo(analyseNo);
                    bo.setAnalyseJson(JsonUtils.toJsonString(productCodeMap.get(bo.getInfoDate())));
                    productAnalyseService.insertByBo(bo);
                }
            }
        });
        return R.ok();
    }

    /**
     * 修改统计分析
     */
    @SaCheckPermission("live:productAnalyse:edit")
    @Log(title = "统计分析", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit() {
        ThreadUtil.execAsync(() -> {
            List<ProductAnalyseVo> productAnalyseVos = productAnalyseService.queryListByEdit();
            for (ProductAnalyseVo productAnalyseVo : productAnalyseVos) {
                // 刷新数据
                List<String> productCodeList = JsonUtils.parseArray(productAnalyseVo.getAnalyseJson(), String.class);
                if (null == productCodeList || productCodeList.isEmpty()) {
                    continue;
                }
                List<String> verifyProductCodeList = productLogService.queryAnalyseVerify(productAnalyseVo.getInfoDate(), productCodeList);
                if (null == verifyProductCodeList) {
                    continue;
                }
                ProductAnalyseBo bo = new ProductAnalyseBo();
                bo.setId(productAnalyseVo.getId());
                bo.setVerifyJson(JsonUtils.toJsonString(verifyProductCodeList));
                BigDecimal verifySize = new BigDecimal(verifyProductCodeList.size());
                BigDecimal productSize = new BigDecimal(productCodeList.size());
                bo.setAccuracy(verifySize.divide(productSize, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")));
                productAnalyseService.updateByBo(bo);
            }
        });
        return R.ok();
    }

    /**
     * 删除统计分析
     *
     * @param ids 主键串
     */
    @SaCheckPermission("live:productAnalyse:remove")
    @Log(title = "统计分析", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(productAnalyseService.deleteWithValidByIds(List.of(ids), true));
    }
}
