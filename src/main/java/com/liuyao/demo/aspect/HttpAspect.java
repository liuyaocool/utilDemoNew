package com.liuyao.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class HttpAspect {


    //spring 自带 HttpAspect.class为本类名 Controller也可以用日志形式
    private final static Logger logger = LoggerFactory.getLogger(HttpAspect.class);

    @Pointcut("execution(public * com.liuyao.demo.controller.BeautyController.*(..))")
    public void login(){

    }

    @Before("login()")
    public void doBefore(JoinPoint joinPoint){

        ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attr.getRequest();

        //url
        logger.info("url={}", request);//自动吧第二个参数写到第一个的{}中

        //method
        logger.info("method={}", request.getMethod());

        //ip
        logger.info("ip={}", request.getRemoteAddr());

        //那个类方法 类名.方法名
        logger.info("class_method={}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());

        //参数
        logger.info("args={}", joinPoint.getArgs());


        logger.info("方法之前");//显示所有信息
//        logger.error("error");//error形式
//        System.out.println("方法之前");
    }

    @After("login()")
    public void doAfter(){

        logger.info("方法之后");//显示所有信息
    }

    @AfterReturning(returning = "object", pointcut = "login()")
    public void doAfterRetiurning(Object object){

        logger.info("response={}", object);
    }

    /**
     * 老代码
     *

    //方法执行之前执行
//  @Before("execution(public * com.liuyao.demo.controller.BeautyController.getOne(..))")   //拦截一个方法 : .. 表示所有参数
    @Before("execution(public * com.liuyao.demo.controller.BeautyController.*(..))")   //拦截所有方法
    public void doBefore(){

        System.out.println("方法之前");
    }

    //方法之后执行
    @After("execution(public * com.liuyao.demo.controller.BeautyController.*(..))")
    public void doAfter(){

        System.out.println("方法之后");
    }
     */
}
