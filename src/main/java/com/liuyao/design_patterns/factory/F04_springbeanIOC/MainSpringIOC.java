package com.liuyao.design_patterns.factory.F04_springbeanIOC;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainSpringIOC {

    /**
     * PO: 数据库
     * VO: 传值
     * POJO: 普通
     * Bean: 一系列专门格式
     * 都是一回事
     *
     * inverse of control ioc 控制反转 又叫DI dependency injection 依赖注入
     *  原来在程序中控制 spring在xml文件中控制
     *
     *
     * @param args
     */
    public static void main(String[] args) {
        Driver d = new Driver();

        ApplicationContext context = new ClassPathXmlApplicationContext("config/spring.xml");
        d = (Driver) context.getBean("driver");
    }
}
