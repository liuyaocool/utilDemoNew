package com.liuyao.design_patterns.factory.F01_F02;

public class ICar implements Moveable{
    @Override
    public void go() {
        System.out.println("car dididi...");
    }
}
