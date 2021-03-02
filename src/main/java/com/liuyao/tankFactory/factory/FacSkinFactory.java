package com.liuyao.tankFactory.factory;

import com.liuyao.tank.enumm.Dir;
import com.liuyao.tankFactory.FacTankFrame;

/**
 * 皮肤工厂
 *
 */
public abstract class FacSkinFactory {
    public abstract FacTank createTank(FacTankFrame tf);
    public abstract FacBullet createBullet(FacTankFrame tf, int x, int y, Dir dir);
    public abstract FacExplode createExplode(FacTankFrame tf, int x, int y) ;
}
