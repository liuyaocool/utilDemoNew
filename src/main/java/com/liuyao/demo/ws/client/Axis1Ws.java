package com.liuyao.demo.ws.client;


import com.liuyao.demo.ws.User;
import com.liuyao.demo.ws.test.Ws;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.apache.axis.message.SOAPHeaderElement;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.encoding.TypeMapping;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * <?xml version="1.0" encoding="utf-8"?>
 * <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
 *   <soap:Header>
 *     <AuthorSoapHeader xmlns="http://tempuri.org/">
 *       <Secretkey>string</Secretkey>
 *     </AuthorSoapHeader>
 *   </soap:Header>
 *   <soap:Body>
 *     <GetStabilityReactivity xmlns="http://tempuri.org/">
 *       <searchModel>
 *         <param>val</param>
 *       </searchModel>
 *     </GetStabilityReactivity>
 *   </soap:Body>
 * </soap:Envelope>
 *
 */
public class Axis1Ws {

    public static void main(String[] args) throws SOAPException {

        String pwd = Ws.doPostSingle("http://172.23.121.147:8110/Handler1.ashx",
                null, null, null, null);

        System.out.println("====================================================================================");

        String url = "http://sdsapi.nrcc.com.cn/DataExtractService.asmx?wsdl";
        String namespace = "http://tempuri.org/";
        int timeout = 0;
        String method = "GetIP";
        Map params = new HashMap();
        User user = new User("62-53-3");
        user.setCHEM_CATEGORY("混合物");
        user.setCHEM_SHAPE("固体");
        user.setCHEM_NAME("苯胺");
        params.put("searchModel", user);

        /**
         * 添加header
         * <soap:Header>
         *     <AuthorSoapHeader xmlns="http://tempuri.org/">
         *       <Secretkey>123</Secretkey>
         *     </AuthorSoapHeader>
         *   </soap:Header>
         */
        SOAPHeaderElement header = new SOAPHeaderElement(namespace, "AuthorSoapHeader");
        header.setPrefix("");
        header.setMustUnderstand(true);
        SOAPElement element = header.addChildElement("Secretkey");
        element.addTextNode(pwd);
        axis1(url, namespace, timeout, method, params, header);


    }

    public static String axis1(String url, String namespace, int timeout, String method,
                               Map<String, Object> params,SOAPHeaderElement header) {

        Service service = new Service();
        //参数值
        String retXML2 = null;
        //添加地址
        try {
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new URL(url));

            //设定调用超时
            if (timeout > 0){
                call.setTimeout(new Integer(timeout));
            }
            //命名空间(wsdl文件中的targetNameSpace属性值) 以及方法名
            call.setOperationName(new QName(namespace, method));
            call.setSOAPActionURI(namespace + method);

            //参数类型 字符串
    //        call.addParameter("no", XMLType.XSD_STRING, ParameterMode.IN);
            //参数类型 实例
            Object[] soapParams = new Object[params.size()];
            int i = 0;
            for (String key : params.keySet()) {
                Class claz = params.get(key).getClass();
                //namespace处的参数 在wsdd中有配置
                QName qn = new QName(namespace, key);
                //注册SimpleObject的序列化类型
                call.registerTypeMapping(claz, qn, new BeanSerializerFactory(claz, qn),
                        new BeanDeserializerFactory(claz, qn));
                //可省略
                call.addParameter(key, XMLType.XSD_ANYTYPE, ParameterMode.IN);
                soapParams[i++] = params.get(key);
            }

            call.setUseSOAPAction(true);
            //返回值类型
            call.setReturnType(XMLType.XSD_STRING);
            if (null != header) {
                call.addHeader(header);
            }

            retXML2 = (String) call.invoke(soapParams);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        System.out.println("========================================================================");
        System.out.println("接口返回数据： "+String.valueOf(retXML2));
        System.out.println("====================================================================================");
        return retXML2;
    }

}
