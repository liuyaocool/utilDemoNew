package com.liuyao.demo.ttt;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;

public class TestId {

    public static void main(String[] args) throws UnsupportedEncodingException {

        System.out.println("adfasvcblrfjhgalsjd".replace("a", "*"));

        System.out.println(URLEncoder.encode("\"as'd/z x+c'", "utf-8"));

        System.out.println(System.currentTimeMillis());
        System.out.println(System.nanoTime());

        System.out.println(" -------------------------------------------- ");

        int circleNumber = 1000_0000;
        final CountDownLatch latch = new CountDownLatch(circleNumber);
//        final ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        final ThreadPoolExecutor pool = new ThreadPoolExecutor(0, 10,
                100, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
//                new LinkedBlockingDeque<>(),
                new ThreadFactory() {public Thread newThread(Runnable r) { return new Thread(r); }},
                new RejectedExecutionHandler() {public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) { }});
        final LongAdder count = new LongAdder();
        for (int i = 0; i < circleNumber; i++) {
            pool.execute(() -> {
                final long l = System.currentTimeMillis() + (int) (Math.random() * 1000000) + (int) (Math.random() * 1000000);
                final long l1 = System.currentTimeMillis() + (int) (Math.random() * 1000000) + (int) (Math.random() * 1000000);
                if (l == l1) {
                    count.increment();
                }
//                final String s = UUID.randomUUID().toString();
//                final String s2 = UUID.randomUUID().toString();
//                if (s.equals(s2)) {
//                    count.increment();
//                }
                latch.countDown();
//                System.out.println(Thread.currentThread().getName());
            });
        }
        new Thread(() -> {
            while (latch.getCount() > 0) {
                System.out.println(latch.getCount());
                System.out.println(" all：" + pool.getTaskCount());
                System.out.println("over：" + pool.getCompletedTaskCount());
                System.out.println(" run：" + pool.getActiveCount());
                System.out.println("========================");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("get zero " + count);
        pool.shutdown();
    }
}
