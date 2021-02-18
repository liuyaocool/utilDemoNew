package com.liuyao.demo.test;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

public class TestTreeMap {


    public static void main(String[] args){
        TreeSet ts = new TreeSet(new MyCompare());
        ts.add("A");
        ts.add("C");
        ts.add("E");
        ts.add("D");
        ts.add("B");
        ts.add("G");
        ts.add("F");
        Iterator ite = ts.iterator();
        while (ite.hasNext()){
            Object el = ite.next();
            System.out.println(el + " ");
        }
    }
}


class MyCompare implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        String a1,a2;
        a1 = (String)o1;
        a2 = (String)o2;

        return a1.compareTo(a2);

    }
}
