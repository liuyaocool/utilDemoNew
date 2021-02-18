package com.liuyao.demo.config.runner;

import com.liuyao.demo.utils.LogUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
public class MyRunner1 implements ApplicationRunner {


    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        LogUtil.info("这是自启动方法。");

//        Endpoint.publish("http://127.0.0.1:9090/wbsvc", new WbSvcImpl());

        //发布webserice 仅jdk7可用
//        WbSvc wbSvc = new WbSvcImpl();
//        JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
//        factory.setServiceClass(WbSvc.class);
//        factory.setAddress("http://localhost:9090/wbsvc");
//        factory.setServiceBean(wbSvc);
//        factory.create();
//        System.out.println("Server start...");
    }
}
