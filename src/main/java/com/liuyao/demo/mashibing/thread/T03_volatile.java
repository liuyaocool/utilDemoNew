package com.liuyao.demo.mashibing.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * volatile
 * 保证线程可见性
 *  MESI CPU缓存一致性协议
 *  多个线程不运行在同一个cpu上
 *  堆内存 所有线程共享
 *      当前线程会copy一份堆内存的共享数据，两个线程会出现两份数据，
 *      当一个线程修改了这个值，另一个线程不会立马读到新值
 *          （由于修改值是及时的，但不加volatile会用当前线程copy的数据，造成两个线程数据不一致）
 *
 * 禁止指令重排序 -- 汇编层面
 *  Double Check Lock -- 懒汉式单例
 *  测试 ASM 操作二进制码
 *  loadfence原语指令
 *  storefence原语指令
 *
 * 不能保证原子性
 *  当前操作完成后才能继续执行
 *  不能替换synchronized
 */
public class T03_volatile extends Func {

    /*volatile*/ boolean running = true;

    volatile static T03_volatile T = new T03_volatile();

    void m(){
        log("m start");
        while (running){

        }
        log(("m end"));
    }

    public static void main(String[] args) {

//        testVisible();
//        testVisible2();

        testAtomicity();

    }

    //测试线程可见
    private static void testVisible(){
        T03_volatile t = new T03_volatile();

        new Thread(t::m, "t1").start();

        msleep(1000);

        t.running = false;
    }

    //测试线程可见
    private static void testVisible2(){

        new Thread(T::m, "t1").start();

        msleep(1000);

        T.running = false;
    }

    /*volatile*/ int count = 0;
    private /*synchronized*/ void m2(){
        for (int i = 0; i < 10000; i++) {
            count++;
        }
    }
    //测试原子性
    private static void testAtomicity(){
        T03_volatile t = new T03_volatile();

        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new Thread(t::m2, "atom-" + i));
        }
        list.forEach((o) -> o.start());

        list.forEach((o) -> {
            try {
                o.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println(t.count);


    }
}
