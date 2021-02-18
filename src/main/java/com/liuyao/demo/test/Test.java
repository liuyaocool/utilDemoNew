package com.liuyao.demo.test;

import com.liuyao.demo.entity.Hero;

public class Test {

    public static void main(String[] args) {

//        System.out.println(new Double("12.1").intValue());
//        System.out.println(new Double("12.1").toString());
//        System.out.println(new Double("12.0").intValue());
//        System.out.println(12.0%1 == 0);
//        System.out.println(12.1%1 == 0);

        int count = 0;
        int times = 1000000;
        double max = 0.6;
        for (int i = 0; i < times; i++) {
            double r = Math.random();
            if (Math.sqrt(r) < 0.6) count ++;
        }
        System.out.println(count);

        int x = 1310 - 1540;int y = 167 - 136;ss(x,y);
        x = 1540 - 1800;y = 136 - 78;ss(x, y);

        x = 2470 - 2890;y = 194 - 148;ss(x, y);
        x = 2890 - 3380;y = 148 - 22;ss(x, y);

//        Hero h = (Hero) new Object();
//        System.out.println(h.getClass());

        System.out.println(Hero.class.getName());

        Aa aa = new Aa();;
        aa.get("a");
        aa.get();
        aa.get(1, 2, 3);

    }

    private static void ss(int x, int y){
        System.out.println(Math.sqrt(x*x+y*y));
    }
}

class Aa{

    void get(){
        System.out.println("get()");
    }

    void get(Object k){
        System.out.println("get(Object k)");
    }
    void get(Object... k){
        System.out.println("get(Object... k)");
    }
}