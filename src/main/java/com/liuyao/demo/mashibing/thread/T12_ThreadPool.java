package com.liuyao.demo.mashibing.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * 马士兵高并发八
 *  Callable() Future(特征)
 */
public class T12_ThreadPool extends Func{


    /**
     * Executors 线程池的工厂
     *
     * concurrent(并发) parallel(并行)
     *  并发是任务的提交,同时运行,同时提交
     *      好多任务同时过来
     *  并行指任务的执行,是并发的子集
     *  <<深入理解计算机系统>>
     *
     */
    public static void main(String[] args) {
        testFutureTask();
        testCollableFuture();
        testCompletableFuture();
    }

    /**
     * 大任务分叉 然后汇总
     */
    public void testForkJoinPool() {
        // 每个线程都有自己单独的队列(pop,push),当自己队列没有的时候,去其他队列偷(尾巴poll work steal)
        ExecutorService workStealingPool = Executors.newWorkStealingPool();


    }

    static void testPaeallelStreamAPI(){
        List<Integer> nums = new ArrayList<>();
        Random r = new Random();
        for(int i=0; i<10000; i++) nums.add(1000000 + r.nextInt(1000000));

        //System.out.println(nums);

        long start = System.currentTimeMillis();
        nums.forEach(v->isPrime(v));
        long end = System.currentTimeMillis();
        System.out.println(end - start);

        //使用parallel stream api

        start = System.currentTimeMillis();
        // 底层是ForkJoinPool
        // 并行流处理 比直接foreach快
        nums.parallelStream().forEach(T12_ThreadPool::isPrime);
        end = System.currentTimeMillis();

        System.out.println(end - start);
    }

    static boolean isPrime(int num) {
        for(int i=2; i<=num/2; i++) {
            if(num % i == 0) return false;
        }
        return true;
    }

    public static void testThreadPool(){
        // 单线程线程池: 任务队列 生命周期管理
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        // 加一个线程就马上执行 忽高忽低
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        // 固定线程数 平稳
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
        // 定时任务线程池 quartz cron
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
//        scheduledExecutorService.scheduleAtFixedRate(()->{ }, );
        // 上述是共同访问同一个任务队列

    }

    /**
     * ThreadPoolExecutor
     *  corePollSize 核心线程
     *  maxiunpoolSize 最大线程
     *  keepAliveTime 多长时间没活干,归还OS,剩下核心线程
     *  unit
     *  workQueue 线程队列
     *  threadFactory 线程工厂
     *  handler 拒绝策略
     *      Abort 不处理,抛异常
     *      Discard 不处理,不抛异常
     *      DiscardOldest 扔掉排队时间最久的,也就是最先加入的
     *      CallerRuns 调用者处理任务 --此处为方法调用者所在线程处理
     */
    public static void testThreadPoolExecutor(){
        class Task implements Runnable{
            private int i;
            public Task(int i) {
                this.i = i;
            }

            @Override
            public void run() {
                log("Task" + this.i);
                systemInRead();
            }

            @Override
            public String toString() {
                return "Task{" +
                        "i=" + i +
                        '}';
            }
        }
        ThreadPoolExecutor exe = new ThreadPoolExecutor(2, 4,
                60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(4),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

        for (int i = 0; i < 8; i++) {
            exe.execute(new Task(i));
        }

        System.out.println(exe.getQueue());

        exe.execute(new Task(100));

        System.out.println(exe.getQueue());

        exe.shutdown();
    }

    /**
     * 管理多个Future的结果
     */
    private static void testCompletableFuture() {

        long start = System.currentTimeMillis();

        // 第一种
        CompletableFuture<Double> f1 = CompletableFuture.supplyAsync(() -> c1());
        CompletableFuture<Double> f2 = CompletableFuture.supplyAsync(() -> c2());
        CompletableFuture<Double> f3 = CompletableFuture.supplyAsync(() -> c3());

        CompletableFuture.allOf(f1, f2, f3).join();

        // 第二种
        CompletableFuture.supplyAsync(()->c1())
                .thenApply(String::valueOf)
                .thenApply(str -> "price " + str)
                .thenAccept(System.out::println);

        long end = System.currentTimeMillis();

        System.out.println("completable future " + (end - start));
    }

    public static Double c1 (){ msleep(new Random().nextInt(500)); return 1.0; }
    public static Double c2 (){ msleep(new Random().nextInt(500)); return 2.0; }
    public static Double c3 (){ msleep(new Random().nextInt(500)); return 3.0; }

    private static void testCollableFuture() {

        ExecutorService service = Executors.newCachedThreadPool();
        Future<Integer> future = service.submit(() -> {
            return 1;
        });

        try {
            System.out.println(future.get()); // 阻塞
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        service.shutdown();
    }

    public static void testFutureTask(){

        FutureTask<Integer> task = new FutureTask<>(()->{
            return 1;
        });

        new Thread(task).start();

        try {
            System.out.println(task.get()); // 阻塞
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}


class MyExcutor implements Executor {

    @Override
    public void execute(Runnable command) {

    }
}
