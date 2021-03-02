package com.liuyao.tankFactory.factory;

import com.liuyao.tank.enumm.Dir;
import com.liuyao.tankFactory.FacTankFrame;

public class GoodFactory extends FacSkinFactory{

    @Override
    public FacTank createTank(FacTankFrame facTankFrame) {
        return new GoodTank(facTankFrame, this,200, 200, Dir.UP);
    }

    @Override
    public FacBullet createBullet(FacTankFrame facTankFrame) {
        return null;
    }

    @Override
    public FacExplode createExplode(FacTankFrame facTankFrame, int x, int y) {
        return null;
    }
}
