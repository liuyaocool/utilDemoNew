package com.liuyao.demo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

//5：编写切面
//@Aspect //声明一个切面
//@Component //让此切面成为Spring容器管理的Bean
public class LogAspect {

//    @Pointcut("@annotation(com.wisely.highlight_spring4.chi.aop.Action)") //声明切点
//    public void annotationPointCut () {}
//
//    @After("annotationPointCut()") //通过@After声明一个建言，冰使用@PointCut定义切点
//    public void after (JoinPoint joinPoint) {
//        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
//        Method method = signature.getMethod();
//        Action action = method.getAnnotation(Action.class);
//        System.out.print("注解式拦截" + action.name()); //通过反射可获得注解上的属性，然后做日志记录的相关操作
//    }
//
//    @Before("execution(* com.wisely.highlight_spring4.chi.aop.DemoMethodService.*(..))") //声明一个建言，直接使用拦截规则作为参数
//    public void before (JoinPoint joinPoint) {
//        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
//        Method method = signature.getMethod();
//        System.out.print("方法规则式拦截" + method.getName());
//
//    }
}
