package com.liuyao.design_patterns.factory.F01_F02;

/**
 * 简单工长
 * 可扩展性不好
 */
public class F02_PlaneFactory {

    public Moveable create(){
        System.out.println("a plane created.");
        return new IPlane();
    }
}
