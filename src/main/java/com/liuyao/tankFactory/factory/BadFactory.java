package com.liuyao.tankFactory.factory;

import com.liuyao.tankFactory.FacTankFrame;

public class BadFactory extends FacSkinFactory{

    @Override
    public FacTank createTank(FacTankFrame facTankFrame) {
        return new BadTank(facTankFrame, this);
    }

    @Override
    public FacBullet createBullet(FacTankFrame facTankFrame) {
        return null;
    }

    @Override
    public FacExplode createExplode(FacTankFrame facTankFrame) {
        return null;
    }
}
