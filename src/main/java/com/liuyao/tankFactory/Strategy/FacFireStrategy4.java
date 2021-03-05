package com.liuyao.tankFactory.Strategy;

import com.liuyao.tank.Bullet;
import com.liuyao.tank.TankFrame;
import com.liuyao.tank.enumm.Dir;
import com.liuyao.tank.enumm.Group;
import com.liuyao.tankFactory.FacTankFrame;
import com.liuyao.tankFactory.factory.FacTank;

// 四个方向的子弹
public class FacFireStrategy4 implements FacFireStrategy {

    private static FacFireStrategy4 instance = new FacFireStrategy4();
    private FacFireStrategy4() {}
    public synchronized static FacFireStrategy4 neww(){
        return instance;
    }

    @Override
    public void fire(FacTank tank) {
        for (Dir dir :Dir.values()){
            tank.tankFrame.bullets.add(tank.factory.createBullet(tank.tankFrame, tank.x, tank.y, dir));
        }
    }
}
