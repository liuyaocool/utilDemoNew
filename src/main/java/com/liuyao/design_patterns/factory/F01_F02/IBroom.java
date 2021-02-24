package com.liuyao.design_patterns.factory.F01_F02;

public class IBroom implements Moveable {
    @Override
    public void go() {
        System.out.println("broom fly...");
    }
}
