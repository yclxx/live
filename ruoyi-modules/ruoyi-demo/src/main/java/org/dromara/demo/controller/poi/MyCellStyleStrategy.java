package org.dromara.demo.controller.poi;

import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.AbstractCellStyleStrategy;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.List;

/**
 * @author xiexi
 * @description
 * @date 2024/11/2 18:49
 */
@Slf4j
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class MyCellStyleStrategy extends AbstractCellStyleStrategy {

    private WriteCellStyle headWriteCellStyle;
    private List<WriteCellStyle> contentWriteCellStyleList;

    public MyCellStyleStrategy() {
    }

    public MyCellStyleStrategy(WriteCellStyle contentWriteCellStyle) {
        if (contentWriteCellStyle != null) {
            this.contentWriteCellStyleList = ListUtils.newArrayList(contentWriteCellStyle);
        }
    }

    @Override
    protected void setHeadCellStyle(CellWriteHandlerContext context) {
        if (stopProcessing(context) || headWriteCellStyle == null) {
            return;
        }
        WriteCellData<?> cellData = context.getFirstCellData();
        WriteCellStyle.merge(headWriteCellStyle, cellData.getOrCreateStyle());
    }

    @Override
    protected void setContentCellStyle(CellWriteHandlerContext context) {
//        if (stopProcessing(context) || CollectionUtils.isEmpty(contentWriteCellStyleList)) {
//            return;
//        }
//        Cell cell = context.getCell();
//        if (null != cell) {
//            CellStyle cellStyle = cell.getCellStyle();
//            if (null == cellStyle) {
//                cellStyle = context.getWriteContext().getWorkbook().createCellStyle();
//            }
//            String stringCellValue = cell.getStringCellValue();
//            if ("数据测试1".equals(stringCellValue)) {
//                // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
//                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//                // 背景绿色
//                cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
//                cell.setCellStyle(cellStyle);
//                return;
//            } else {
//
//            }
//        }
        if (stopProcessing(context)) {
            return;
        }

        WriteCellData<?> cellData = context.getFirstCellData();
        // 判断数据，根据数据设置样式 此处可定义一个map，事先配置好样式，然后根据map获取样式
        String str = cellData.getStringValue();
        WriteCellStyle writeCellStyle = null;
        if ("数据测试1".equals(str)) {
            writeCellStyle = new WriteCellStyle();
            // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
            writeCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
            // 背景红色
            writeCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        } else if ("列表测试6".equals(str)) {
            writeCellStyle = new WriteCellStyle();
            // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
            writeCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
            // 背景绿色
            writeCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        }

        if (context.getRelativeRowIndex() == null || context.getRelativeRowIndex() <= 0) {
            if (null != writeCellStyle) {
                WriteCellStyle.merge(writeCellStyle, cellData.getOrCreateStyle());
            } else if (!CollectionUtils.isEmpty(contentWriteCellStyleList)) {
                WriteCellStyle.merge(contentWriteCellStyleList.get(0), cellData.getOrCreateStyle());
            }
        } else {
            if (null != writeCellStyle) {
                WriteCellStyle.merge(writeCellStyle, cellData.getOrCreateStyle());
            } else if (!CollectionUtils.isEmpty(contentWriteCellStyleList)) {
                WriteCellStyle.merge(contentWriteCellStyleList.get(context.getRelativeRowIndex() % contentWriteCellStyleList.size()),
                        cellData.getOrCreateStyle());
            }
        }
    }

    protected boolean stopProcessing(CellWriteHandlerContext context) {
        return context.getFirstCellData() == null;
    }
}
