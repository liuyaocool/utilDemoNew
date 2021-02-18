package com.liuyao.demo.ws.client;

import com.liuyao.demo.ws.User;
import com.liuyao.demo.ws.test.Ws;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class SoapConncet {

    private String url = "http://172.23.121.147:8081/DataExtractService.asmx";
    private String host = "172.23.121.147";
    private String contentType = "text/xml; charset=utf-8";
    private String nameSpace = "http://tempuri.org/"; // SOAPAction = nameSpace+method

    /*
     * 远程访问SOAP协议接口
     *
     * @param url： 服务接口地址"http://192.168.0.120:8222/HelloWorld?wsdl"
     * @param isClass：接口类名
     * @param isMethod： 接口方法名
     * @param sendSoapString： soap协议xml格式访问接口
     *
     * @return  soap协议xml格式
     *
     * @备注：有四种请求头格式1、SOAP 1.1； 2、SOAP 1.2 ； 3、HTTP GET； 4、HTTP POST
     * 参考---》http://www.webxml.com.cn/WebServices/WeatherWebService.asmx?op=getWeatherbyCityName
     */
    public String fire(String method,String soap) throws IOException {
        if (soap == null) {
            return null;
        }
        URL soapUrl = new URL(this.url);
        URLConnection conn = soapUrl.openConnection();
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Host",this.host);
        conn.setRequestProperty("Content-Length", String.valueOf(soap.length()));
        conn.setRequestProperty("Content-Type", this.contentType);
        // 调用的接口方法是
        conn.setRequestProperty("SOAPAction", this.nameSpace + method);
        OutputStream os = conn.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
        osw.write(soap);
        osw.flush();
        osw.close();
        // 获取webserivce返回的流
        InputStream is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null){
            sb.append(line);
        }
        System.out.println(sb.toString());

//        if (is!=null) {
//            byte[] bytes = new byte[0];
//            bytes = new byte[is.available()];
//            is.read(bytes);
//            String str = new String(bytes);
//            return str;
//        }else {
//            return null;
//        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        StringBuffer sendSoapString = new StringBuffer();
        String pwd = null;
//                Ws.doPostSingle("http://172.23.121.147:8110/Handler1.ashx",
//                null, null, null, null);
        String soap =
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Header>\n" +
                "    <AuthorSoapHeader xmlns=\"http://tempuri.org/\">\n" +
                "      <Secretkey>"+pwd+"</Secretkey>\n" +
                "    </AuthorSoapHeader>\n" +
                "  </soap:Header>\n" +
                "  <soap:Body>\n" +
                "    <GetIdentityList xmlns=\"http://tempuri.org/\">\n" +
                "      <searchModel>\n" +
                "        <IDEN_DATA_ID></IDEN_DATA_ID>\n" +
                "        <BASE_BRANCH_MANAGE_ID></BASE_BRANCH_MANAGE_ID>\n" +
                "        <BASE_VERSION_MANAGE_ID></BASE_VERSION_MANAGE_ID>\n" +
                "        <CHEM_NAME></CHEM_NAME>\n" +
                "        <CHEM_NAME_VAGUE></CHEM_NAME_VAGUE>\n" +
                "        <CHEM_CAS>62-53-3</CHEM_CAS>\n" +
                "        <CHEM_SHAPE></CHEM_SHAPE>\n" +
                "        <CHEM_CATEGORY></CHEM_CATEGORY>\n" +
                "        <CHEM_ALIAS></CHEM_ALIAS>\n" +
                "        <IDEN_ANNEX_ID></IDEN_ANNEX_ID>\n" +
                "      </searchModel>\n" +
                "    </GetIdentityList>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        sendSoapString.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tes=\"http://test03/\">");
        sendSoapString.append("   <soapenv:Header/>");
        sendSoapString.append("   <soapenv:Body>");
        sendSoapString.append("      <tes:getSum>");
        sendSoapString.append("         <arg0>66</arg0>");
        sendSoapString.append("         <arg1>33</arg1>");
        sendSoapString.append("      </tes:getSum>");
        sendSoapString.append("   </soapenv:Body>");
        sendSoapString.append("</soapenv:Envelope>");
        soap = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <GetIP xmlns=\"http://tempuri.org/\" />\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";

        String url = "http://172.23.121.147:8081/DataExtractService.asmx";
        String namespace = "http://tempuri.org/";
        int timeout = 0;
        String method = "GetChemIdentity";

        try {
            SoapConncet conncet = new SoapConncet();
            String ret= conncet. fire(method,
                    "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                            "  <soap:Header>\n" +
                            "    <AuthorSoapHeader xmlns=\"http://tempuri.org/\">\n" +
                            "      <Secretkey>123123</Secretkey>\n" +
                            "    </AuthorSoapHeader>\n" +
                            "  </soap:Header>\n" +
                            "  <soap:Body>\n" +
                            "    <GetChemIdentity xmlns=\"http://tempuri.org/\">\n" +
                            "      <searchModel>\n" +
                            "        <IDEN_DATA_ID>B3672D6B-B1AB-45F3-B46A-04E1AFCAAD37</IDEN_DATA_ID>\n" +
                            "      </searchModel>\n" +
                            "    </GetChemIdentity>\n" +
                            "  </soap:Body>\n" +
                            "</soap:Envelope>");
            System.out.println(ret);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
