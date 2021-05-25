package com.liuyao;

public class Test {

    public static void main(String[] args) {
        System.out.println(T1.count); // 3
        System.out.println(T2.count); // 2
    }

}

class T1{
    public static int count = 2;
    public static T1 t = new T1();
    private T1() { count++; }
}
class T2{
    public static T2 t = new T2();
    public static int count = 2;
    private T2() { count++; }
}
