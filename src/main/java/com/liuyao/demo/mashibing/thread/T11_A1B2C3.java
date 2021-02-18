package com.liuyao.demo.mashibing.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 马士兵高并发七
 *  123456 ABCDEF 交替打印
 */
public class T11_A1B2C3 {


    public static void main(String[] args) {

        // wait notify

        // lock condition

        // cas

        //pipedStream 效率低

        // TransferQueue 交给对方打印

    }

    public static void lockConsition(){
        Lock lock = new ReentrantLock();

        Condition c1 = lock.newCondition();
        Condition c2 = lock.newCondition();

    }

}
