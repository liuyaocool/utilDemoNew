package com.liuyao.demo.utilutil.office;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

/**
 * 文件工具类
 */
public class ExceUtil3 {

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

            if (fileName.endsWith("doc")){

                HWPFDocument document = new HWPFDocument(is);
                WordExtractor word = new WordExtractor(document);
                return word.getText();

            } else if (fileName.endsWith("docx")){

                XWPFDocument document = new XWPFDocument(is);
                XWPFWordExtractor word = new XWPFWordExtractor(document);
//                return word.getText();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 导入工具类
     * @param multipartFile 文件
     * @param columns   列映射 map = {1: "name"; ......; 6: "age"} 第一列对应name字段, 第六列对应年龄(Excel中)
     * @param format 表格中涉及到的日期格式 "yyyy-MM-dd HH:mm:ss"
     * @param startRowNum 从第几行开始读 (Excel行号) >0
     */
    public static List<Map<String, String>> importExcel(MultipartFile multipartFile, Map<Integer, String> columns, String format, int startRowNum){

        if (null == multipartFile){
            System.out.println("文件不存在");
            return null;
        }

        List<Map<String, String>> list = new ArrayList<>();

        Workbook workbook = ExcelUtil.getWorkbook(multipartFile);
        if (null != workbook){
            for (int i = 0; i < workbook.getNumberOfSheets(); i++){

                Sheet sheet = workbook.getSheetAt(i);//获得sheet
                if (null == sheet) continue;
                int firstRowNum = sheet.getFirstRowNum();//有效数据第一行的行号
                int lastRowNum = sheet.getLastRowNum();//有效数据最后一行的行号

                startRowNum = startRowNum < 1 ? firstRowNum : startRowNum - 1; //设置开始行
                for (int j = startRowNum; j <= lastRowNum; j++){// 遍历行 得到所有行
                    Row row = sheet.getRow(j); //获得行
                    if (null == row) continue;
                    int firstCellNum = row.getFirstCellNum(); //有效数据第一列的列号
                    int lastCellNum = row.getLastCellNum(); //有效数据最后一列的列号

                    Map<String, String> map = null;
                    for (int k = firstCellNum; k < lastCellNum; k++){ //遍历列 得到一行的所有数据
                        Cell cell = row.getCell(k);// 获得单元格
                        if (null == cell) continue;
                        String cellValue = ExcelUtil.getCellValue(cell, format);
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
}