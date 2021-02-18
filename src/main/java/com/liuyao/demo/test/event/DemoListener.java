package com.liuyao.demo.test.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

//2:事件监听器
@Component
public class DemoListener implements ApplicationListener<DemoEvent> { //实现借口 指定事件类型

    public void onApplicationEvent(DemoEvent event) { // 使用此方法对消息进行接受处理
        String msg = event.getMsg();

        System.out.println("我(bean-demoListener)接收到了(bean-demoPublisher)发布的消息:" + msg);
    }
}
