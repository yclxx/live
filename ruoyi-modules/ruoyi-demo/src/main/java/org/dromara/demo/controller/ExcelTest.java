package org.dromara.demo.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.excel.convert.ExcelBigNumberConvert;
import org.dromara.demo.controller.poi.MyCellStyleStrategy;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiexi
 * @description
 * @date 2024/11/2 13:00
 */
@Slf4j
public class ExcelTest {

//    public static void main(String[] args) throws Exception {
//        String templatePath = "excel/单列表.xlsx";
//        File file = new File("D:\\excel\\test.xlsx");
//        File file2 = new File("D:\\excel\\tes2.xlsx");
//        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
//            Map<String, String> map = new HashMap<>();
//            map.put("title", "单列表多数据");
//            map.put("test1", "数据测试1");
//            map.put("test2", "数据测试2");
//            map.put("test3", "数据测试3");
//            map.put("test4", "数据测试4");
//            map.put("testTest", "666");
//            List<TestExcelController.TestObj> list = new ArrayList<>();
//            list.add(new TestExcelController.TestObj("单列表测试1", "列表测试1", "列表测试2", "列表测试3", "列表测试4"));
//            list.add(new TestExcelController.TestObj("单列表测试2", "列表测试5", "列表测试6", "列表测试7", "列表测试8"));
//            list.add(new TestExcelController.TestObj("单列表测试3", "列表测试9", "列表测试10", "列表测试11", "列表测试12"));
//
//            ArrayList<Object> arrayList = CollUtil.newArrayList(map, list);
//
//            ClassPathResource templateResource = new ClassPathResource(templatePath);
//            ExcelWriter excelWriter = EasyExcel.write(fileOutputStream)
//                    .withTemplate(templateResource.getStream())
//                    .autoCloseStream(false)
//                    // 大数值自动转换 防止失真
//                    .registerConverter(new ExcelBigNumberConvert())
//                    .build();
//            WriteSheet writeSheet = EasyExcel.writerSheet().build();
//
//            // 单表多数据导出 模板格式为 {.属性}
//            for (Object d : arrayList) {
//                excelWriter.fill(d, writeSheet);
//            }
//            excelWriter.finish();
//        }
//
//        // 读取excel
//        try (InputStream in = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(in)) {
//            Sheet sheet = workbook.getSheetAt(0);
//            for (Row row : sheet) {
//                for (Cell cell : row) {
//                    if ("数据测试1".equals(cell.getStringCellValue())) {
//                        CellStyle cellStyle = workbook.createCellStyle();
//                        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//                        cellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
//
//                        cell.setCellStyle(cellStyle);
//                    }
//                }
//            }
//            try (OutputStream out = new FileOutputStream(file2)) {
//                workbook.write(out);
//            }
//        }
//    }

    public static void main(String[] args) throws Exception {
        String templatePath = "excel/单列表.xlsx";
        File file = new File("D:\\excel\\test.xlsx");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            Map<String, String> map = new HashMap<>();
            map.put("title", "单列表多数据");
            map.put("test1", "数据测试1");
            map.put("test2", "数据测试2");
            map.put("test3", "数据测试3");
            map.put("test4", "数据测试4");
            map.put("testTest", "666");
            List<TestExcelController.TestObj> list = new ArrayList<>();
            list.add(new TestExcelController.TestObj("单列表测试1", "列表测试1", "列表测试2", "列表测试3", "列表测试4"));
            list.add(new TestExcelController.TestObj("单列表测试2", "列表测试5", "列表测试6", "列表测试7", "列表测试8"));
            list.add(new TestExcelController.TestObj("单列表测试3", "列表测试9", "列表测试10", "列表测试11", "列表测试12"));

            ArrayList<Object> arrayList = CollUtil.newArrayList(map, list);
            // 内容的策略
//            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
//            // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
//            contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
//            // 背景绿色
//            contentWriteCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
            // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
//            MyCellStyleStrategy myCellStyleStrategy =
//                    new MyCellStyleStrategy();
            ClassPathResource templateResource = new ClassPathResource(templatePath);
            ExcelWriter excelWriter = EasyExcel.write(fileOutputStream)
                    .withTemplate(templateResource.getStream())
                    .autoCloseStream(false)
                    .registerWriteHandler(new MyCellStyleStrategy())
                    // 大数值自动转换 防止失真
                    .registerConverter(new ExcelBigNumberConvert())
                    .build();

//            Class<? extends ExcelWriter> aClass = excelWriter.getClass();
//            Field declaredField = aClass.getDeclaredField("excelBuilder");
//            declaredField.setAccessible(true); // 设置属性为可访问
//            ExcelBuilder excelBuilder = (ExcelBuilder) declaredField.get(excelWriter);
//            Class<? extends ExcelBuilder> excelBuilderClass = excelBuilder.getClass();
//            Field excelWriteFillExecutor = excelBuilderClass.getDeclaredField("excelWriteFillExecutor");
//            excelWriteFillExecutor.setAccessible(true);
//            // 覆盖原来的excelWriteFillExecutor
//            MyExcelWriteFillExecutor myExcelWriteFillExecutor = new MyExcelWriteFillExecutor(excelWriter.writeContext());
//            excelWriteFillExecutor.set(excelBuilder, myExcelWriteFillExecutor);

            WriteSheet writeSheet = EasyExcel.writerSheet().build();

            // 单表多数据导出 模板格式为 {.属性}
            for (Object d : arrayList) {
//                if(d instanceof TestExcelController.TestObj){
//                    log.info("{}", d);
//                    Workbook workbook = excelWriter.writeContext().getWorkbook();
//                    // 设置单元格样式
//                    CellStyle cellStyle = workbook.createCellStyle();
//                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//                    cellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
//                    myExcelWriteFillExecutor.setCellStyle(cellStyle);
//                }
                excelWriter.fill(d, writeSheet);
            }
            excelWriter.finish();
        }

//        // 读取excel
//        try (InputStream in = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(in)) {
//            Sheet sheet = workbook.getSheetAt(0);
//            for (Row row : sheet) {
//                for (Cell cell : row) {
//                    if ("数据测试1".equals(cell.getStringCellValue())) {
//                        CellStyle cellStyle = workbook.createCellStyle();
//                        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//                        cellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
//
//                        cell.setCellStyle(cellStyle);
//                    }
//                }
//            }
//            try (OutputStream out = new FileOutputStream(file2)) {
//                workbook.write(out);
//            }
//        }
    }
}
