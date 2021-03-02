package com.liuyao.tankFactory.factory;

import com.liuyao.tank.TankFrame;
import com.liuyao.tank.enumm.Dir;
import com.liuyao.tankFactory.FacTankFrame;
import com.liuyao.tankFactory.Strategy.FacFireStrategy;
import com.liuyao.tankFactory.Strategy.FacFireStrategy1;

import java.util.Random;

public abstract class FacTank extends FacTankParent {
    protected static final Random RANDOM = new Random();

    protected Dir dir = Dir.DOWN;
    protected boolean moving = true;
    protected FacFireStrategy bulletStrategy = FacFireStrategy1.neww();

    public FacFireStrategy getBulletStrategy() { return bulletStrategy; }
    public void setBulletStrategy(FacFireStrategy bulletStrategy) { this.bulletStrategy = bulletStrategy; }
    public Dir getDir() { return dir; }
    public void setDir(Dir dir) { this.dir = dir; }
    public boolean getMoving() { return moving; }
    public void setMoving(boolean moving) { this.moving = moving; }

    public FacTank(FacTankFrame tankFrame, FacSkinFactory factory, int x, int y) {
        super(tankFrame, factory, x, y);
    }

    protected void boundsCheck() {
        if (this.x < 2) x = 2;
        if (this.y < 28) y = 28;
        if (this.x > TankFrame.GAME_WIDTH - this.width - 2) x = TankFrame.GAME_WIDTH - this.width - 2;
        if (this.y > TankFrame.GAME_HEIGHT - this.height - 2) y = TankFrame.GAME_HEIGHT -this.height - 2;
    }

    public abstract void fire();
    protected abstract void move();
}
