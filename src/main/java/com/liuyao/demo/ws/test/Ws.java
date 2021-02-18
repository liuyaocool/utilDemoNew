package com.liuyao.demo.ws.test;

import com.liuyao.demo.utils.LogUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ws {

    /**
     * 单笔请求方法
     * @return
     */
    public static String doPostSingle(String url, Map<String, String> header,
                                      String contentType, String encoding, String wsdl){
        LogUtil.info("开始调用接口：" + url);
        CloseableHttpClient client = HttpClients.createDefault();
        String result = null;
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity s;
            if (null == wsdl){
                s = new StringEntity("");
            } else {
                s = new StringEntity(wsdl);
            }
            if (null != header){
                for (String key : header.keySet()) {
                    httpPost.setHeader(key, header.get(key));
                }
            }
            if (null != contentType){
                httpPost.setHeader("Content-type", contentType);
                s.setContentType(contentType);
            }
            if (null != encoding){
                s.setContentEncoding(encoding);
            }
            httpPost.setEntity(s);
            response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if(entity!=null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                result = EntityUtils.toString(entity);
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { response.close(); } catch (IOException e) { e.printStackTrace(); }
            try { client.close(); } catch (IOException e) { e.printStackTrace(); }
        }
        LogUtil.info("调用接口成功，即将返回数据。");
        return result;
    }

    //通过文件获得文档
    public static Document getDocumentByFile(String filePath) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        return builder.build(new File(filePath));
    }

    //通过xml字符串获得文档
    public static Document getDocumentByStr(String xml) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(new StringReader(xml));
        return doc;
    }

    //从Doc对象集合中获得数据
    public static List<Map<String, String>> gatherData(List<Element> elements){
        List<Map<String, String>> data = new ArrayList<>();
        for (int j = 0; j < elements.size(); j++) {
            data.add(gatherData(elements.get(j)));
        }
        return data;
    }

    //从Doc对象集合中获得数据
    public static Map<String, String> gatherData(Element element){
        List<Element> elements = element.getChildren();
        Map<String, String> data = new HashMap<>();
        for (int j = 0; j < elements.size(); j++) {
            data.put(elements.get(j).getName(), elements.get(j).getValue());
        }
        return data;
    }

    /**
     * 通过标签名获得列表
     * @param doc 支持List<Element> Document Element
     * @param tagName
     * @return
     */
    public static List<Element> listByTagName(Object doc, String tagName){
        List<Element> elements = null;
        if (null == doc || null == tagName || "".equals(tagName.trim())){
            return new ArrayList<>();
        }else if (doc instanceof Document){
            elements = ((Document) doc).getRootElement().getChildren();
        } else if (doc instanceof Element){
            elements = ((Element) doc).getChildren();
        } else if (doc instanceof List){
            List a = (List) doc;
            if (a.size() > 0 && a.get(0) instanceof Element){
                elements = (List) doc;
            } else {
                return new ArrayList<>();
            }
        } else {

        }

        List<Element> result = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
            if (tagName.equals(elements.get(i).getName())){
                result.add(elements.get(i));
            } else {
                result.addAll(listByTagName(elements.get(i), tagName));
            }
        }
        return result;
    }

    public static void main(String[] args) {
        String[] methodNames = {"GetProject","GeProjectPlan","GetEmployee","GetEnterprise"};

        String url = "http://172.23.125.250:8980/common/testIp";
        String xmlStr =  doPostSingle(url, null, null, null, null);
        Document document = null;
        try {
            document = getDocumentByStr(xmlStr);
            List<Map<String, String>> data = gatherData(document.getRootElement().getChildren());

        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
