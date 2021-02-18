package com.liuyao.demo.ws;

import com.liuyao.demo.ws.test.Ws;

import java.util.HashMap;
import java.util.Map;

public class Test {

    public static void main(String[] args) {

        String pwd = Ws.doPostSingle("http://172.23.121.147:8110/Handler1.ashx",
                null, null, null, null);

        String url = "http://sdsapi.nrcc.com.cn/DataExtractService.asmx/GetIdentityList";

        String method = "GetIdentityList";
        String soapAction = "http://tempuri.org/";
        String contentType = "text/xml";
        String encoding = "utf-8";
        Map map = new HashMap();
        map.put("AuthorSoapHeader", pwd);
//        Map mm = new HashMap();
//        mm.put("CHEM_CAS", "71-43-2");
//        map.put("ServiceSearchModel", mm);
        map.put("ServiceSearchModel.CHEM_CAS", "71-43-2");

        String wsdl = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Header>\n" +
                "    <AuthorSoapHeader xmlns=\"http://tempuri.org/\">\n" +
                "      <Secretkey>"+pwd+"</Secretkey>\n" +
                "    </AuthorSoapHeader>\n" +
                "  </soap:Header>\n" +
                "  <soap:Body>\n" +
                "    <GetIdentityList xmlns=\"http://tempuri.org/\">\n" +
                "      <searchModel>\n" +
                "        <CHEM_NAME>苯胺</CHEM_NAME>\n" +
                "        <CHEM_CAS>62-53-3</CHEM_CAS>\n" +
                "        <CHEM_SHAPE>固体</CHEM_SHAPE>\n" +
                "        <CHEM_CATEGORY>混合物</CHEM_CATEGORY>\n" +
                "      </searchModel>\n" +
                "    </GetIdentityList>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";

        String aa = Ws.doPostSingle(url, map, contentType, encoding, wsdl);

        System.out.println(aa);




    }

    /// <summary>
    /// 第三方应用加密
    /// </summary>
    /// <param name="value1"></param>
    /// <returns></returns>
    public static String EncryptApi2(String value1)
    {
        if (null != value1 && value1 != "")
        {
//            DESCryptoServiceProvider cryptoProvider = new DESCryptoServiceProvider();
//            MemoryStream ms = new MemoryStream();
//            CryptoStream cs = new CryptoStream(ms, cryptoProvider.CreateEncryptor(KEY_64_2, IV_64_2), CryptoStreamMode.Write);
//            StreamWriter sw = new StreamWriter(cs);//为cs加密流初始化为StreamWriter
//            sw.Write(value1);
//            sw.Flush();
//            cs.FlushFinalBlock();
//            ms.Flush();
//            return Convert.ToBase64String(ms.GetBuffer(), 0, (int)ms.Length);
        }
        return "";
    }

}
