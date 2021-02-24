package com.liuyao.design_patterns.factory.F03_abstract;

/**
 * 现代
 */
public class ModernFactory extends AbstractFactory {
    @Override
    public AbFood createFood() {
        return new Bread();
    }

    @Override
    public AbWeapon createWeapon() {
        return new AK47();
    }

    @Override
    public AbVehicle createVehicle() {
        return new Car();
    }
}
