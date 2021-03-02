package com.liuyao.tankFactory.Strategy;

import com.liuyao.tank.Bullet;
import com.liuyao.tankFactory.factory.FacBullet;
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

        FacBullet bullet = tank.factory.createBullet(
                tank.tankFrame, 0, 0, tank.getDir());
        BufferedImage bimg;
        switch (tank.getDir()){
            case LEFT: bimg = bullet.getImgL(); break;
            case UP: bimg = bullet.getImgU(); break;
            case RIGHT: bimg = bullet.getImgR(); break;
            case DOWN: bimg = bullet.getImgD(); break;
            default: return;
        }
        bullet.x = tank.x + tank.width/2 - bimg.getWidth()/2;
        bullet.y = tank.y + tank.height/2 - bimg.getHeight()/2;
        tank.tankFrame.bullets.add(bullet);
    }
}
