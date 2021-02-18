package com.liuyao.demo.mashibing.proxy;

import com.liuyao.demo.mashibing.entity.Human;
import com.liuyao.demo.mashibing.entity.impl.Girl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class P01 {

    public static void main(String[] args) {

        final Girl girl = new Girl();
        girl.setName("taylor swift");

        Human proxyGirl = (Human) Proxy.newProxyInstance(Girl.class.getClassLoader(), Girl.class.getInterfaces(), new InvocationHandler(){

            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{

                print("invoke method: " + method.getName());

                switch (method.getName()){
                    case "bath":
                        print("see bath。");
                        Object invoke = method.invoke(girl, args);
                        print("after see bath, run.");
                        return invoke;
                    case "eat":
                        print("see bath。");
                        Object invoke2 = method.invoke(girl, args);
                        print("after see bath, run.");
                        return invoke2;
                    default: return null;
                }
            }
        });

        proxyGirl.eat();
        print("==================");
        proxyGirl.bath();



    }

    private static void print(Object o){
        System.out.println(o);
    }
}
