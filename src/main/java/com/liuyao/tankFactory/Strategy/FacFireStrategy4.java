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
        int x = tank.getX();
        int y = tank.getY();
        FacTankFrame tf = tank.getTankFrame();
        Group group = tank.getGroup();
        tank.getTankFrame().bullets.add(new Bullet(x, y, Dir.LEFT, tf, group));
        tank.getTankFrame().bullets.add(new Bullet(x, y, Dir.UP, tf, group));
        tank.getTankFrame().bullets.add(new Bullet(x, y, Dir.RIGHT, tf, group));
        tank.getTankFrame().bullets.add(new Bullet(x, y, Dir.DOWN, tf, group));
    }
}
