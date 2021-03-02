package com.liuyao.tankFactory.factory;

import com.liuyao.tank.enumm.Dir;
import com.liuyao.tankFactory.FacTankFrame;

public class AutoFactory extends FacSkinFactory{

    @Override
    public FacTank createTank(FacTankFrame facTankFrame) {
        return new AutoTank(facTankFrame, this);
    }

    @Override
    public FacBullet createBullet(FacTankFrame tf, int x, int y, Dir dir) {
        return new AutoBullet(tf, this, x, y, dir);
    }

    @Override
    public FacExplode createExplode(FacTankFrame tf, int x, int y) {
        return new Explode(tf, this, x, y);
    }
}
