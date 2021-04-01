package com.liuyao.demo.test;

import com.liuyao.demo.utils.NumUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.StampedLock;

public class TestHash {

    public static void main(String[] args) {

        LongAdder longAdder = new LongAdder();
        longAdder.increment();

        ReentrantLock lock = new ReentrantLock();

        try {
            lock.lockInterruptibly();

            CountDownLatch latch = new CountDownLatch(123);
            latch.countDown();
            latch.await();

            CyclicBarrier barrier = new CyclicBarrier(20, () -> {
                System.out.println("满20个线程 执行");
            });
            barrier.await();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        StampedLock stampedLock = new StampedLock();
        Lock lock1 = stampedLock.asReadLock();
        Lock lock2 = stampedLock.asWriteLock();

        Condition consumer = lock.newCondition();
        Condition producer = lock.newCondition();

        consumer.signalAll();

        LinkedList linkedList = new LinkedList();



        printBinRes(" 1024       = ", 1024);
        printBinRes(" 1024 <<  1 = ", 1024 << 1);
        printBinRes(" 1024 >>  1 = ", 1024 >> 1);
        printBinRes(" 1024 >>> 1 = ", 1024 >>> 1);
        printBinRes("-1024       = ", -1024);
        printBinRes("-1024 <<  1 = ", -1024 << 1);
        printBinRes("-1024 >>  1 = ", -1024 >> 1);
        printBinRes("-1024 >>> 1 = ", -1024 >>> 1);
        System.out.println("------------------------------------------");

//        HashSet hashSet = new HashSet();
//        hashSet.add(new P("zhang", 12));
//        hashSet.add(new P("zhang", 12));
//        hashSet.add(new P("li", 13));
//        System.out.println(hashSet);

        new String("aa").hashCode();
        HashMap hashMap = new HashMap();
        hashMap.put("a", "aaa");
        hashMap.values();
        hashMap.get("a");

        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.put("a", "a");
        concurrentHashMap.get("a");


    }

    static void printBinRes(String name, int num) {
        System.out.println(name + NumUtil.printBin(num) + " = " + num);
    }


}

class P {
    private String name;
    private int age;

    public P() {
    }

    public P(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        P p = (P) o;
        return age == p.age &&
                Objects.equals(name, p.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
