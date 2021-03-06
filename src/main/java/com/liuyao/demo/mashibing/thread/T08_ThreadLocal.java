package com.liuyao.demo.mashibing.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class T08_ThreadLocal extends Func{

    static ThreadLocal<String> tl = new ThreadLocal<>();

    public static void main(String[] args) {
//        test1();
//        test2();
//        test3();
        test4();
    }

    private static void test4() {
        ThreadLocal tl = new ThreadLocal();
        final Thread[] starts = starts(tl);
        sleep(2000);
//        tl = null;
        System.gc();
        System.out.println("run gc");
        sleep(5000);
        for (Thread start : starts) {
            try {
                start.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.gc();
        System.out.println("join finish");
        sleep(2000);
        System.out.println("finish");
    }
    static Thread[] starts(ThreadLocal tl){
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            int finalI = i;
            threads[i] = new Thread(() -> {
                tl.set(new Person("thread-" + finalI));
                sleep(1000);
                if (0 == finalI) { sleep(20000); }
//                tl.remove();
                System.out.println(tl.get());
            });
            threads[i].start();
        }
        return threads;
    }
    static class Person {
        private String name;
        public Person(String name) { this.name = name; }
        public String toString() { return "Person{" + "name='" + name + '\'' + '}'; }
        protected void finalize() throws Throwable {
            System.out.println(System.currentTimeMillis() + " " + this.name + " person finalize");
            super.finalize();
        }
    }


    private static void test3() {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("线程结束");
            }
        });
        thread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();

    }

    private static void test2() {

        AtomicInteger count = new AtomicInteger();
        ThreadPoolExecutor.AbortPolicy rej = new ThreadPoolExecutor.AbortPolicy();
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "mythread-" + count.getAndIncrement());
            }
        };
        ThreadPoolExecutor tp = new ThreadPoolExecutor(
                4, 20, 5, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(), threadFactory, rej
        );

        for (int i = 0; i < 400; i++) {
            int finalI = i;
            tp.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "-" + finalI);
            });
        }

    }


    public static void test1(){
        new Thread(()->{
            msleep(2000);
            System.out.println(tl.get());
        }).start();

        new Thread(()->{
            msleep(1000);
            tl.set("thread2");
        }).start();

    }

    public static void sleep(long tm) {
        try {
            Thread.sleep(tm);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
