package com.liuyao.demo.mashibing.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * CAS 无锁 自旋锁 乐观锁 效率更高
 *  Atomic***
 *  cpu原语支持 不能被打断
 *  compare and set --Unsafe cpu原语
 *
 *  cas(v, expected, newval)
 *      if v == e //此时不能有另一个线程修改值，因为cpu原语支持，在cpu层面加屏障,不能被打断
 *          v = newval
 *      else
 *          try again or fail
 *
 * ABA问题：
 *  基础数据类型没问题，
 *  引用类型有问题，线程先改变指向B，修改了B的一些内容，又指回A(内部指向B)，指向不变，但指向的内容已经变了 --前女友问题
 *  某个线程把值先变成2，又变成了1
 *  加版本号即可解决
 */
public class T04_CAS {

    // =========================== CAS 类测试 ======================================================
    AtomicInteger count = new AtomicInteger(0);
    /*synchronized*/ void m1(){
        for (int i = 0; i < 10000; i++) {
            count.incrementAndGet();// count++
        }
    }
    private static void testAtomicInteger(){
        T04_CAS t = new T04_CAS();

        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new Thread(t::m1, "atom-" + i));
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

    // ============================= 性能对比测试 ======================================================
    int c1 = 0;
    AtomicLong c2 = new AtomicLong(0);
    LongAdder c3 = new LongAdder(); //分段锁 自增的所有线程分段 然后加和分段后的结果
    final Object lock = new Object();
    private long runThreadsRetTime(Thread[] threads) throws InterruptedException {
        long time = System.currentTimeMillis();
        for(Thread t : threads ) t.start();
        for (Thread t : threads) t.join();
        return System.currentTimeMillis() - time;
    }
    void add1(){ for (int i = 0; i < 100000; i++) synchronized (lock) {c1++;} }
    void add2(){ for (int i = 0; i < 100000; i++) c2.incrementAndGet(); }
    void add3(){ for (int i = 0; i < 100000; i++) c3.increment(); }
    private static void testOptimization(){
        Thread[] threads = new Thread[1000];
        T04_CAS t = new T04_CAS();
        try {
            for (int i = 0; i < threads.length; i++) threads[i] = new Thread(t::add1);
            System.out.println("int cost: " + t.runThreadsRetTime(threads));
            for (int i = 0; i < threads.length; i++) threads[i] = new Thread(t::add2);
            System.out.println("AtomicLong cost: " + t.runThreadsRetTime(threads));
            for (int i = 0; i < threads.length; i++) threads[i] = new Thread(t::add3);
            System.out.println("LongAdder cost: " + t.runThreadsRetTime(threads));
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        testAtomicInteger(); // CAS类测试
        testOptimization(); // 性能对比测试
    }
}
