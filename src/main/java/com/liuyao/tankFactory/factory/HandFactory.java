package com.liuyao.tankFactory.factory;

import com.liuyao.tank.enumm.Dir;
import com.liuyao.tankFactory.FacTankFrame;

public class HandFactory extends FacSkinFactory{

    @Override
    public FacTank createTank(FacTankFrame facTankFrame) {
        return new HandTank(facTankFrame, this,200, 200, Dir.UP);
    }

    @Override
    public FacBullet createBullet(FacTankFrame tf, int x, int y, Dir dir) {
        return new HandBullet(tf, this, x, y, dir);
    }

    @Override
    public FacExplode createExplode(FacTankFrame tf, int x, int y) {
        return new Explode(tf, this, x, y);
    }
}
