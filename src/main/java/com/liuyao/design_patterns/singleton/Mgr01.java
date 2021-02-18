package com.liuyao.design_patterns.singleton;

/**
 * lazy loading
 * 也称懒汉式
 * 虽然达到了按需初始化的目的，但却带来线程不安全的问题
 * 可以通过synchronized解决，但也带来效率下降
 */
public class Mgr01 {
    private static volatile Mgr01 INSTANCE; //JIT

    private Mgr01() {
    }

    public static Mgr01 getInstance() {
        if (INSTANCE == null) {
            //双重检查
            synchronized (Mgr01.class) {
                if(INSTANCE == null) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    INSTANCE = new Mgr01();
                }
            }
        }
        return INSTANCE;
    }

    public void m() {
        System.out.println("m");
    }

    public static void main(String[] args) {
        for(int i=0; i<100; i++) {
            new Thread(()->{
                System.out.println(Mgr01.getInstance().hashCode());
            }).start();
        }
    }
}
