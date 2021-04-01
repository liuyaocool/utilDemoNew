package com.liuyao.demo.test;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaTest {

    public static void main(String[] args) {

        Cache<String, String> cache = CacheBuilder.newBuilder()
//                .maximumSize(2)
                .expireAfterWrite(2, TimeUnit.SECONDS)
                .build();

        cache.put("a", "aa");
        cache.put("b", "bb");
        cache.put("c", "cc");

        System.out.println(cache.size());
        System.out.println(cache.getIfPresent("a"));
        System.out.println(cache.getIfPresent("b"));
        System.out.println(cache.getIfPresent("c"));

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println(cache.size());
        System.out.println(cache.getIfPresent("a"));
        System.out.println(cache.getIfPresent("b"));
        System.out.println(cache.getIfPresent("c"));

//
//        Callable<Object> objectCallable = new Callable<Object>(){
//
//            @Override
//            public Object call() throws Exception {
//                return null;
//            }
//        };
//        try {
//            System.out.println(cache.get("a", () -> {
//                return null;
//            }));
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }


    }
}
