package com.liuyao.demo.aop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

//6：配置类
@Configuration
@ComponentScan("com.liuyao.demo.aop")
@EnableAspectJAutoProxy //开启spring对aspectj的支持
public class AopConfig {


}
