package com.liuyao.demo.utilutil.utils;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 林存河
 * 2018/7/10
 */
public class ExportExcelUtil {
    private String title; // 导出表格的表名

    private String[] rowName;// 导出表格的列名

    private List<Object[]> dataList = new ArrayList<Object[]>(); // 对象数组的List集合

    private HttpServletResponse response;

    private HttpServletRequest request;
     @Autowired
     private HorizontalAlignment align;

//
//    /**
//     * 实例化导出类
//     * @param title  导出表格的表名，最好是英文，中文可能出现乱码
//     * @param rowName 导出表格的列名数组
//     * @param dataList 对象数组的List集合
//     * @param response
//     */
//    public ExportExcelUtil(String title,String[] rowName,List<Object[]>  dataList, HttpServletRequest request, HttpServletResponse  response){
//        this.title=title;
//        this.rowName=rowName;
//        this.dataList=dataList;
//        this.response = response;
//        this.request = request;
//    }
//
//    // 导出数据
//    public void exportData() throws Exception{
//        HSSFWorkbook workbook =new HSSFWorkbook(); // 创建一个excel对象
//        HSSFSheet sheet =workbook.createSheet(title); // 创建表格
//
//        //sheet.setDefaultRowHeightInPoints(18.5f);
//
//        // sheet样式定义
//        HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook,16); // 头样式
//        HSSFCellStyle columnStyle = this.getColumnStyle(workbook,14); // 标题样式
//        HSSFCellStyle style = this.getStyle(workbook,11);  // 单元格样式
//
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (rowName.length-1)));// 合并第一行的所有列
//        // 产生表格标题行
//        HSSFRow rowm  =sheet.createRow(0);  // 行
//        rowm.setHeightInPoints(26f);
//        HSSFCell cellTiltle =rowm.createCell(0);  // 单元格
//
//        cellTiltle.setCellStyle(columnTopStyle);
//        cellTiltle.setCellValue(title);
//
//        int columnNum = rowName.length;  // 表格列的长度
//        HSSFRow rowRowName = sheet.createRow(1);  // 在第二行创建行
//        HSSFCellStyle cells =workbook.createCellStyle();
//        cells.setBottomBorderColor(HSSFColor.BLACK.index);
//        rowRowName.setRowStyle(cells);
//
//        // 循环 将列名放进去
//        for (int i = 0; i < columnNum; i++) {
//            HSSFCell cellRowName = rowRowName.createCell(i);
//            cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING); // 单元格类型
//            HSSFRichTextString text = new HSSFRichTextString(rowName[i]);  // 得到列的值
//            cellRowName.setCellValue(text); // 设置列的值
//            cellRowName.setCellStyle(columnStyle); // 样式
//        }
//
//        // 将查询到的数据设置到对应的单元格中
//        for (int i = 0; i < dataList.size(); i++) {
//            Object[] obj = dataList.get(i);//遍历每个对象
//            HSSFRow row = sheet.createRow(i+2);//创建所需的行数
//            for (int j = 0; j < obj.length; j++) {
//                HSSFCell  cell = null;   //设置单元格的数据类型
//                if(j==0){
//                    // 第一列设置为序号
//                    cell = row.createCell(j,HSSFCell.CELL_TYPE_NUMERIC);
//                    cell.setCellValue(i+1);
//                }else{
//                    cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);
//                    if(!"".equals(obj[j]) && obj[j] != null){
//                        cell.setCellValue(obj[j].toString());  //设置单元格的值
//                    }else{
//                        cell.setCellValue("  ");
//                    }
//                }
//                cell.setCellStyle(style); // 样式
//            }
//        }
//
//        //  让列宽随着导出的列长自动适应，但是对中文支持不是很好  也可能在linux（无图形环境的操作系统）下报错，报错再说
//        for (int i = 0; i < columnNum; i++) {
//            sheet.autoSizeColumn(i);
//            sheet.setColumnWidth(i, sheet.getColumnWidth(i)+888);//适当再宽点
//        }
//
//        if(workbook !=null){
//            // 输出到服务器上
////          FileOutputStream fileOutputStream = new FileOutputStream("D:/user.xls");
////          workbook.write(fileOutputStream);//将数据写出去
////          fileOutputStream.close();//关闭输出流
//            // 输出到用户浏览器上
//            OutputStream out = response.getOutputStream();
//            try {
//                // excel 表文件名
//                String fileName = title + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + ".xls";
//                String fileName11 = "";
//                String userAgent = request.getHeader("USER-AGENT");
//                if(StringUtils.contains(userAgent, "Firefox") || StringUtils.contains(userAgent, "firefox")){//火狐浏览器
//                    fileName11 = new String(fileName.getBytes(), "ISO8859-1");
//                }else{
//                    fileName11 = URLEncoder.encode(fileName,"UTF-8");//其他浏览器
//                }
//                String headStr = "attachment; filename=\"" + fileName11 + "\"";
//                response.setContentType("APPLICATION/OCTET-STREAM");
//                response.setCharacterEncoding("UTF-8");
//                response.setHeader("Content-Disposition", headStr);
//                workbook.write(out);
//                out.flush();
//                workbook.close();
//            } catch (Exception e) {
//                throw e;
//            } finally {
//                if (null != out) {
//                    out.close();
//                }
//            }
//        }
//    }
//
//    public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook,int fontSize) {
//        // 设置字体
//        HSSFFont font = workbook.createFont();
//        //设置字体大小
//        font.setFontHeightInPoints((short)fontSize);
//        //字体加粗
//        font.setBold(true);
//        //设置字体名字
//        font.setFontName("宋体");
//        //设置样式;
//        HSSFCellStyle style = workbook.createCellStyle();
//        //在样式用应用设置的字体;
//        style.setFont(font);
//        //设置自动换行;
//        style.setWrapText(false);
//        //设置水平对齐的样式为居中对齐;
//        style.setAlignment(HorizontalAlignment.CENTER);
//        //设置垂直对齐的样式为居中对齐;
//        style.setVerticalAlignment(VerticalAlignment.CENTER);
//        return style;
//    }
//
//    public HSSFCellStyle getColumnStyle(HSSFWorkbook workbook,int fontSize) {
//        // 设置字体
//        HSSFFont font = workbook.createFont();
//        //设置字体大小
//        font.setFontHeightInPoints((short)fontSize);
//        //字体加粗
//        font.setBold(true);
//        //设置字体名字
//        font.setFontName("宋体");
//        //设置样式;
//        HSSFCellStyle style = workbook.createCellStyle();
//        //设置底边框;
//        style.setBorderBottom(BorderStyle.THIN);
//        //设置底边框颜色;
//        style.setBottomBorderColor(HSSFColor.BLACK.index);
//        //设置左边框;
//        style.setBorderLeft(BorderStyle.THIN);
//        //设置左边框颜色;
//        style.setLeftBorderColor(HSSFColor.BLACK.index);
//        //设置右边框;
//        style.setBorderRight(BorderStyle.THIN);
//        //设置右边框颜色;
//        style.setRightBorderColor(HSSFColor.BLACK.index);
//        //设置顶边框;
//        style.setBorderTop(BorderStyle.THIN);
//        //设置顶边框颜色;
//        style.setTopBorderColor(HSSFColor.BLACK.index);
//        //在样式用应用设置的字体;
//        style.setFont(font);
//        //设置自动换行;
//        style.setWrapText(false);
//        //设置水平对齐的样式为居中对齐;
//        style.setAlignment(HorizontalAlignment.CENTER);
//        //设置垂直对齐的样式为居中对齐;
//        style.setVerticalAlignment(VerticalAlignment.CENTER);
//        return style;
//    }
//
//    public HSSFCellStyle getStyle(HSSFWorkbook workbook,int fontSize) {
//        //设置字体
//        HSSFFont font = workbook.createFont();
//        //设置字体大小
//        font.setFontHeightInPoints((short)fontSize);
//        //字体加粗
//        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//        //设置字体名字
//        font.setFontName("宋体");
//        //设置样式;
//        HSSFCellStyle style = workbook.createCellStyle();
//        //设置底边框;
//        style.setBorderBottom(BorderStyle.THIN);
//        //设置底边框颜色;
//        style.setBottomBorderColor(HSSFColor.BLACK.index);
//        //设置左边框;
//        style.setBorderLeft(BorderStyle.THIN);
//        //设置左边框颜色;
//        style.setLeftBorderColor(HSSFColor.BLACK.index);
//        //设置右边框;
//        style.setBorderRight(BorderStyle.THIN);
//        //设置右边框颜色;
//        style.setRightBorderColor(HSSFColor.BLACK.index);
//        //设置顶边框;
//        style.setBorderTop(BorderStyle.THIN);
//        //设置顶边框颜色;
//        style.setTopBorderColor(HSSFColor.BLACK.index);
//        //在样式用应用设置的字体;
//        style.setFont(font);
//        //设置自动换行;
//        style.setWrapText(false);
//        //设置水平对齐的样式为居中对齐;
//        style.setAlignment(HorizontalAlignment.CENTER);
//        //设置垂直对齐的样式为居中对齐;
//        style.setVerticalAlignment(VerticalAlignment.CENTER);
//
//        return style;
//    }
}

