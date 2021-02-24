package com.liuyao.design_patterns.singleton;

/**
 * 最完美 单例
 * 不仅可以解决线程同步，还可以防止反序列化。
 *  反序列化：只会反实例化INSTANCE，仍然是Mgr03，依旧是单例
 * <<effective java>>
 */
public enum Mgr03_enum {

    INSTANCE;

    public void m() {}

    public static void main(String[] args) {
        for(int i=0; i<100; i++) {
            new Thread(()->{
                System.out.println(Mgr03_enum.INSTANCE.hashCode());
            }).start();
        }
    }

}
