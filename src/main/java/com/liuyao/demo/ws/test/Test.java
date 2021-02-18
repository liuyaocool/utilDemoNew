package com.liuyao.demo.ws.test;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;

import javax.xml.namespace.QName;
import javax.xml.rpc.encoding.TypeMapping;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class Test {

    protected static void registerBeanMapping(TypeMapping mapping, Class type,
                                              QName qname) {
        mapping.register(type, qname, new BeanSerializerFactory(type, qname),
                new BeanDeserializerFactory(type, qname));
    }

    public static void main(String[] args) {

//        String methodName = "GeProjectPlan";

//        wsInvoke("GetEmployee");

//        wsInvoke("","", "");

        testWeather();

    }

    public static String testWeather(){
        String url = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx";
        String nameSpace = "http://WebXml.com.cn/";
        String methodName = "getSupportCity";
        Map pa = new HashMap();
        pa.put("byProvinceName", "山东");

        return wsInvoke(url, nameSpace, methodName,pa);
    }

    public static String wsInvoke(String methodName){
        String url = "http://10.206.1.81:8006/AppService.asmx?wsdl";
        String nameSpace = "http://www.qigousoft.com/";
        wsInvoke(url, nameSpace, methodName,null);
        return null;
    }

    public static String wsInvoke(String url, String nameSpace, String methodName, Map<String, Object> params){


        Service service = new Service();
        String result = null;
        try{
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(url));

            // 设置要调用哪个方法
            call.setOperationName(new QName(nameSpace, methodName));
            call.setReturnType(XMLType.XSD_STRING);

            call.setUseSOAPAction(true);
            call.setSOAPActionURI(nameSpace + methodName);

            List paramValues = new ArrayList();
            if (null != params){
                for (String key : params.keySet()) {
                    call.addParameter(new QName(nameSpace,key),
                            XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                    paramValues.add(params.get(key));
                }
            }

            // 调用方法并传递参数-传递的参数和设置的参数要对应，顺序不能搞错了
            result= (String) call.invoke(paramValues.toArray());

            System.out.print("===========-=-=-=-=-=-=-=-=-=-==-=-=-=-=-=-=-=-=-=-");
            System.out.print(result);//打印结果（我设置的接收格式为json字符串，这边直接打印出来）
        }catch (Exception ex){
            ex.printStackTrace();
//            System.out.println(ex.getMessage());

        }
        return result;

    }
}
