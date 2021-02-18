package com.liuyao.demo.utilutil;

import java.util.*;

public class Test {

    public static void main(String[] args) {

        System.out.println("aaa" == "aaa");

        for (int i = 0; i < 3; i++) {
            Scanner sc = new Scanner(System.in);
            sc.hasNext();
            System.out.println("输出: "+sc.next());
        }

        System.out.println("123asd".startsWith(""));

//        UncodeAndChar
        String aa = null;
        System.out.println("null".equals(String.valueOf(aa)));
        System.out.println(4 ^ 3);
        System.out.println(2 ^ 5);
        System.out.println((2 ^ 5) << 4);
        System.out.println(7 << 4);
        System.out.println((2 ^ 5) << 4 ^ 3);

//        大小写 数字 8位正则
        String aaaa = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}";
        System.out.println("aa".matches(aaaa));
        System.out.println("aaaaaaaaa".matches(aaaa));
        System.out.println("AA".matches(aaaa));
        System.out.println("AAAAAAAA".matches(aaaa));
        System.out.println("11".matches(aaaa));
        System.out.println("11111111".matches(aaaa));
        System.out.println("aaAA".matches(aaaa));
        System.out.println("aaAAaaaaa".matches(aaaa));
        System.out.println("aa11".matches(aaaa));
        System.out.println("aaasdadsfdsf11".matches(aaaa));
        System.out.println("AA11".matches(aaaa));
        System.out.println("AA11342343".matches(aaaa));
        System.out.println("AAaa11".matches(aaaa));
        System.out.println("AAaa112233445".matches(aaaa));


        System.out.println("aa.sva.aa".indexOf(".",3));


        List<String> list = new ArrayList<>();
        list.add("1231231232j");
        System.out.println(list.contains("1231231232J"));



    }
}
