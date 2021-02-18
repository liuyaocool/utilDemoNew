package com.liuyao.demo.ws.test;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.apache.axis.message.SOAPHeaderElement;

import javax.xml.namespace.QName;
import javax.xml.rpc.encoding.TypeMapping;
import javax.xml.soap.SOAPElement;
import java.util.HashMap;
import java.util.Map;


public class TestWs {

    protected static void registerBeanMapping(TypeMapping mapping, Class type,
                                       QName qname) {
        mapping.register(type, qname, new BeanSerializerFactory(type, qname),
                new BeanDeserializerFactory(type, qname));
    }

    public static void main(String[] args) {

        String pwd = Ws.doPostSingle("http://172.23.121.147:8110/Handler1.ashx",
                null, null, null, null);

        String url = "http://sdsapi.nrcc.com.cn/DataExtractService.asmx";
        String methodName = "GetIdentityList";
        String soap = "http://tempuri.org/";

//        String url = "http://10.206.1.81:8006/AppService.asmx";
//        String methodName = "GeProjectPlan";
        // 域名，这是在server定义的--不知道的可以问接口提供方，他们一并提供这个
//        String nameSpace = "http://www.qigousoft.com/";

//        TypeMappingRegistry registry = service.getTypeMappingRegistry();
//        TypeMapping mapping = registry.createTypeMapping();
//        registerBeanMapping(mapping, GetSubscriptionReq.class,new QName("http://req.portalEngine.ismp.chinatelecom.com", "GetSubscriptionReq"));
//        registerBeanMapping(mapping, GetSubscriptionRsp.class,new QName("http://rsp.portalEngine.ismp.chinatelecom.com", "GetSubscriptionRsp"));
//        registerBeanMapping(mapping, SubInfo.class,new QName("http://rsp.portalEngine.ismp.chinatelecom.com", "SubInfo"));
//        registry.register("http://schemas.xmlsoap.org/soap/encoding/", mapping);

        Service service = new Service();

        try{
            SOAPHeaderElement header = new SOAPHeaderElement(soap, "AuthorSoapHeader");
            header.setPrefix("");
            header.setMustUnderstand(true);
            SOAPElement element = header.addChildElement("Secretkey");
            element.addTextNode(pwd);

            Call call = (Call) service.createCall();
//            call.setTargetEndpointAddress(url);
            call.setTargetEndpointAddress(url);

            // 设置要调用哪个方法
            call.setOperationName(new QName(soap, methodName));

            // 设置要传递的参数--要和接口方提供的参数名一致 多个add多次
            Object[] params = new Object[1];
            call.addParameter(new QName(soap, "searchModel"),
                    XMLType.XSD_ENTITY,
                    javax.xml.rpc.ParameterMode.IN);
            Map aa = new HashMap<>();
            aa.put("CHEM_CAS", "62-53-3");
            params[0] = aa;

            // 要返回的数据类型（自定义类型，我这边接口提供方给我返回的是json字符串，所以我用string类型接收。
            // 这个地方一定要设置好，不然各种报错很崩溃）
            call.setReturnType(new QName(soap, methodName), Map.class);
//            call.setReturnType(XMLType.XSD_STRING);

            call.setUseSOAPAction(true);
            call.setSOAPActionURI(soap + methodName);

//            SOAPHeaderElement soapHeaderElement = new SOAPHeaderElement(
//                    nameSpace, headerName);
//            call.addHeader(soapHeaderElement);

            call.addHeader(header);

            // 调用方法并传递参数-传递的参数和设置的参数要对应，顺序不能搞错了
            Object v = call.invoke(params);

            System.out.print("===========-=-=-=-=-=-=-=-=-=-==-=-=-=-=-=-=-=-=-=-");
            System.out.print(v);//打印结果（我设置的接收格式为json字符串，这边直接打印出来）
        }catch (Exception ex){
            System.out.println(ex.getMessage());
//            ex.printStackTrace();
        }
    }

//    private static HttpClient    httpclient;



//    public static HttpClient getHttpClient() {
//        if (httpclient != null)
//            return httpclient;
//
//        // SimpleHttpConnectionManager默认是false,connection是不会被主动关闭的，因此要设置为true,这里很关键
//        httpclient = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));
//        return httpclient;
//    }
//
//    public static void getQuestionList() {
//        HttpClient httpclient = getHttpClient();
//        String url = "http://203.148.56.10/WebService/AvisRentalService.asmx?op=GetSDLocations&LoginName=avis@common.com&Password=zI9Fvh4NmMA=";
//        GetMethod get = new GetMethod(url) {
//            public String getRequestCharSet() {
//                return "UTF-8";
//            }
//        };
//
//        try {
//            int result = httpclient.executeMethod(get);
//            if (result != 200) {
//                return;
//            }
//
//            String xml = get.getResponseBodyAsString();
//            get.releaseConnection();
//            System.out.println(xml);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            get.releaseConnection();
//        }
//    }
}
