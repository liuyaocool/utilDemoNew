package com.liuyao.design_patterns.factory.F03_abstract;

public abstract class AbstractFactory {
    public abstract AbFood createFood();
    public abstract AbWeapon createWeapon();
    public abstract AbVehicle createVehicle();
}
