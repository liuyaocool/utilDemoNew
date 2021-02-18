package com.liuyao.demo.ws.test;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class HeaderWs {


    public static byte[] KEY_64_2 = { 42, 16, 93, 56, 78, 41, (byte) 218, (byte) 223};//公钥 Byte[]数组：
    public static byte[] IV_64_2 = { 55, 103, 46, 79, 36, 89, (byte) 167, 31 };//私钥 Byte[]数组：

    /**
     * 单笔短信推送请求地址
     */
    private final static String singleUrl = "http://localhost:8080/untitled3/services/HelloWorld";

    /**
     * 批量短信推送请求地址
     */
    private final static String batUrl = "http://localhost:8080/sms/smsReceiveAction_batReceiveSms.action";

    public static void main(String[] args) throws UnsupportedEncodingException {

        doPostSingle();
    }

    /**
     *单笔请求方法
     * @return
     */
    public static void doPostSingle() throws UnsupportedEncodingException {
        String url = "http://sdsapi.nrcc.com.cn/DataExtractService.asmx/GetIdentityList";

        String method = "GetIdentityList";
        String soapAction = "http://tempuri.org/";
        String contentType = "text/xml";
        String encoding = "utf-8";
        String soap = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Header>\n" +
                "    <AuthorSoapHeader xmlns=\"http://tempuri.org/\">\n" +
                "      <Secretkey>string</Secretkey>\n" +
                "    </AuthorSoapHeader>\n" +
                "  </soap:Header>\n" +
                "  <soap:Body>\n" +
                "    <GetChemComponentData xmlns=\"http://tempuri.org/\">\n" +
                "      <searchModel>\n" +
                "        <IDEN_DATA_ID>guid</IDEN_DATA_ID>\n" +
                "        <BASE_BRANCH_MANAGE_ID>guid</BASE_BRANCH_MANAGE_ID>\n" +
                "        <BASE_VERSION_MANAGE_ID>guid</BASE_VERSION_MANAGE_ID>\n" +
                "        <CHEM_NAME>string</CHEM_NAME>\n" +
                "        <CHEM_NAME_VAGUE>string</CHEM_NAME_VAGUE>\n" +
                "        <CHEM_CAS>string</CHEM_CAS>\n" +
                "        <CHEM_SHAPE>string</CHEM_SHAPE>\n" +
                "        <CHEM_CATEGORY>string</CHEM_CATEGORY>\n" +
                "        <CHEM_ALIAS>string</CHEM_ALIAS>\n" +
                "        <IDEN_ANNEX_ID>guid</IDEN_ANNEX_ID>\n" +
                "      </searchModel>\n" +
                "    </GetChemComponentData>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        List<NameValuePair> pairs = new ArrayList<>();

        pairs.add(new BasicNameValuePair("AuthorSoapHeader",
                "+eYpFtf5PPQs6we48ch4Hdq5ar0s/sb67O4BbgRqt/3Qgxh91AbXe1XnbnR7qqVgjRWoJG152Kc="));
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", contentType);
            httpPost.setHeader("SOAPAction", soapAction);
            CloseableHttpResponse response = null;
            try {
                StringEntity s = new StringEntity("");
                s.setContentEncoding(encoding);
                s.setContentType(contentType);//发送json数据需要设置contentType

//                httpPost.setEntity(s);
                httpPost.setEntity(new UrlEncodedFormEntity(pairs,"UTF-8"));

                response = client.execute(httpPost);
                System.out.println(response.getStatusLine());
                HttpEntity entity = response.getEntity();
                if(entity!=null){
                    if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                        String result = EntityUtils.toString(response.getEntity());// 返回json格式：
                        System.out.println(result);

                        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                        DocumentBuilder db = dbf.newDocumentBuilder();
                        StringReader sr = new StringReader(result);
                        InputSource is = new InputSource(sr);
                        Document document = db.parse(is);
                        Element root = document.getDocumentElement();
                        NodeList nodelist_return = root.getElementsByTagName("fromReturn");
                        String returnCont = nodelist_return.item(0).getTextContent();
                        //打印返回信息
                        System.out.println("webservice返回信息："+returnCont);

//                        res = JSONObject.fromObject(returnCont);
//                        System.out.println(res.toString());
                    }
                }
                EntityUtils.consume(entity);

            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
