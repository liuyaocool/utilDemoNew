package com.liuyao.demo.mashibing.thread;

import java.io.IOException;

public class Func extends Thread{

    public static void msleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("sleep " + time + " s");
    }

    public static void log(Object str){
        System.out.println("Thread-" + Thread.currentThread().getName()
                + " --> " + String.valueOf(str));
    }

    public static void systemInRead(){
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
