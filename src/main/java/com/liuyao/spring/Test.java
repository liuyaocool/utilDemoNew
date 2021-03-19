package com.liuyao.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
    }

    public void m() {
        System.out.println("m");
    }
}
