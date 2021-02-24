package com.liuyao.design_patterns.strategy;

import java.util.Arrays;

public class Main {

    /**
     * 封装的是 做一件事情 有不同的执行方式
     *  如: Cat( implements Comparator).java CatHeightComparator.java
     *  如：坦克打什么样的子弹
     * @param args
     */
    public static void main(String[] args) {
//        int[] a = {9, 2, 3, 5, 7, 6, 5, 1, 7, 0};
        Cat[] a = {
                new Cat(3,4),
                new Cat(5,3),
                new Cat(1,5),
                new Cat(2,1)
        };
        SorterNormal<Cat> sorter = new SorterNormal();
        sorter.sort(a);
        System.out.println(Arrays.toString(a));
        SorterStrategy<Cat> sorterStrategy = new SorterStrategy<>();
        // 策略模式 传入排序策略
        sorterStrategy.sort(a, new CatHeightComparator());
        sorterStrategy.sort(a, (o1, o2) -> {
            if(o1.weight < o2.weight) return -1;
            else if(o1.weight > o2.weight) return 1;
            else return 0;
        });
        System.out.println(Arrays.toString(a));
    }
}
