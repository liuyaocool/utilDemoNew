package com.liuyao.design_patterns.factory;

import com.liuyao.design_patterns.factory.F03_abstract.*;
import com.liuyao.design_patterns.factory.F01_F02.*;

public class MainFactory {

    /**
     * 任意定制交通工具
     *  集成Moveable
     * 任意定制生产过程 F02
     *  Moveable XXXFactory.create();
     *  产品维度可扩展
     * 任意定制产品一族
     *  产品族可扩展 族中产品个数不可扩展
     *
     * 名词用抽象类 形容词用接口
     * @param args
     */
    public static void main(String[] args) {

        // F01
        Moveable m1 = new ICar();
        m1.go();
        // F02
        Moveable m2 = new F02_CarFactory().create();
        m2.go();
        // F03
        Car c = new Car();
        AK47 w = new AK47();
        w.shoot();
        Bread b = new Bread();
        b.printName();

        // 产品族
//        AbstractFactory abFactory = new ModernFactory();
        AbstractFactory abFactory = new MagicFactory();
        AbVehicle vehicle = abFactory.createVehicle();
        vehicle.go();
        AbFood food = abFactory.createFood();
        food.printName();
        AbWeapon weapon = abFactory.createWeapon();
        weapon.shoot();

    }
}
