package com.liuyao.demo.mashibing.thread;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * 马士兵高并发六
 * 容器
 *  物理: 连续 链表
 */
public class T10_Container {

//    Hashtable; Vector// 全是加锁的方法

//    Map m = Collections.synchronizedMap(new HashMap()); // 插快 读慢
//    Hashtable // 插快 读慢
//    ConcurrentHashMap // 插慢 读快 底层CAS

//    Vector // 线程安全
//    ArrayList // 线程不安全

//    ConcurrentLinkedQueue // 线程安全 poll()

//    HashMap // 没有排序
//    TreeMap // 红黑树 排序的
//    ConcurrentHashMap // 没有排序的
//    ConcurrentSkipListMap // 跳表排序

//    CopyOnWriteArrayList // 写时复制 插入时会copy一份,替换原数组 读不加锁

//    Queue
//    对线程友好的api:
//    offer()-加进去返回true poll()-取,会remove peak()-取,不会remove
//    ConcurrentLinkedQueue //

//    LinkedBlockingDeque // put()-满了会阻塞 take()-空了会阻塞 容量无限制
//    ArrayBlockingQueue // 个数限制 满了阻塞

//    DelayQueue
//    SynchronousQueue // 容量为0 多线程传数据

//    TransferQueue // 消费过后才会继续执行
//    LinkedTransferQueue

    public static void main(String[] args) {

        print("aaaccvfgafgADFfagfg");

        System.out.println((int )'a');
        System.out.println((int )'A');

        LinkedBlockingQueue<Object> objects = new LinkedBlockingQueue<>();
        ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue<>(20);
        ConcurrentLinkedQueue<Object> objects1 = new ConcurrentLinkedQueue<>();
        DelayQueue<Delayed> delayeds = new DelayQueue<>();

        // 消费过后继续执行
        testLinkedTransferQueue();
    }

    static void print(String str){
        TreeMap<Character, Integer> map = new TreeMap<>();
        for (char s: str.toCharArray()){
            if (! map.containsKey(s)){
                map.put(s, 1);
                continue;
            }
            map.put(s, map.get(s)+1);
        }
        for (char s: map.keySet()){
            System.out.print(s);
            System.out.print("(" + map.get(s) + ")");
        }
    }

    /**
     * 消费过后才会继续执行
     */
    private static void testLinkedTransferQueue() {
        LinkedTransferQueue<String> queue = new LinkedTransferQueue();

        new Thread(()->{
            try {
                queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            // 等有线程取走才会继续执行,否则阻塞在这
            queue.transfer("aaa");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("testLinkedTransferQueue 继续执行.");
    }

    public static void tessHashTable1() {

    }

}
