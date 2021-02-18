package com.liuyao.demo.ws.client;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

import javax.xml.namespace.QName;
import java.util.Map;

public class Axis2Ws {

    //该种方式缺点是客户端实体类路径即包名必须与服务端实体类路径相同！！！
    // 但该方式可以调任何方式编写的接口！！！！
    public static String axis2(String url, String namespace, String method,
                               Map<String, Object> params) throws AxisFault {

        // 使用RPC方式调用WebService  
        RPCServiceClient client = new RPCServiceClient();
        Options options = client.getOptions();
        EndpointReference point = new EndpointReference(url);
        options.setTo(point);

        QName qName = new QName(namespace, method);

        Object[] wsParams = new Object[params.size()];
        int i = 0;
        for (String key : params.keySet()) {
            wsParams[i++] = params.get(key);
        }
        //返回类型
        Class<?>[] classes = new Class[] { Boolean.class };

//        Object[] o = client.invokeBlocking(qName, wsParams, classes);
        OMElement om = client.invokeBlocking(qName, wsParams);
        return om.toString();
    }
}
