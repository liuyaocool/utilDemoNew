package com.liuyao.demo.zookeeper.getData_forConfigCenter;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class DefaultWatch implements Watcher {

    @Override
    public void process(WatchedEvent event) {

        final ConcurrentHashMap<Object, Object> map = new ConcurrentHashMap<>();

        map.put("a", "a");

        map.get("a");

        final ThreadLocal threadLocal = new ThreadLocal();

        threadLocal.set("A");


        Executors
//                .newCachedThreadPool();
                .newFixedThreadPool(1);



    }

    public static void main(String[] args) {
        Integer a = 400;
        add(a);
        System.out.println(a);
    }

    private static void add(Integer a) {
        a++;
        System.out.println("add : a=" + a);
    }


}
