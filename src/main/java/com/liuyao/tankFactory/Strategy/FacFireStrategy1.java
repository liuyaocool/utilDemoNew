package com.liuyao.tankFactory.Strategy;

import com.liuyao.tank.Bullet;
import com.liuyao.tankFactory.factory.FacTank;

import java.awt.image.BufferedImage;

// 默认前方的子弹
public class FacFireStrategy1 implements FacFireStrategy {

    private static FacFireStrategy1 instance = new FacFireStrategy1();
    private FacFireStrategy1() {}
    public synchronized static FacFireStrategy1 neww(){
        return instance;
    }

    @Override
    public void fire(FacTank tank) {
        BufferedImage bimg;
        switch (tank.getDir()){
            case LEFT: bimg = Bullet.bulletL; break;
            case UP: bimg = Bullet.bulletU; break;
            case RIGHT: bimg = Bullet.bulletR; break;
            case DOWN: bimg = Bullet.bulletD; break;
            default: return;
        }
        int bx = tank.getX() + tank.getWidth()/2 - bimg.getWidth()/2;
        int by = tank.getY() + tank.getHeight()/2 - bimg.getHeight()/2;
        Bullet b = new Bullet(bx, by, tank.getDir(), tank.getTankFrame(), tank.getGroup());
        tank.getTankFrame().bullets.add(b);
    }
}
