package com.liuyao.demo.utilutil.office.office;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelCreate {

    private HttpServletResponse response;
    private String fileName;
    private String encoding;
    private XSSFWorkbook workbook;
    private XSSFSheet lastSheet;//正在操作的最后一个
    private XSSFRow lastRow;//正在操作的最后一行
    private final float H_RATIO = 15f;//高度转换为像素比例
    private final int W_RATIO = 32;//宽度转换为像素比例

    public ExcelCreate(HttpServletResponse response, String fileName, int defaultHeight) {
        this(response, "utf-8", fileName, defaultHeight);
    }

    public ExcelCreate(HttpServletResponse response, String encoding, String fileName, int defaultHeight) {
        this(response, new XSSFWorkbook(), encoding, fileName);
        this.workbook = new XSSFWorkbook();
        createSeet(defaultHeight);
    }

    public ExcelCreate(HttpServletResponse response, XSSFWorkbook workbook,String encoding, String fileName) {
        this.response = response;
        this.workbook = workbook;
        this.encoding = encoding;
        this.fileName = fileName + ".xlsx";
    }

    public XSSFSheet createSeet(String sheetName){
        this.lastSheet = this.workbook.createSheet(sheetName);
        return this.lastSheet;
    }

    public XSSFSheet createSeet(int defaultHeight){
        this.lastSheet = this.workbook.createSheet();
        this.lastSheet.setDefaultRowHeight((short) (defaultHeight * H_RATIO));
        return this.lastSheet;
    }

    public XSSFSheet createSeet(){
        this.lastSheet = this.workbook.createSheet();
        return this.lastSheet;
    }

    public XSSFRow createRow(int index){
        this.lastRow = this.lastSheet.createRow(index);
        return this.lastRow;
    }

    /**
     * 最后位置追加行
     * @return
     */
    public XSSFRow appendRow(){
        int index = this.lastSheet.getLastRowNum();
        if (index == 0 && null == this.lastSheet.getRow(index)){
            index--;
        }
        this.lastRow = createRow(index+1);
        return this.lastRow;
    }

    /**
     * 最后位置追加行
     * @return
     */
    public XSSFRow appendRow(CellStyle style, String content){
        appendRow();
        appendCell(style,content);
        return this.lastRow;
    }

    /**
     * 最后位置追加行
     * @return
     */
    public XSSFRow appendRow(CellStyle style, double height, XSSFRichTextString content){
        appendRow();
        this.lastRow.setHeight((short) (height * H_RATIO));
        appendCell(style,content);
        return this.lastRow;
    }

    /**
     * 最后位置追加行
     * @return
     */
    public XSSFRow appendRow(CellStyle style, double height, String content){
        appendRow();
        this.lastRow.setHeight((short) (height * H_RATIO));
        appendCell(style,content);
        return this.lastRow;
    }

    /**
     * 最后位置追加单元格
     * @param content 内容
     * @param style 样式
     * @return
     */
    public XSSFCell appendCell(CellStyle style, String content){
        XSSFCell cell = appendCell();
        cell.setCellValue(content);
        if (null != style){
            cell.setCellStyle(style);
        }
        return cell;
    }

    public XSSFCell appendCell(CellStyle style, XSSFRichTextString content){
        XSSFCell cell = appendCell();
        cell.setCellValue(content);
        if (null != style){
            cell.setCellStyle(style);
        }
        return cell;
    }

    public XSSFCell appendCell(){
        return this.lastRow.createCell(this.lastRow.getLastCellNum() < 0 ? 0 : this.lastRow.getLastCellNum());
    }

    public Font createFont(){
        return this.workbook.createFont();
    }

    /**
     * 合并
     * 合并表格慢 https://blog.csdn.net/weixin_34205826/article/details/92100792
     */
    public void merge(int startRow, int endRow, int startCell, int endCell){
        this.lastSheet.addMergedRegionUnsafe(new CellRangeAddress(startRow, endRow, startCell, endCell));
//        this.lastSheet.addMergedRegion(new CellRangeAddress(startRow, endRow, startCell, endCell));
    }

    /**
     * 设置列宽
     * @param index 列下表
     * @param width 像素值
     */
    public void width(int index, int width){
        this.lastSheet.setColumnWidth(index, width * W_RATIO);
    }

    /**
     * 居中对齐样式
     */
    public XSSFCellStyle centerStyle(int borderSize, String fontStyle, int fontSize, boolean blod){
        return style(borderSize, fontStyle, fontSize, blod, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, null, null);
    }
    /**
     * 设置样式
     */
    public XSSFCellStyle style(int borderSize, String fontStyle, int fontSize, boolean blod,
                               int alignX, int alignY, boolean warp, int[] bkg, int[] color) {
        XSSFCellStyle css = this.workbook.createCellStyle();
        if (borderSize > 0){
            short bs = (short) borderSize;
            css.setBorderBottom(bs); // 下边框
            css.setBorderLeft(bs);// 左边框
            css.setBorderTop(bs);// 上边框
            css.setBorderRight(bs);// 右边框
        }
        if (alignY > 0) css.setVerticalAlignment((short) alignY);//垂直居中
        if (alignX > 0) css.setAlignment((short) alignX); // 水平居中
        css.setWrapText(warp);//自动换行
        // 设置字体:
        XSSFFont font = workbook.createFont();
        if (null != fontStyle && !"".equals(fontStyle)) font.setFontName(fontStyle); //设置字体样式
        if (fontSize > 4) font.setFontHeightInPoints((short) fontSize);// 设置字体大小
        if (null != color) font.setColor(new XSSFColor(new java.awt.Color(color[0],color[1],color[2])));//字体颜色
        font.setBold(blod);// 粗体显示
        css.setFont(font);
        if (null != bkg){ //表格背景色
            css.setFillForegroundColor(new XSSFColor(new java.awt.Color(bkg[0],bkg[1],bkg[2])));
            css.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }

//        XSSFDataFormat format =  (XSSFDataFormat) workbook.createDataFormat();//设置文本格式样式
//        css.setDataFormat(format.getFormat("@"));
        return css;
    }


    public void saveFile(){
        try {
            setRespHeader();
            OutputStream ops = this.response.getOutputStream();
            this.workbook.write(ops);
            ops.flush();
            ops.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setRespHeader() throws UnsupportedEncodingException {
        String fname = new String(this.fileName.getBytes(this.encoding),"iso-8859-1");
        this.fileName = this.response.encodeURL(new String(this.fileName.getBytes(),this.encoding));
        this.response.setContentType("application/octet-stream;charset=" + this.encoding);
        response.setHeader("Content-Disposition", "attachment;filename="+ this.fileName);
        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
    }

    /**
     * 解析方法
     *
     * @param
     * @return
     * @throws Exception
     */
    public  static List<String[]> getExcel(String filePath) throws Exception {
        // 创建对Excel工作簿文件的引用
        boolean isExcel2003 = filePath.toLowerCase().endsWith("xls") ? true : false;
        System.out.println(isExcel2003);
        Workbook workbook = null;
        if (isExcel2003) {
            workbook = new HSSFWorkbook(new FileInputStream(new File(filePath)));
        } else {
            workbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));
        }
        // 在Excel文档中，第一张工作表的缺省索引是0
        // 其语句为：
        // HSSFSheet sheet = wookbook.getSheetAt(0);
        Sheet sheet = workbook.getSheet("Sheet1");
        // 获取到Excel文件中的所有行数
        int rows = sheet.getPhysicalNumberOfRows();
        System.out.println("rows =" + rows);
        // 遍历行
        List<String[]> list_excel = new ArrayList<String[]>();
        for (int i = 1; i <= rows; i++) {
            // 读取左上端单元格
            Row row = sheet.getRow(i);
            // 行不为空
            if (row != null) {
                // 获取到Excel文件中的所有的列
                int cells = row.getPhysicalNumberOfCells();
                System.out.println("cells =" + cells);
                String value = "";
                // 遍历列
                for (int j = 0; j <= cells; j++) {
                    // 获取到列的值
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_FORMULA:
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                value += cell.getNumericCellValue() + ",";
                                break;
                            case Cell.CELL_TYPE_STRING:
                                value += cell.getStringCellValue() + ",";
                                break;
                            default:
                                value += "0";
                                break;
                        }
                    }
                }
                String[] val = value.split(",");
                list_excel.add(val);
            }
        }
        return list_excel;
    }

    private final static String excel2003L=".xls";//2003以及之前的excel版本
    private final static String excel2007U=".xlsx";//2007以及以后的excell版本
    public static SimpleDateFormat sFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static short[] yyyyMMdd = {14, 31, 57, 58, 179, 184, 185, 186, 187, 188};
    public static short[] HHmmss = {20, 32, 190, 191, 192};
    static List<short[]> yyyyMMddList = Arrays.asList(yyyyMMdd);
    static List<short[]> hhMMssList = Arrays.asList(HHmmss);
    /**
     * 描述：获取IO流中的数据，组装成List<List<Object>>对象
     * @param in,fileName
     * @return
     * @throws
     */
    public  List<List<String>> getBankListByExcel(InputStream in, String fileName) throws Exception{
        List<List<String>> list = null;
        //创建Excel工作薄
        Workbook work = this.getWorkbook(in,fileName);
        if(work==null){
            throw new Exception("创建Excel为空");
        }
        Sheet sheet=null;
        Row row=null;
        Cell cell=null;
        list=new ArrayList<List<String>>();
        //遍历Excel中所有的sheet
        for (int i=0;i<work.getNumberOfSheets();i++){
            sheet=work.getSheetAt(i);
            if(sheet==null){continue;}
            int totalCell = sheet.getRow(0).getPhysicalNumberOfCells();//标题行一共有多少列
            //遍历当前sheet中的所有行
            for (int j = sheet.getFirstRowNum(); j < sheet.getLastRowNum()+1; j++) {
                row = sheet.getRow(j);
                if(row==null || validateRow(row) || row.getPhysicalNumberOfCells() < totalCell){continue;} //3个条件，有一个为true就不会往list里加，不仅过滤空行还过滤了列数不够的行，这点要注意，要求表中前后的列都是必填的。
                //遍历所有的列
                List<String> li = new ArrayList<String>();
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    li.add(this.getCellData(cell));
                }
                list.add(li);
            }
            // 简单起见，这里只解析第一个工作簿！
            break;
        }
        work.close();
        return list;
    }
    // 过滤空行，（其中一行的数据的确都为空，可是其原本的格式还在，并没有连带删除，这样计算出来的行数就不真实，比真实的大）
    private boolean validateRow(Row row) throws Exception{
//      for (Cell cell : row) {
//
//      }
        //只判断第一列。第一列为空就代表这行的数据无效
        if (row.getCell(0).getCellType() == Cell.CELL_TYPE_BLANK || "".equals(this.getCellData(row.getCell(0)))) {
            return true;
        }
        return false;//不是空行
    }
    /**
     * 描述：根据文件后缀，自适应上传文件的版本
     * @param inStr,fileName
     * @return
     * @throws Exception
     */
    public  Workbook getWorkbook(InputStream inStr,String fileType) throws Exception{
        Workbook wb = null;
        if(excel2003L.equals(fileType)){
            wb = new HSSFWorkbook(inStr);  //2003-
        }else if(excel2007U.equals(fileType)){
            wb = new XSSFWorkbook(inStr);  //2007+
        }else{
            throw new Exception("解析的文件格式有误！");
        }
        return wb;
    }

    /**
     * 获取单元中值(字符串类型)
     *
     * @param cell
     * @return
     * @throws Exception
     */
    public String getCellData(Cell cell) throws Exception {
        String cellValue = "";
        if (cell != null) {
            try {
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_BLANK://空白
                        cellValue = "";
                        break;
                    case Cell.CELL_TYPE_NUMERIC: //数值型 0----日期类型也是数值型的一种
                        if (DateUtil.isCellDateFormatted(cell)) {
                            short format = cell.getCellStyle().getDataFormat();

                            if (yyyyMMddList.contains(format)) {
                                sFormat = new SimpleDateFormat("yyyy-MM-dd");
                            } else if (hhMMssList.contains(format)) {
                                sFormat = new SimpleDateFormat("HH:mm:ss");
                            }
                            Date date = cell.getDateCellValue();
                            cellValue = sFormat.format(date);
                        } else {
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            cellValue = replaceBlank(cell.getStringCellValue());
                            //Double numberDate = new BigDecimal(cell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//似乎还是有点问题
                            //cellValue = numberDate + "";
                        }
                        break;
                    case Cell.CELL_TYPE_STRING: //字符串型 1
                        cellValue = replaceBlank(cell.getStringCellValue());
                        break;
                    case Cell.CELL_TYPE_FORMULA: //公式型 2
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cellValue = replaceBlank(cell.getStringCellValue());
                        break;
                    case Cell.CELL_TYPE_BOOLEAN: //布尔型 4
                        cellValue = String.valueOf(cell.getBooleanCellValue());
                        break;
                    case Cell.CELL_TYPE_ERROR: //错误 5
                        cellValue = "!#REF!";
                        break;
                }
            } catch (Exception e) {
                throw new Exception("读取Excel单元格数据出错：" + e.getMessage());
            }
        }
        return cellValue;
    }

    public static String replaceBlank(String source) {
        String dest = "";
        if (source != null) {
            Pattern p = Pattern.compile("\t|\r|\n");
            Matcher m = p.matcher(source);
            dest = m.replaceAll("");
        }
        return dest.trim();
    }

}
