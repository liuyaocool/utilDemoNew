package com.liuyao.demo.mashibing.proxy;

import com.liuyao.demo.mashibing.entity.Human;
import com.liuyao.demo.mashibing.entity.impl.Girl;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.Callback;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class P02_CGLibFactory implements Callback{

    private Object target;

    public P02_CGLibFactory() {
        super();
    }

    public P02_CGLibFactory(Object target) {
        super();
        this.target = target;
    }

    public Object createProxy(){
        //增强器
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Girl.class);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    public static void main(String[] args) {




    }

    private static void print(Object o){
        System.out.println(o);
    }
}
