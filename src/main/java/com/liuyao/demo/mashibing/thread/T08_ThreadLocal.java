package com.liuyao.demo.mashibing.thread;

public class T08_ThreadLocal extends Func{

    static ThreadLocal<Person> tl = new ThreadLocal<>();

    public static void main(String[] args) {
        test1();
    }

    public static void test1(){
        new Thread(()->{
            msleep(2000);
            System.out.println(tl.get());
        }).start();

        new Thread(()->{
            msleep(1000);
            tl.set(new Person());
        }).start();
    }


}

class Person{
    String name = "Tina";
}
