package com.liuyao.demo.utilutil.office;

import com.liuyao.demo.utils.DateUtil;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelUtil {


    /**
     * 导出Excel
     *
     * @param fileName
     *            默认导出Excel文件名（带后缀格式）
     * @param titles
     *            表标题：可传Null
     * @param titleList
     *            表头
     * @param tableList
     *            数据
     * @param response
     * @throws IOException
     */
    public static void exportExcelUtil(String fileName, String[] titles, List<String> titleList, List<String[]> tableList,
                                       HttpServletResponse response) {

        XSSFWorkbook wb = new XSSFWorkbook();

        XSSFSheet sheet = wb.createSheet(fileName.substring(0, fileName.lastIndexOf(".")));
        XSSFRow row = null;
        XSSFCell cell = null;
        CellStyle css = setTitleStyle(wb); // Excel标题样式
        CellStyle cs = setSimpleCellStyle(wb); // Excel单元格样式

        int row_index = 0;
        // 插入标题
        for(int i= 0;i<titles.length;i++){
            if (titles[i] != null && titles[i].trim().length() != 0) {
                row = sheet.createRow(row_index++);
                cell = row.createCell(0);
                cell.setCellValue(titles[i]);
                cell.setCellStyle(css);
            }
            if (titleList != null && titles[i] != null && titles[i].trim().length() != 0) {
                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, titleList.size() - 1));// 横向：合并第一行的第0列到第n列
            }
        }


        // 插入表头
        boolean bo = false;
        if (titleList != null) {
        	if (titleList.size()==16) {
				bo=true;
			}
            row = sheet.createRow(row_index++);
            for (int i = 0; i < titleList.size(); i++) {
                cell = row.createCell(i);
                cell.setCellValue(titleList.get(i).toString());

                cell.setCellStyle(css);
                // 自动适应列宽
                sheet.autoSizeColumn(i);
            }
        }
        // 插入数据
        for (String[] item : tableList) {
            row = sheet.createRow(row_index++); // 创建新的ROW，用于数据插入
            // 将对象数据插入到Excel中
            if (null == item || item.length == 0) {
                break;
            }
            // Cell赋值开始
            for (int i = 0; i < item.length; i++) {
                cell = row.createCell(i);
                cell.setCellValue(item[i]);
                if (bo) {
//					cs.setAlignment(HorizontalAlignment.CENTER);
					if (i==9) {
						sheet.setColumnWidth(i,50 * 256);
					}else if (i==3||i==4||i==11) {
						sheet.setColumnWidth(i,10 * 256);
					}else if (i==12||i==13||i==14||i==15) {
						sheet.setColumnWidth(i,20 * 256);
					} else {
						sheet.autoSizeColumn(i);
					}
					cell.setCellStyle(cs);
				}else {
					sheet.autoSizeColumn(i);
					cell.setCellStyle(cs);
				}
            }

        }

    	//合并
        if ((titleList.size()==4||titleList.size()==8)&&"评估项目".equals(titleList.get(0))) {
            int number = 1;
            for (int i = 1; i <= tableList.size(); i++) {
            	if (i==tableList.size()) {
            		if (number!=1) {
        				sheet.addMergedRegion(new CellRangeAddress((i+2)-number, i+1, 0,0));
					}
    				number=1;
				}else {
					if (tableList.get(i)[0].equals(tableList.get(i-1)[0])) {
	    				number++;
	    			}else {
	    				if (number!=1) {
	        				sheet.addMergedRegion(new CellRangeAddress((i+2)-number, i+1, 0,0));
						}
	    				number=1;
	    			}
				}
    			
    		}
		}
        if (titleList.size()==4&&"一级指标".equals(titleList.get(0))) {
            int ynumber = 1;
            int xnumber = 1;
            for (int i = 1; i <= tableList.size(); i++) {
            	if (i==tableList.size()) {
            		if (ynumber!=1) {
        				sheet.addMergedRegion(new CellRangeAddress((i+2)-ynumber, i+1, 0,0));
                		ynumber=1;
					}
            		if (xnumber!=1) {
        				sheet.addMergedRegion(new CellRangeAddress((i+2)-xnumber, i+1, 1,1));
        				xnumber=1;
					}
				}else {
					if (tableList.get(i)[0].equals(tableList.get(i-1)[0])) {
						ynumber++;
	    			}else {
	    				if (ynumber!=1) {
	        				sheet.addMergedRegion(new CellRangeAddress((i+2)-ynumber, i+1, 0,0));
						}
	    				ynumber=1;
	    			}
					if (tableList.get(i)[1].equals(tableList.get(i-1)[1])) {
						xnumber++;
	    			}else {
	    				if (xnumber!=1) {
	        				sheet.addMergedRegion(new CellRangeAddress((i+2)-xnumber, i+1, 1,1));
						}
	    				xnumber=1;
	    			}
				}
    			
    		}
		}
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream =new ByteArrayOutputStream();
            wb.write(byteArrayOutputStream);
            response.setContentType("application/octet-stream;charset=utf-8");
            fileName = response.encodeURL(new String(fileName.getBytes(),"iso8859-1"));			//保存的文件名,必须和页面编码一致,否则乱码
            response.addHeader("Content-Disposition",   "attachment;filename=" + fileName);
            response.setContentLength(byteArrayOutputStream.size());
            response.setHeader("Set-Cookie", "fileDownload=true; path=/");
            ServletOutputStream outputstream = response.getOutputStream();	//取得输出流
            byteArrayOutputStream.writeTo(outputstream);					//写到输出流
            byteArrayOutputStream.close();									//关闭
            outputstream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    /**
     * 导入工具类
     * @param multipartFile 文件
     * @param startRowNum 从第几行开始读 (Excel行号) >0
     */
    public static List<List<String>> importExcel(MultipartFile multipartFile, int startRowNum){
        String format = "yyyy-MM-dd HH:mm:ss";

        if (null == multipartFile){
            System.out.println("文件不存在");
            return null;
        }

        List<List<String>> list = new ArrayList<>();

        Workbook workbook = getWorkbook(multipartFile);
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

                    List<String> dataList = null;
                    StringBuilder rowSb = new StringBuilder();
                    for (int k = 0; k < lastCellNum; k++){ //遍历列 得到一行的所有数据
                        Cell cell = row.getCell(k);// 获得单元格
                        String cellValue = getCellValue(cell, format);//将单元格转换为字符串
                        rowSb.append(cellValue);
//                        if ("".equals(cellValue)) continue;
                        if (null == dataList) {
                            dataList = new ArrayList<>();
                            dataList.add(0,"0");
                        }
                        dataList.add(k+1, cellValue);
                    }
                    if (null == dataList) continue;
                    if ("".equals(rowSb.toString().trim())) continue;
                    list.add(dataList);
                }
            }
        }
//        for (int len = list.size(),i = len-1; i >= 0; i--) {
//            if (list.get(i).toString().trim().equals("")){
//                list.remove(i);
//            }
//        }
        return list;
    }

    /**
     * 获得工作表EXCEL
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
                workbook = new HSSFWorkbook(is);
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
        if (null == cell) return "";
        System.out.println(cell.toString());

//        if (!(CellType.NUMERIC.getCode() == cell.getCellType())) {
//            return cell.toString();
//        }
        if ((!HSSFDateUtil.isCellDateFormatted(cell))){
            return cell.toString();
        }
        Date date = cell.getDateCellValue();
        return DateUtil.format(date, format);
    }

    /**
     * 描述：设置表头样式
     *
     * @param workbook
     * @return
     */
    @SuppressWarnings("deprecation")
    public static CellStyle setTitleStyle(Workbook workbook) {
        CellStyle css = workbook.createCellStyle();

//        css.setBorderBottom(BorderStyle.THIN); // 下边框
//        css.setBorderLeft(BorderStyle.THIN);// 左边框
//        css.setBorderTop(BorderStyle.THIN);// 上边框
//        css.setBorderRight(BorderStyle.THIN);// 右边框
//        css.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
//        css.setAlignment(HorizontalAlignment.CENTER ); // 居中
        // 设置字体:
        Font font = workbook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 12);// 设置字体大小
        font.setBold(true);// 粗体显示
        css.setFont(font);
        return css;
    }

    /**
     * 描述：设置简单的Cell样式
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static CellStyle setSimpleCellStyle(Workbook wb) {
        CellStyle cs = wb.createCellStyle();

//        cs.setBorderBottom(BorderStyle.THIN); // 下边框
//        cs.setBorderLeft(BorderStyle.THIN);// 左边框
//        cs.setBorderTop(BorderStyle.THIN);// 上边框
//        cs.setBorderRight(BorderStyle.THIN);// 右边框
//        cs.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        cs.setWrapText(true);//自动换行
        XSSFDataFormat format =  (XSSFDataFormat) wb.createDataFormat();//设置文本格式样式
        cs.setDataFormat(format.getFormat("@"));
        return cs;
    }

}
