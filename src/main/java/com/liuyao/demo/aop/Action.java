package com.liuyao.demo.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Action {

    String name();
}

/**
 第二步，编写拦截规则的注解
 注解：本身没有功能，与xml一样都是元数据(解释数据的数据)，这就是所谓的配置
        注解的功能来自用此注解的地方
 */