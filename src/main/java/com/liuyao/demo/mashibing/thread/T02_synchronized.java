package com.liuyao.demo.mashibing.thread;

/**
 * synchronized 底层实现
 * JDK早期 重量级 - OS
 * 后来改进：锁升级 - 《我就是厕所所长》（1 2）（马士兵写）
 *  只有一个线程 不加锁
 *  线程争用 升级为自旋锁（适用：线程数少 线程执行时间短）
 *  10次后 升级为重量级锁-OS
 *
 * 可重入 同一锁可互相调用 test2()
 * 优化：
 *  细化 同步代码块中的语句越少越好
 *  粗化 当细琐很多的时候，合并成一张大锁，减少锁争用
 *
 * synchronized(o) 是在o头上两位添加标记 不能更改对象 需要加final关键字
 *
 * 注：不能加锁 String常量（跟JVM处理String的方式相关） Integer Long 等基本数据类型
 *
 */
public class T02_synchronized extends Func{

    synchronized void m1(){
        log("m1 start");
        msleep(10000);
        log("m1 end");

    }

    void m2(){
        log("m2 start");
        msleep(5000);
        log("m2 end");

    }

    synchronized void m3(){
        log("m3 start");
        msleep(1000);
        m4();
        log("m3 end");
    }

    synchronized void m4(){
        log("m4 start");
        msleep(1000);
        log("m4 end");
    }

    public static void main(String[] args) {
//        test_12();
        test_14();
//        test_34();

    }

    public static void test_12(){

        T02_synchronized t = new T02_synchronized();

        new Thread(t::m1, "t1").start();
        new Thread(t::m2, "t2").start();
    }
    public static void test_14(){

        // 两个线程之间不存在重入,存在争用
        T02_synchronized t = new T02_synchronized();

        new Thread(t::m1, "t1").start();
        new Thread(t::m4, "t4").start();
    }

    public static void test_34(){
        //可重入,同一个线程同一锁可互相调用

        T02_synchronized t = new T02_synchronized();

        new Thread(t::m3, "t3").start();
    }

    //线程出现异常 默认锁会被释放
}
