package com.liuyao.demo.ttt;

import java.util.Comparator;
import java.util.TreeMap;

public class TestStringSort {

    private static final Comparator<String> comparator = new Comparator<String>() {
        public int compare(String o1, String o2) {
            int len1 = o1.length(), len2 = o2.length(), minLen = Math.min(len1, len2);
            for (int i = 0; i < minLen; i++) {
                int diff = o1.charAt(i) - o2.charAt(i);
                if (0 == diff) { continue; }
                return diff;
            }
            return len1 == len2 ? 0 : len1 - len2;
        }
    };

    public static void main(String[] args) {
        TreeMap<String, Object> map = new TreeMap<>(comparator);

        map.put("Adsdfsdf", 1);
        map.put("Adsdfsdfaaa", 1);
        map.put("Adsdfsdsf", 1);
        map.put("Adsdfsdsf", 1);
        map.put("Adadsf", 1);
        map.put("sfdg", 1);
        map.put("ukrjthj", 1);
        map.put("ffffff", 1);

        for (String s : map.keySet()) {
            System.out.println(s);
        }

    }
}
