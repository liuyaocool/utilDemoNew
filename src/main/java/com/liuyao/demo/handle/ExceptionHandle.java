package com.liuyao.demo.handle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//由此类处理Controller 抛出的异常 throws Exception
@ControllerAdvice
public class ExceptionHandle {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResBody handle(Exception e){

        if(e instanceof BeautyException){
            BeautyException exception = (BeautyException) e;
            return new ResBody();
        } else {
            logger.info("[系统异常] {}", e);

            //返回
            return new ResBody();
        }
    }
}
