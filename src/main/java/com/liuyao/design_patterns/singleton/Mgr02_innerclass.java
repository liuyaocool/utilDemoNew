package com.liuyao.design_patterns.singleton;

/**
 * 静态内部类方式
 * JVM保证单例
 * 加载外部类时不会加载内部类，这样可以实现懒加载
 */
public class Mgr02_innerclass {

    private Mgr02_innerclass() {
    }

    private static class Mgr07Holder {
        private final static Mgr02_innerclass INSTANCE = new Mgr02_innerclass();
    }

    public static Mgr02_innerclass getInstance() {
        return Mgr07Holder.INSTANCE;
    }

    public void m() {
        System.out.println("m");
    }

    public static void main(String[] args) {
        for(int i=0; i<100; i++) {
            new Thread(()->{
                System.out.println(Mgr02_innerclass.getInstance().hashCode());
            }).start();
        }
    }


}
