package com.liuyao.demo.test.prepost;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//4：配置类
@Configuration
@ComponentScan("com.wisely.highlight_spring4.ch2.prepost")
public class PrePostConfig {

    @Bean(initMethod = "init", destroyMethod = "destory") //指定BeanWayService类的init 和destor 方法在构造之后、Bean销毁之前执行
    BeanWayService beanWayService () {
        return new BeanWayService();
    }

    @Bean
    JSR250WayService jsr250WayService () {
        return new JSR250WayService();
    }
}
