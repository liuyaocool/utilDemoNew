package com.liuyao.demo.test.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

// 3:事件发布类
@Component
public class DemoPublisher {

    @Autowired
    ApplicationContext applicationContext; // 注入。。用来发布事件

    public void publish(String msg) {
        applicationContext.publishEvent(new DemoEvent(this, msg)); // 使用此方法发布
    }
}
