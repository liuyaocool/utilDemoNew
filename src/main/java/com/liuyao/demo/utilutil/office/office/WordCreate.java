package com.liuyao.demo.utilutil.office.office;

import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.*;
import org.jvnet.jaxb2_commons.ppp.Child;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBElement;
import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.*;

public class WordCreate {

    public static ObjectFactory factory = Context.getWmlObjectFactory();
    private WordprocessingMLPackage wordMLPackage;
    private String folder;//保存的文件夹
    private String fileName;//保存的文件名
    private Tbl tbl;//模板最后一个表格
    private Map<String, Tr> $map;//所有占位符所在行
    private final String JAR_STATIC_PATH = "/BOOT-INF/classes/static/";// jar包中静态资源的路径

    public WordCreate() { }

    public WordCreate(String folder, String fileName, String modelPath) throws Docx4JException {
        this.folder = folder;
        this.fileName = fileName;
        this.wordMLPackage = getModel(modelPath);
        this.tbl = getLastTbl();
        this.$map = new HashMap<>();
        List<Object> trs = getElements(getTbl(), Tr.class);
        List<Object> ps;
        String content;
        for (Object o: trs) {
            ps = getElements(o, P.class);
            for (Object po :ps) {
                content = getText((P) po);
                if (content.contains("${") && content.contains("}")){
                    content = content.substring(content.indexOf("${")+2,content.indexOf("}"));
                    this.$map.put(content,(Tr) o);
                }
            }
        }
    }

    //创建文件
    public String createFile() throws Docx4JException {
        //创建文件夹
        File targetFile = new File(this.folder);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        String date = "_yyyyMMddHHmmss";
//        String date = "_"+DateFormatUtil.getDateString(new Date(), "yyyyMMddHHmmss");
        if (this.fileName.contains(".docx")){
            this.fileName = this.fileName.replace(".docx", date+".docx");
        } else if (this.fileName.contains(".doc")){
            this.fileName = this.fileName.replace(".doc", date+".doc");
        } else {
            this.fileName = this.fileName.replace(".", date+".");
        }
        //保存文件
        wordMLPackage.save(new File(this.folder+"/"+this.fileName));
        return (this.folder+"/"+this.fileName).replace("//","/");
    }

    //下载文件
    public void downLoadWord(HttpServletRequest request, HttpServletResponse response){
        File file = new File(this.folder+"/"+this.fileName);
//        FileUtils.downFile(file, request, response, this.fileName);
    }

    /**
    * 根据格式类型转换doc文件
    *
    * @param descPath the docx path 目标文件
    * @return the file
    * @throws Exception the exception
    * @author Harley Hong
    * @created 2017 /08/09 16:14:07 Convert docx 2 doc file.
    * */
    public File convertDocFmt(String srcPath, String descPath, int fmt) throws Exception {
        // 实例化ComThread线程与ActiveXComponent
//        ComThread.InitSTA();
//        ActiveXComponent app = new ActiveXComponent("Word.Application");
//        try {
//            // 文档隐藏时进行应用操作
//            app.setProperty("Visible", new Variant(false));
//            // 实例化模板Document对象
//            Dispatch document = app.getProperty("Documents").toDispatch();
//            // 打开Document进行另存为操作
//            Dispatch doc = Dispatch.invoke(document, "Open", Dispatch.Method,
//                    new Object[] { srcPath, new Variant(true), new Variant(true) }, new int[1]).toDispatch();
//            Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] { descPath, new Variant(fmt) }, new int[1]);
//            Dispatch.call(doc, "Close", new Variant(false));
            return new File(descPath);
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            // 释放线程与ActiveXComponent
//            app.invoke("Quit", new Variant[] {});
//            ComThread.Release();
//        }
    }

    /**
     * 获得模板数据
     */
    public WordprocessingMLPackage getModel(String filePath) throws Docx4JException {
        File file = new File(filePath);
        if (file.exists()){
            return WordprocessingMLPackage.load(file);
        }
        InputStream in = this.getClass().getResourceAsStream(JAR_STATIC_PATH + "files/model/"+filePath);
        return WordprocessingMLPackage.load(in);
    }

    /**
     * 获得最后一个tbl
     */
    private Tbl getLastTbl() throws Docx4JException {
        List<Object> tbs = getElements(this.wordMLPackage.getMainDocumentPart(), Tbl.class);
        return tbs.size() < 1 ? createTbl() : (Tbl) tbs.get(tbs.size()-1);
    }

    /**
     * 获得最后一个tbl
     */
    public Tbl getLastTbl(String path) throws Docx4JException {
        WordprocessingMLPackage work = WordprocessingMLPackage.load(new File(path));
        List<Object> tbs = getElements(work.getMainDocumentPart(), Tbl.class);
        return tbs.size() < 1 ? null : (Tbl) tbs.get(tbs.size()-1);
    }

    /**
     * 拼接模板
     */
    public void appendTable(Tbl tbl, String path,int index) throws Docx4JException {
        WordprocessingMLPackage word = getModel(path);
        List<Object> trs = getElements(word.getMainDocumentPart(), Tr.class);
        for (int i = 1; i < trs.size(); i++) {
            tbl.getContent().add(index,trs.get(i));
            index ++;
        }
    }

    //新建表格
    public Tbl createTbl(){
        Tbl tbl = factory.createTbl();
        this.wordMLPackage.getMainDocumentPart().addObject(tbl);
        return tbl;
    }

    /**
     * 创建表格行
     */
    public Tr createTr(int index){
        return addTr(this.tbl, factory.createTr(), index);
    }

    /**
     * 创建表格行
     */
    public Tr createTr(Tbl tbl,int index){
        return addTr(tbl, factory.createTr(), index);
    }

    /**
     * 创建表格行
     */
    public Tr addTr(Tbl tbl, Tr tr, int index){
        if (index < 0) {
            tbl.getContent().add(tr);
        } else {
            tbl.getContent().add(index, tr);
        }
        return tr;
    }

    /**
     * 创建单元格
     */
    public Tc createTc(String content){
        Tc tc = factory.createTc();
        createP(tc, content);
        return tc;
    }
    public Tc createTc(Tr tr){
        Tc tc = factory.createTc();
        tr.getContent().add(tc);
        return tc;
    }
    public Tc createTc(Tr tr, List<String> cons){
        Tc tc = factory.createTc();
        tr.getContent().add(tc);
        for (int i = 0; i < cons.size(); i++) {
            createP(tc, cons.get(i));
        }
        return tc;
    }
    public Tc createTc(Tr tr, String content){
        Tc tc = factory.createTc();
        tr.getContent().add(tc);
        createP(tc, content);
        return tc;
    }

    /**
     * 创建段落
     */
    public P createP(Tc tc){
        P para = createP(tc,null);
        return para;
    }
    public P createP(Tc tc, String content){
        P para = createP(content);
        tc.getContent().add(para);
        return para;
    }
    public P createP(String content){
        P para = factory.createP();
        if (content != null && !"".equals(content)) {
            createText(para, content);
        }
        return para;
    }

    /**
     * 创建文本
     * @param content
     * @return
     */
    public Text createText(P para, String content){
        Text t = factory.createText();
        t.setValue(content);
        R run = factory.createR();
        run.getContent().add(t);
        para.getContent().add(run);
        return t;
    }

    /**
     * 追加文本到段落
     * @param p
     * @param content
     * @return
     */
    public P appendToP(P p, String content){
        List<Object> texts = getElements(p, Text.class);
        Text text;
        if (null != content && !"".equals(content)){
            if (texts.size() < 1 ){
                text = createText(p, content);
            } else {
                text = (Text) texts.get(texts.size()-1);
                text.setValue(text.getValue() + content);
            }
        }
        return p;
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
     * 替换占位符 会将段落中的所有样式设置为第一个Text的样式
     */
    public P replace(P p, String oldStr, String newStr){
        oldStr = "${"+oldStr+"}";
        String content = getText(p);
        if (!content.contains(oldStr)){
            return p;
        }
        content = content.replace(oldStr,newStr);
        //获得R 与 Text
        List<Object> rs = getElements(p,R.class);
        R r = null;
        Text text = null;
        List<Object> texts;
        for (int i = 0; i < rs.size(); i++) {
            texts = getElements(rs.get(i), Text.class);
            if (texts.size() > 0){
                r = (R) rs.get(i);
                text = (Text) texts.get(0);
                break;
            }
        }
        if (null != text){
            text.setValue(content);
        }
        p.getContent().clear();
        p.getContent().add(r);
        return p;
    }

    /**
     * 替换占位符
     */
    public Tr replace(Tr tr, String oldStr, String newStr){
        List<Object> ps = getElements(tr, P.class);
        for (int i = 0; i < ps.size(); i++) {
            P p = (P) ps.get(i);
            replaceP(p,oldStr, newStr);
        }
        return tr;
    }

    /**
     * 获得P中的所有文本
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
     * 替换P中占位符所在的文本对象 仅替换${}的内容
     */
    public P replaceP(P p, String oldStr, String newStr){
        List<Object> allText = getElements(p, Text.class);
        StringBuilder textSb = new StringBuilder();
        String midText; Text text = null;
        boolean start = false;
        for (int i = 0; i < allText.size(); i++) {
            midText = ((Text) allText.get(i)).getValue();
            if (midText.contains("$")){
                textSb.append(midText);
                text = (Text) allText.get(i);
                start = true; continue;
            }
            if (start){
                textSb.append(midText);
                ((Text) allText.get(i)).setValue("");
            }
            if (midText.contains("}")){ break; }
        }
        if (start){
            midText = textSb.toString().replace("${"+oldStr+"}", newStr);
            text.setValue(midText);
        }
        return p;
    }

    /**
     * 获得所有对象 递归实现
     * @param obj 从哪
     * @param toSearch 查询的类型
     * @return
     */
    public List<Object> getElements(Object obj, Class<?> toSearch) {
        List<Object> result = new ArrayList<Object>();
        if (obj instanceof JAXBElement) obj = ((JAXBElement<?>) obj).getValue();

        if (obj.getClass().equals(toSearch)){
            result.add(obj);
        } else if (obj instanceof ContentAccessor) {
            List<Object> children = ((ContentAccessor) obj).getContent();
            for (int i = 0; i < children.size(); i++) {
                result.addAll(getElements(children.get(i), toSearch));
            }
        }
        return result;
    }

    /**
     * 获得父级  BUG tc没有父级
     */
    private static Object getParent(Child obj, Class toSearch) {
        if (obj.getClass().equals(toSearch)){
            return obj;
        } else if (obj.getParent() instanceof Child){
            return getParent((Child) obj.getParent(), toSearch);
        }
        return null;
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
     * @param startIndex 集合中开始下标 0，1，2，。。。
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

    /**
     * 单元格对齐
     * @param tc
     * @param vAlignType 垂直
     * @param jcType 水平
     * @return
     */
    public Tc setAlign(Tc tc, STVerticalJc vAlignType, JcEnumeration jcType){
        setVAlign(tc, vAlignType);
        setJcAlign(tc, jcType);
        return tc;
    }

    /**
     * 行对齐方式
     * @param tr
     * @param vAlignType
     * @param jcType
     * @return
     */
    public Tr setAlign(Tr tr, STVerticalJc vAlignType, JcEnumeration jcType){
        List<Object> tcs = getElements(tr, Tc.class);
        for (Object tc : tcs) {
            setAlign((Tc) tc, vAlignType, jcType);
        }
        return tr;
    }

    /**
     * 设置单元格垂直对齐方式  对齐需在合并后进行，否则将被覆盖
     */
    public Tc setVAlign(Tc tc, STVerticalJc vAlignType) {
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
    public Tr setVAlign(Tr tr, STVerticalJc vAlignType) {
        List<Object> tcs = getElements(tr, Tc.class);
        for (Object tc : tcs) {
            setVAlign((Tc) tc, vAlignType);
        }
        return tr;
    }

    /**
     * 单元格水平对齐
     */
    public Tc setJcAlign(Tc tc, JcEnumeration jcType) {
        if (jcType != null) {
            List<Object> pList = getElements(tc, P.class);
            for (Object p : pList) {
                setJcAlign((P) p, jcType);
            }
        }
        return tc;
    }

    /**
     * 行水平对齐
     */
    public Tr setJcAlign(Tr tr, JcEnumeration jcType) {
        List<Object> tcs = getElements(tr, Tc.class);
        for (Object tc : tcs) {
            setJcAlign((Tc) tc, jcType);
        }
        return tr;
    }

    /**
     * 段落水平对齐
     */
    public P setJcAlign(P paragraph, JcEnumeration hAlign) {
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

    private TcPr getTcPr(Tc tc) {
        TcPr tcPr = tc.getTcPr();
        if (tcPr == null) {
            tcPr = new TcPr();
            tc.setTcPr(tcPr);
        }
        return tcPr;
    }

    //获得占位符所在表格
    public Tbl gte$Tbl(String str){
        return (Tbl) this.$map.get(str).getParent();
    }

    //获得占位符所在位置
    public int get$Index(String str){
        return getTbl().getContent().indexOf(get$map().get(str));
    }

    //删除行
    public void removeTr(int index){
        getTbl().getContent().remove(index);
    };

    public Tbl getTbl() {
        return this.tbl;
    }

    public Map<String, Tr> get$map() {
        return this.$map;
    }

}
