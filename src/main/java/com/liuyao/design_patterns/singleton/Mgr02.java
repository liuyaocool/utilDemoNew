package com.liuyao.design_patterns.singleton;

/**
 * 静态内部类方式
 * JVM保证单例
 * 加载外部类时不会加载内部类，这样可以实现懒加载
 */
public class Mgr02 {

    private Mgr02() {
    }

    private static class Mgr07Holder {
        private final static Mgr02 INSTANCE = new Mgr02();
    }

    public static Mgr02 getInstance() {
        return Mgr07Holder.INSTANCE;
    }

    public void m() {
        System.out.println("m");
    }

    public static void main(String[] args) {
        for(int i=0; i<100; i++) {
            new Thread(()->{
                System.out.println(Mgr02.getInstance().hashCode());
            }).start();
        }
    }


}
