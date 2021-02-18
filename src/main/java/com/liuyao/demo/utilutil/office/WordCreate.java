package com.liuyao.demo.utilutil.office;

import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.*;

import javax.xml.bind.JAXBElement;
import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class WordCreate {

    private static ObjectFactory factory = Context.getWmlObjectFactory();
    private WordprocessingMLPackage wordMLPackage;
    private String folder;
    private String fileName;

    public WordCreate(String folder,String fileName) throws InvalidFormatException {
        this.folder = folder;
        this.fileName = fileName;
        this.wordMLPackage = WordprocessingMLPackage.createPackage();
    }

    //创建文件夹
    private void createFile() throws Docx4JException {
        //创建文件夹
        File targetFile = new File(this.folder);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        wordMLPackage.save(new File(folder+"/"+fileName));
    }

    /**
     * 获得模板并添加
     */
    public Tbl addFromModel(String path) throws Docx4JException {
        this.wordMLPackage = getModel(path);
        List<Object> tbs = getElements(this.wordMLPackage.getMainDocumentPart(),Tbl.class);
        return tbs.size() < 1 ? factory.createTbl() : (Tbl) tbs.get(tbs.size()-1);
    }

    /**
     * 获得模板数据
     */
    public WordprocessingMLPackage getModel(String filePath) throws Docx4JException {
        File file = new File(filePath);
        return WordprocessingMLPackage.load(file);
    }

    /**
     * 创建表格
     */
    public Tbl createTable(){
        Tbl table = factory.createTbl();
        wordMLPackage.getMainDocumentPart().addObject(table);
        return table;
    }

    /**
     * 创建表格行
     */
    public Tr createTr(Tbl tbl){
        Tr tr = factory.createTr();
        tbl.getContent().add(tr);
        return tr;
    }

    /**
     * 创建单元格
     */
    public Tc createTc(Tr tr){
        Tc tc = factory.createTc();
        tr.getContent().add(tc);
        return tc;
    }
    public Tc createTc(Tr tr, List<String> cons){
        Tc tc = factory.createTc();
        tr.getContent().add(tc);
        for (int i = 0; i < cons.size(); i++) {
            tc.getContent().add(wordMLPackage.getMainDocumentPart().createParagraphOfText(cons.get(i)));
        }
        return tc;
    }
    public Tc createTc(Tr tr, String content){
        Tc tc = factory.createTc();
        tr.getContent().add(tc);
        tc.getContent().add(wordMLPackage.getMainDocumentPart().createParagraphOfText(content));
        return tc;
    }

    /**
     * 创建段落
     */
    public P createP(Tc tc, String content, RPr rPr){
        P para = factory.createP();
        if (content != null) {
            Text t = factory.createText();
            t.setValue(content);
            R run = factory.createR();
            run.setRPr(rPr);
            run.getContent().add(t);
            para.getContent().add(run);
        }
        tc.getContent().add(para);
        return para;
    }

    /**
     * 替换占位符
     */
    public void replace(P p, String oldStr, String newStr){
        String content = getText(p).replace("${"+oldStr+"}",newStr);
        List<Object> rs = getElements(p,R.class);
        R r = (R) rs.get(0);
        if (r.getContent().get(0) instanceof JAXBElement &&
                ((JAXBElement) r.getContent().get(0)).getValue() instanceof Text){
            ((Text)((JAXBElement) r.getContent().get(0)).getValue()).setValue(content);
        }
        p.getContent().clear();
        p.getContent().add(r);
    }

    /**
     * 获得P中的文本
     */
    public String getText(P p){
        StringBuilder result = new StringBuilder();
        List<Object> texts = getElements(p, Text.class);
        for (int i = 0; i < texts.size(); i++) {
            result.append(((Text) texts.get(i)).getValue());
        }
        return result.toString();
    }

    /**
     * 获得所有对象 递归实现
     * @param obj 从哪
     * @param toSearch 查询的类型
     * @return
     */
    public static List<Object> getElements(Object obj, Class<?> toSearch) {
        List<Object> result = new ArrayList<Object>();
        if (obj instanceof JAXBElement) obj = ((JAXBElement<?>) obj).getValue();

        if (obj.getClass().equals(toSearch)){
            result.add(obj);
        } else if (obj instanceof ContentAccessor) {
            List<Object> children = ((ContentAccessor) obj).getContent();
            for (Object child : children) {
                result.addAll(getElements(child, toSearch));
            }
        }
        return result;
    }

    /**
     * 横向合并
     * @param tr 要合并的行
     * @param start 从第start开始 1，2，3，。。。
     * @param count 跨几列
     */
    public void colspan(Tr tr, int start, int count){
        start--;
        int end = start + count - 1;
        List<Object> tcList = getElements(tr, Tc.class);
        for (int i = start, len = Math.min(tcList.size() - 1, end); i <= len; i++) {
            Tc tc = (Tc) tcList.get(i);
            TcPr tcPr = getTcPr(tc);
            TcPrInner.HMerge hMerge = tcPr.getHMerge();
            if (hMerge == null) {
                hMerge = new TcPrInner.HMerge();
                tcPr.setHMerge(hMerge);
            }
            if (i == start) {
                hMerge.setVal("restart");
            } else {
                hMerge.setVal("continue");
            }
        }
    }

    /**
     * 纵向合并
     * @param trs 要合并的行的集合
     * @param start 集合中开始下标 0，1，2，。。。
     * @param count 跨几行
     * @param y 第y列 1，2，3，。。。
     */
    public void rowspan(List<Object> trs, int startIndex, int count, int y){
        if (startIndex < 0 || count < 2 || y < 1) {
            return;
        }
        List<Object> tcs;
        for (int i = 0; i < Math.min(count, trs.size()-startIndex); i++) {
            tcs = getElements(trs.get(startIndex+i), Tc.class);
            if (tcs.size() < y) {break;}
            Tc tc = (Tc) tcs.get(y-1);
            TcPr tcPr = getTcPr(tc);
            TcPrInner.VMerge vMerge = tcPr.getVMerge();
            if (vMerge == null) {
                vMerge = new TcPrInner.VMerge();
                tcPr.setVMerge(vMerge);
            }
            if (i == 0) {
                vMerge.setVal("restart");
            } else {
                vMerge.setVal("continue");
            }
        }
    }

    private TcPr getTcPr(Tc tc) {
        TcPr tcPr = tc.getTcPr();
        if (tcPr == null) {
            tcPr = new TcPr();
            tc.setTcPr(tcPr);
        }
        return tcPr;
    }

    /**
     * 设置单元格垂直对齐方式  对齐需在合并后进行，否则将被覆盖
     */
    public Tc setTcVAlign(Tc tc, STVerticalJc vAlignType) {
        if (vAlignType != null) {
            TcPr tcPr = getTcPr(tc);
            CTVerticalJc vAlign = new CTVerticalJc();
            vAlign.setVal(vAlignType);
            tcPr.setVAlign(vAlign);
        }
        return tc;
    }

    /**
     * 行垂直对齐
     */
    public Tr setTcVAlign(Tr tr, STVerticalJc vAlignType) {
        List<Object> tcs = getElements(tr, Tc.class);
        for (Object tc : tcs) {
            setTcVAlign((Tc) tc, vAlignType);
        }
        return tr;
    }

    /**
     * 单元格水平对齐
     */
    public Tc setTcJcAlign(Tc tc, JcEnumeration jcType) {
        if (jcType != null) {
            List<Object> pList = getElements(tc, P.class);
            for (Object p : pList) {
                setParaJcAlign((P) p, jcType);
            }
        }
        return tc;
    }

    /**
     * 行水平对齐
     */
    public Tr setTcJcAlign(Tr tr, JcEnumeration jcType) {
        List<Object> tcs = getElements(tr, Tc.class);
        for (Object tc : tcs) {
            setTcJcAlign((Tc) tc, jcType);
        }
        return tr;
    }

    /**
     * 段落水平对齐
     */
    public P setParaJcAlign(P paragraph, JcEnumeration hAlign) {
        if (hAlign != null) {
            PPr pprop = paragraph.getPPr();
            if (pprop == null) {
                pprop = new PPr();
                paragraph.setPPr(pprop);
            }
            Jc align = new Jc();
            align.setVal(hAlign);
            pprop.setJc(align);
        }
        return paragraph;
    }


    /**
     *
     * @param obj 添加样式的位置
     * @param blod 加粗
     * @param fontSize 大小
     * @param fontColor 颜色
     * @param fontStyle 字体
     * @param ind 缩进
     */
    public  void addStyle(Object obj, boolean blod, int fontSize, String fontColor, String fontStyle, int ind){
        List<Object> rs = getElements(obj, R.class);
        for (Object o : rs) {
            ((R) o).setRPr(getRpr(blod,fontSize,fontColor, fontStyle));
        }
        List<Object> ps = getElements(obj, P.class);
        for (Object o : ps) {
            ((P) o).setPPr(getPpr(ind));
        }
    }

    /**
     * 字体样式
     * @param blod 加粗
     * @param fontSize 尺寸
     * @param fontColor 颜色
     * @param fontStyle 字体
     * @return
     */
    public RPr getRpr(boolean blod, int fontSize, String fontColor, String fontStyle){
        RPr rPr = factory.createRPr();
        if (fontSize > 4){
            //大小
            HpsMeasure size = new HpsMeasure();
            size.setVal(new BigInteger(String.valueOf(fontSize)));
            rPr.setSz(size);
            rPr.setSzCs(size);
        }
        if (null != fontStyle && fontStyle.trim().length() > 1){
            //字体
            RFonts style = new RFonts();
            style.setAscii(fontStyle);
            style.setHAnsi(fontStyle);
            rPr.setRFonts(style);
        }
        if (null != fontColor && fontColor.trim().length() > 1){
            //颜色
            Color color = new Color();
            color.setVal(fontColor);
            rPr.setColor(color);
        }
        //加粗
        if (blod){
            BooleanDefaultTrue b = new BooleanDefaultTrue();
            b.setVal(true);
            rPr.setB(b);
        }
        return rPr;
    }

    /**
     * 设置段落样式
     * @param ind 缩进
     * @return
     */
    public PPr getPpr(int ind){
        PPr pPr = factory.createPPr();
        if (ind > 0){
            //缩进
            PPrBase.Ind ind1 = new PPrBase.Ind();
            ind1.setFirstLineChars(new BigInteger(String.valueOf(ind)));
            pPr.setInd(ind1);
        }
        return pPr;
    }

    /**
         *
         */
    public static void main(String[] args) throws Docx4JException {
//        WordCreate wordCreate = new WordCreate("C:/Users/61/Desktop/doc/","aaa.doc");
//        Tbl table = wordCreate.addFromModel("C:/Users/61/Desktop/doc/report.xml");
//
//        List<Object> trs = new ArrayList<>();
//        Tr tr = wordCreate.createTr(table);
//        wordCreate.createTc(tr,"组长");
//        wordCreate.createTc(tr,"张三");
//        wordCreate.createTc(tr,"张三");
//        wordCreate.createTc(tr,"AAA111");
//        wordCreate.colspan(tr, 2,2);
//        trs.add(tr);
//        Tr tr1 = wordCreate.createTr(table);
//        wordCreate.createTc(tr1, "职能");
//        wordCreate.createTc(tr1, "职能");
//        wordCreate.createTc(tr1, "做饭");
//        wordCreate.createTc(tr1, "做饭");
////        wordCreate.colspan(tr1, 1,2);
////        wordCreate.colspan(tr1, 3,2);
//        trs.add(tr1);
//        Tr tr2 = wordCreate.createTr(table);
//        wordCreate.createTc(tr2, "职能");
//        wordCreate.createTc(tr2, "职能");
//        wordCreate.createTc(tr2, "做饭");
//        wordCreate.createTc(tr2, "做饭");
////        wordCreate.colspan(tr1, 1,2);
////        wordCreate.colspan(tr1, 3,2);
//        trs.add(tr2);
//        Tr tr3 = wordCreate.createTr(table);
//        wordCreate.createTc(tr3, "职能");
//        wordCreate.createTc(tr3, "职能");
//        wordCreate.createTc(tr3, "做饭");
//        wordCreate.createTc(tr3, "做饭");
////        wordCreate.colspan(tr1, 1,2);
////        wordCreate.colspan(tr1, 3,2);
//        trs.add(tr3);
//        wordCreate.rowspan(trs,0,3,1);
//        wordCreate.rowspan(trs,1,2,2);
//        wordCreate.rowspan(trs,2,2,3);
//        wordCreate.setTcVAlign(tr,STVerticalJc.CENTER);
//        wordCreate.setTcJcAlign(tr,JcEnumeration.CENTER);
//        wordCreate.createFile();

        int i = 0;
        System.out.println(++i);
        System.out.println(i);
        System.out.println(new BigInteger("12"));
        System.out.println("============");

    }

}
