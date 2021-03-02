package com.liuyao.tankFactory.factory;

import com.liuyao.tankFactory.FacTankFrame;

/**
 * 皮肤工厂
 *
 */
public abstract class FacSkinFactory {
    public abstract FacTank createTank(FacTankFrame facTankFrame);
    public abstract FacBullet createBullet(FacTankFrame facTankFrame);
    public abstract FacExplode createExplode(FacTankFrame facTankFrame, int x, int y);
}
