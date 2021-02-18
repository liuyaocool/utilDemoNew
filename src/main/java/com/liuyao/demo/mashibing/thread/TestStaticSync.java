package com.liuyao.demo.mashibing.thread;

public class TestStaticSync {


    // 锁定的是 TestStaticSync.class这个实例（本classloader中为单例）
    public synchronized static void m1(){

        System.out.println("m1 start");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("m1 end");
    }

    public synchronized static void m2(){

        System.out.println("m2 start");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("m2 end");
    }

    public static void m3(){

        System.out.println("m3 start");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("m3 end");
    }
    public synchronized void m4(){

        System.out.println("m4 start");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("m4 end");
    }

    public static void main(String[] args){
        for (int i = 0; i < 1; i++) {
            new Thread(){
                @Override
                public void run(){
                    m1();;
                }
            }.start();
            new Thread(){
                @Override
                public void run(){
                    m2();
                }
            }.start();
            new Thread(){
                @Override
                public void run(){
                    m3();
                }
            }.start();
            new Thread(new TestStaticSync()::m4, "m4").start();
        }
    }
}
