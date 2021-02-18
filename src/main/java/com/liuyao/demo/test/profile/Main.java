package com.liuyao.demo.test.profile;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ProfileConfig.class);

        context.getEnvironment().setActiveProfiles("prod"); //先将活动的profile设置为prod
        context.register(ProfileConfig.class); // 后置注册Bean配置类，不然会报bean为定义错误
        context.refresh(); //刷新容器

        DemoBean demoBean = context.getBean(DemoBean.class);

        System.out.println(demoBean.getContent());

        context.close();
    }
}

/**
 将context.getEnvironment().setActiveProfiles("prod");改为
 context.getEnvironment().setActiveProfiles("dev");
 效果不同
 */