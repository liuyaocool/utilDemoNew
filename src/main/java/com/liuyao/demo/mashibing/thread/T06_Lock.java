package com.liuyao.demo.mashibing.thread;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

/**
 * 基于CAS的锁 AQS
 * 书: 实战java高并发程序设计 -- 成长快乐 葛一鸣
 */
public class T06_Lock extends Func{

    static Lock lock = new ReentrantLock();//cas

    public static void main(String[] args) {
//        test1(5);
//        testTryLock();
//        testInterruptibly();
//        testFair();
//        testCountDownLatch();
//        testReadWriteLock();
//        testCyclicBarrier();
//        PhaserPerson.test();
//        testSemaphore();
//        testExchanger();
        testLockSupport();
    }

    private static void test1(int times){

        Thread t = new Thread(()->{
            try {
                lock.lock();
                log("start");
                for (int i = 0; i < times; i++) {
                    msleep(1000);
                    log(i);
                }
            } finally {
                lock.unlock();
                log("end");
            }
        }, "t1");
        t.start();

    }

    //尝试加锁 失败则不进行操作
    private static void testTryLock(){
        test1(10);

        Thread t = new Thread(()->{
            boolean locked = false;
            try {
                locked = lock.tryLock(5, TimeUnit.SECONDS);
                log("start: " + locked);
                msleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                log("Interrupted");
            } finally {
                if (locked) lock.unlock();
                log("end");
            }
        }, "t2");

        t.start();
    }

    //可打断锁
    private static void testInterruptibly(){
        test1(20);
        Thread t = new Thread(()->{
            try {
                lock.lockInterruptibly();
                log("start");
                msleep(5000);
            } catch (InterruptedException e) {
                log("Interrupted");
                e.printStackTrace();
            } finally {
                lock.unlock();
                log("end");
            }
        }, "interrupted");
        t.start();
        msleep(5000);
        t.interrupt();
    }

    // 公平锁
    private static void testFair(){
        lock = new ReentrantLock(true); //公平锁 等待队列 先来先执行

        Thread[] threads = new Thread[100];
        for (int i = 0; i < 100; i++) {
            threads[i] = new Thread(()->{
                try {
                    lock.lock();
                    log("get lock");
                } finally {
                    lock.unlock();
                }
            }, "t" + i);
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

    }

    /**
     * 读写锁
     * 共享锁 排他锁
     */
    private static void testReadWriteLock(){
        ReadWriteLock rwlock = new ReentrantReadWriteLock();
        Lock readlock = rwlock.readLock();
        Lock writelock = rwlock.writeLock();

        Runnable readr = ()->{
            try {
                readlock.lock();
                msleep(1000);
                System.out.println("read over.");
            }finally {
                readlock.unlock();
            }
        };
        Runnable writer = ()->{
            try {
                writelock.lock();
                msleep(1000);
                System.out.println("write over.");
            }finally {
                writelock.unlock();
            }
        };
        for (int i = 0; i < 18; i++) new Thread(readr).start();
        for (int i = 0; i < 5; i++) new Thread(writer).start();
    }

    /**
     * 门栓
     * 等待线程结束 也可用join
     */
    private static void testCountDownLatch(){
        Thread[] threads = new Thread[100];
        CountDownLatch latch = new CountDownLatch(threads.length);

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(()->{
                int res = 0;
                for (int j = 0; j < 10000; j++) {
                    res += j;
                }
                latch.countDown();
                latch.countDown();//可以多次
            });
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        try {
            latch.await();//门栓在这等着 countDown等到最后一个线程结束 才继续执行
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log("end latch");
    }

    /**
     *
     */
    private static void testCyclicBarrier(){
        CyclicBarrier barrier = new CyclicBarrier(20, ()->{
           System.out.println("满人 发车.");
        });
//        barrier = new CyclicBarrier(20, new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("满人 发车.");
//            }
//        });

        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                try {
                    barrier.await();//人不满 在这等着
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    /**
     * 阶段 一阶段一阶段进行
     * 例：遗传算法
     */
    static class PhaserPerson implements Runnable{
        static Phaser phaser = new Phaser(){
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                switch (phase){
                    case 0: System.out.println("所有人到齐了：" + registeredParties + "人\n"); return false;
                    case 1: System.out.println("所有人吃完了：" + registeredParties + "人\n"); return false;
                    case 2: System.out.println("所有人离开了：" + registeredParties + "人\n"); return false;
                    case 3: System.out.println("婚礼结束，新郎新娘抱抱：" + registeredParties + "人\n"); return true;
                    default: return true;
                }
            }
        };
        public static void test(){
            int pnum = 5;
            phaser.bulkRegister(pnum + 2);
            for (int i = 0; i < pnum; i++) {
                new Thread(new PhaserPerson("pp" + i)).start();
            }
            new Thread(new PhaserPerson("新郎")).start();
            new Thread(new PhaserPerson("新娘")).start();
        }
        String name;
        public PhaserPerson(String name) { this.name = name; }

        public void normalEvent(String action){
            msleep(1000);
            System.out.printf("%s %s.\n", this.name, action);
            phaser.arriveAndAwaitAdvance();
        }
        public void hugLove(){
            switch (this.name){
                case "新郎":
                case "新娘":
                    msleep(5000);
                    System.out.printf("%s 洞房！\n", name);
                    phaser.arriveAndAwaitAdvance();
                    break;
                default:
//                    msleep(1000);
//                    System.out.printf("%s 回家！\n", name);
                    phaser.arriveAndDeregister();
//                    phaser.register();
                    break;
            }
        }
        @Override
        public void run() {
            normalEvent("到达饭店");
            normalEvent("吃完");
            normalEvent("离开饭店");
            hugLove();
        }
    }

    /**
     * 限流
     * 线程同步的概念 跟线程池不同的概念
     * 类似于高速进站收费站
     */
    private static void testSemaphore(){
        Semaphore s;
        s = new Semaphore(1);// 参数为几 就允许几个同时进行
//        s = new Semaphore(2, true);//公平

        for (int i = 0; i < 2; i++) {
            new Thread(()->{
                try {
                    s.acquire();//没抢到的阻塞在这
                    log("start");
                    msleep(2000);
                    log("end");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    s.release();
                }
            }, "semaphore_" + i).start();
        }
    }

    /**
     * 交换器 只能两个线程之间
     * 场景 两个人交换装备
     */
    private static void testExchanger(){
        Exchanger<String> exchanger = new Exchanger<>();

        new Thread(()->{
            String s = "t1";
            log("start " + s);
            try {
                s = exchanger.exchange(s); // 在这阻塞
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log("end " + s);
        }, "exchange_1").start();

        new Thread(()->{
            String s = "t2";
            log("start " + s);
            try {
                s = exchanger.exchange(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log("end " + s);
        }, "exchange_2").start();

    }

    /**
     * 线程阻塞 pack
     */
    private static void testLockSupport() {
        Thread t = new Thread(()->{
            for (int i = 0; i < 10; i++) {
                msleep(1000);
                System.out.println(i);
                if (5 == i){
                    LockSupport.park(); //停止
                }
            }
        });

        t.start();
        LockSupport.unpark(t);

        msleep(10000);
        System.out.println("main thread sleep 10 seconds.");
//        LockSupport.unpark(t);
    }
}
