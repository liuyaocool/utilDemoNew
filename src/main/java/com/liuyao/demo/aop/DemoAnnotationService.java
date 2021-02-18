package com.liuyao.demo.aop;

import org.springframework.stereotype.Service;

//3 ：编写使用注解的被拦截类
@Service
public class DemoAnnotationService {

    @Action(name="注解拦截的add操作")
    public void add () {

    }
}
