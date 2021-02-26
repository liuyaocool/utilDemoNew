package com.liuyao.tankFactory.factory;

/**
 * 皮肤工厂
 *
 */
public abstract class FacSkinFactory {
    public abstract FacTank createTank();
    public abstract FacBullet createBullet();
    public abstract FacExplode createExplode();
}
