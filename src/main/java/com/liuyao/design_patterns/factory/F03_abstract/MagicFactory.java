package com.liuyao.design_patterns.factory.F03_abstract;

/**
 * 魔法工长
 */
public class MagicFactory extends AbstractFactory {
    @Override
    public AbFood createFood() {
        return new MushRoom();
    }

    @Override
    public AbWeapon createWeapon() {
        return new MagicStick();
    }

    @Override
    public AbVehicle createVehicle() {
        return new Broom();
    }
}
