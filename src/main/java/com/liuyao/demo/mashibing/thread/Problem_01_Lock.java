package com.liuyao.demo.mashibing.thread;

import java.util.ArrayList;
import java.util.List;

public class Problem_01_Lock {

    public static void main(String[] args) {

    }



    /**
     * 定义容器 add size 方法 两线程
     */
    public static void testLock2(){

        Tcontainer c = new Tcontainer();

    }

    /**
     * 同步容器
     * put get getCount 方法
     * 支持 2个生产者线程 10个消费之线程 阻塞调用
     * 解释: 2个生产,容器只能容纳10个
     *
     */
    public static void testLockMany(){



    }

    /**
     * 连个线程交替打印 A-Z 1-26
     *
     */
    public static void testA_Zand1_26(){



    }
}

class Tcontainer{
    List list = new ArrayList<>();

    public void put(Object o){
        list.add(o);
    }

    public Object get(int i ){
        return list.get(i);
    }

    public int getCount(){
        return list.size();
    }
}