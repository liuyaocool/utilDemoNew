package com.liuyao.design_patterns.factory.F01_F02;

/**
 *
 */
public class F02_CarFactory {

    public Moveable create(){
        System.out.println("a car created.");
        return new ICar();
    }


}
