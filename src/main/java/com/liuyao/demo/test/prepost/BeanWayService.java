package com.liuyao.demo.test.prepost;

//2：使用@Bean形式的Bean
public class BeanWayService {

    public void init () {
        System.out.println("@Bean-init-method");
    }
    public BeanWayService () {
        super();
        System.out.println("初始化构造函数-BeanWayService");
    }
    public void destory () {
        System.out.println("@Bean-destory-method");
    }
}
