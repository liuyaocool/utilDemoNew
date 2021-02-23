package com.liuyao.tank.factory;

/**
 * 皮肤工厂
 *
 */
public abstract class AbSkinFactory {
    public abstract AbTank createTank();
    public abstract AbBullet createBullet();
    public abstract AbExplode createExplode();
}
