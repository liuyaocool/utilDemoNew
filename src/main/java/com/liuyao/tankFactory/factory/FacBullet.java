package com.liuyao.tankFactory.factory;

import com.liuyao.tank.Tank;
import com.liuyao.tank.TankFrame;
import com.liuyao.tank.TankParent;
import com.liuyao.tankFactory.FacTankFrame;

public abstract class FacBullet extends FacTankParent {

    public FacBullet(FacTankFrame tankFrame, int x, int y) {
        super(tankFrame, x, y);
    }

    protected abstract void move();
    public abstract void collideWith(FacTank tank);

}
