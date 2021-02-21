package com.liuyao.tank.bullte;

import com.liuyao.tank.Bullet;
import com.liuyao.tank.Tank;

import java.awt.image.BufferedImage;

// 默认前方的子弹
public class FireStrategy1 implements FireStrategy {

    private static FireStrategy1 instance = new FireStrategy1();

    private FireStrategy1() {}

    public synchronized static FireStrategy1 neww(){
        return instance;
    }

    @Override
    public void fire(Tank tank) {
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
