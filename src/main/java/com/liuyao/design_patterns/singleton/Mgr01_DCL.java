package com.liuyao.design_patterns.singleton;

/**
 * lazy loading
 * 也称懒汉式
 * 虽然达到了按需初始化的目的，但却带来线程不安全的问题
 * 可以通过synchronized解决，但也带来效率下降
 */
public class Mgr01_DCL {
    private static volatile Mgr01_DCL INSTANCE; //JIT

    private Mgr01_DCL() {
    }

    public static Mgr01_DCL getInstance() {
        if (INSTANCE == null) {
            //双重检查
            synchronized (Mgr01_DCL.class) {
                if(INSTANCE == null) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    INSTANCE = new Mgr01_DCL();
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
                System.out.println(Mgr01_DCL.getInstance().hashCode());
            }).start();
        }
    }
}
