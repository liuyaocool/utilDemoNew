package com.liuyao.tankFactory.factory;

import com.liuyao.tank.Tank;
import com.liuyao.tank.TankFrame;
import com.liuyao.tank.TankParent;
import com.liuyao.tankFactory.FacTankFrame;

import java.awt.image.BufferedImage;

public abstract class FacBullet extends FacTankParent {


    public FacBullet(FacTankFrame tankFrame, FacSkinFactory factory, int x, int y) {
        super(tankFrame, factory, x, y);
    }

    public abstract BufferedImage getImgU();
    public abstract BufferedImage getImgL();
    public abstract BufferedImage getImgR();
    public abstract BufferedImage getImgD();
    protected abstract void move();

    public void collideWith(FacTank tank) {
        if (this.group == tank.group) return;
        if (this.rectangle.intersects(tank.rectangle)){
            tank.die();
            this.die();
            tankFrame.explodes.add(this.factory.createExplode(this.tankFrame, this.x, this.y));
        }
    }

}
