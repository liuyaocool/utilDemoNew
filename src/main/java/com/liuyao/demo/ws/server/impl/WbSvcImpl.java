package com.liuyao.demo.ws.server.impl;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.util.concurrent.Executors;

@WebService
public class WbSvcImpl implements com.liuyao.demo.ws.server.WbSvc {

    static String url = "http://localhost:9090/wbsvc";

    @Override
//    @WebMethod(exclude = true)
//    @WebResult(targetNamespace = "")
//    @RequestWrapper(localName = "sendBuesinessData", targetNamespace = "http://service.ws.business.ump.com/", className = "com.chat.worker.common.ump.SendBuesinessData")
//    @ResponseWrapper(localName = "sendBuesinessDataResponse", targetNamespace = "http://service.ws.business.ump.com/", className = "com.chat.worker.common.ump.SendBuesinessDataResponse")
    public String findAreaCount(String startDate, String endDate, String areaCode) {
        return "asdfghjkl";
    }

    /**
     * 此发布方法只能在java7下
     * @param args
     */
    public static void main(String[] args) {
//        Endpoint endpoint = Endpoint.create(new WbSvcImpl());
//        endpoint.setExecutor(Executors.newScheduledThreadPool(100));

//        endpoint.publish(url);

//        Endpoint.publish(url, new WbSvcImpl());
    }
}
