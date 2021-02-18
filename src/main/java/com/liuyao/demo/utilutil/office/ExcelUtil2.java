package com.liuyao.demo.utilutil.office;

import com.liuyao.demo.utils.DateUtil;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * 文件工具类
 */
public class ExcelUtil2 {

    public static boolean isExcel2003(String filePath)
    {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    public static boolean isExcel2007(String filePath)
    {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }


    public static List<Map<String, String>> importExcel(MultipartFile multipartFile, Class clazz, Map<Integer, String> columns, String format){

        if (null == multipartFile){
            System.out.println("文件不存在");
            return null;
        }

        List<Map<String, String>> list = new ArrayList<>();

        Workbook workbook = ExcelUtil2.getWorkbook(multipartFile);
        if (null != workbook){
            for (int i = 0; i < workbook.getNumberOfSheets(); i++){

                Sheet sheet = workbook.getSheetAt(i);//获得sheet
                if (null == sheet) continue;
                int firstRowNum = sheet.getFirstRowNum();//有效数据第一行的行号
                int lastRowNum = sheet.getLastRowNum();//有效数据最后一行的行号
                for (int j = firstRowNum; j <= lastRowNum; j++){// 遍历行 得到所有行
                    Row row = sheet.getRow(j); //获得行
                    if (null == row) continue;
                    int firstCellNum = row.getFirstCellNum(); //有效数据第一列的列号
                    int lastCellNum = row.getLastCellNum(); //有效数据最后一列的列号

                    Map<String, String> map = new HashMap<>();
                    for (int k = firstCellNum; k < lastCellNum; k++){ //遍历列 得到一行的所有数据
                        Cell cell = row.getCell(k);// 获得单元格
                        if (null == cell) continue;
                        if (columns.keySet().contains(k)) {
                            map.put(columns.get(k), ExcelUtil2.getCellValue(cell, format));
                        }else {
                            map.put(String.valueOf(k), ExcelUtil2.getCellValue(cell, format));
                        }
                    }
                    list.add(map);
                }
            }
        }

//        String className = clazz.getName();
//        System.out.println("==============" + className);
//        //获得属性的所有信息
//        Field[] fields = clazz.getDeclaredFields();
//        for (Field f: fields) {
//            //获得属性名
//            String fstr = f.getName();
//            System.out.print("属性类型:" + f.getGenericType().toString());
//            System.out.println("属性名:" + fstr);
//        }
//
//        try {
//            //通过反射创建对象
//            Object object = Class.forName(className).newInstance();
//            //通过反射给属性赋值
//            for (Integer column: columnMap.keySet()) {
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        return new ArrayList<Map<Integer, String>>();
        return list;
    }

    /**
     * 导入工具类
     * @param multipartFile 文件
     * @param columns   列映射 map = {1: "name"; ......; 6: "age"} 第一列对应name字段, 第六列对应年龄(Excel中)
     * @param format 文件中涉及到的日期格式
     * @param startRowNum 从第几行开始读 (Excel行号) >0
     * @return 遍历List中Map的时候需要判断是否包含key
     */
    public static List<Map<String, String>> importExcel(MultipartFile multipartFile, Map<Integer, String> columns, String format, int startRowNum){

        if (null == multipartFile){
            System.out.println("文件不存在");
            return null;
        }

        List<Map<String, String>> list = new ArrayList<>();

        Workbook workbook = getWorkbook(multipartFile);
        if (null != workbook){
            for (int i = 0; i < workbook.getNumberOfSheets(); i++){

                Sheet sheet = workbook.getSheetAt(i);//获得sheet
                if (null == sheet) continue;
                int firstRowNum = sheet.getFirstRowNum();//有效数据第一行的行号
                int lastRowNum = sheet.getLastRowNum();//有效数据最后一行的行号

                startRowNum = startRowNum < 0 ? firstRowNum : startRowNum - 1; //设置开始行
                for (int j = startRowNum; j <= lastRowNum; j++){// 遍历行 得到所有行
                    Row row = sheet.getRow(j); //获得行
                    if (null == row) continue;
                    int firstCellNum = row.getFirstCellNum(); //有效数据第一列的列号
                    int lastCellNum = row.getLastCellNum(); //有效数据最后一列的列号

                    Map<String, String> map = null;
                    for (int k = firstCellNum; k < lastCellNum; k++){ //遍历列 得到一行的所有数据
                        Cell cell = row.getCell(k);// 获得单元格
                        if (null == cell) continue;
                        String cellValue = getCellValue(cell, format);
                        if ("".equals(cellValue)) continue;
                        if (null == map) map = new HashMap<>();
                        if (columns.keySet().contains(k + 1)) {
                            map.put(columns.get(k + 1), cellValue);
                        }else {
                            map.put(String.valueOf(k +1), cellValue);
                        }
                    }
                    if (null == map) continue;
                    list.add(map);
                }
            }
        }
        return list;
    }

    /**
     * 获得工作表
     */
    public static Workbook getWorkbook(MultipartFile multipartFile){

        //获得文件名
        String fileName = multipartFile.getOriginalFilename();

        if (!fileName.endsWith("xls") && !fileName.endsWith("xlsx")){
            System.out.println("文件不是Excel格式");
            return null;
        }
        Workbook workbook = null;

        try{
            //获得文件io流
            InputStream is = multipartFile.getInputStream();

            if (fileName.endsWith("xlsx")){
                workbook = new XSSFWorkbook(is);
            } else if (fileName.endsWith("xls")){
                workbook = new HSSFWorkbook();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return workbook;
    }

    /**
     *  获得单元格数据
     */
    public static String getCellValue(Cell cell, String format) {
        System.out.println(cell.toString());

//        if (!(CellType.NUMERIC == cell.getCellTypeEnum())) {
//            return cell.toString();
//        }
         if ((!HSSFDateUtil.isCellDateFormatted(cell))){
            return cell.toString();
        }
        Date date = cell.getDateCellValue();
        return DateUtil.format(date, format);
    }

    /**
     * 解析word
     */
    public static String getWordBook(MultipartFile multipartFile){

        if (multipartFile.isEmpty()) return null;

        //获得文件名
        String fileName = multipartFile.getOriginalFilename();

        if (!fileName.endsWith("doc") && !fileName.endsWith("docx")){
            System.out.println("文件不是Word格式");
            return null;
        }

        try{
            //获得文件io流
            InputStream is = multipartFile.getInputStream();
//            InputStream is = new FileInputStream(new File(filePath));

            if (fileName.endsWith("doc")){
                HWPFDocument document = new HWPFDocument(is); // poi-scratchpad-3.16.jar
                WordExtractor word = new WordExtractor(document); // poi-scratchpad-3.16.jar

                StringBuilder wordText = new StringBuilder();
                for (String ss:word.getParagraphText()) {
                    wordText.append(ss);
                }
                return wordText.toString();
//                return word.getText();
            } else if (fileName.endsWith("docx")){
//                OPCPackage opcPackage = POIXMLDocument.openPackage(filePath);
//                POIXMLTextExtractor word = new XWPFWordExtractor(opcPackage);
//                POIXMLTextExtractor extractor = new XWPFWordExtractor(is);
//                WordExtractor word = new HSSFWorkbook(is)


//                XWPFDocument document = new XWPFDocument(is); // poi-ooxml-3.16.jar
//                XWPFWordExtractor word = new XWPFWordExtractor(document); // poi-ooxml-3.16.jar
//                return word.getText();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
