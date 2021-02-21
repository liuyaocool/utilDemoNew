package com.liuyao.tank.bullte;

import com.liuyao.tank.Bullet;
import com.liuyao.tank.Tank;
import com.liuyao.tank.TankFrame;
import com.liuyao.tank.enumm.Dir;
import com.liuyao.tank.enumm.Group;

import java.awt.image.BufferedImage;
import java.util.List;

// 默认前方的子弹
public class BulletStrategy1 implements BulletStrategy{

    private static BulletStrategy1 instance = new BulletStrategy1();

    private BulletStrategy1() {}

    public synchronized static BulletStrategy1 neww(){
        return instance;
    }

    @Override
    public void action(Tank tank) {
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
