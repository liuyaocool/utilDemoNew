package com.liuyao.tank.bullte;

import com.liuyao.tank.Bullet;
import com.liuyao.tank.Tank;
import com.liuyao.tank.TankFrame;
import com.liuyao.tank.enumm.Dir;
import com.liuyao.tank.enumm.Group;

// 四个方向的子弹
public class BulletStrategy4 implements BulletStrategy{

    private static BulletStrategy4 instance = new BulletStrategy4();

    private BulletStrategy4() {}

    public synchronized static BulletStrategy4 neww(){
        return instance;
    }

    @Override
    public void action(Tank tank) {
        int x = tank.getX();
        int y = tank.getY();
        TankFrame tf = tank.getTankFrame();
        Group group = tank.getGroup();
        tank.getTankFrame().bullets.add(new Bullet(x, y, Dir.LEFT, tf, group));
        tank.getTankFrame().bullets.add(new Bullet(x, y, Dir.UP, tf, group));
        tank.getTankFrame().bullets.add(new Bullet(x, y, Dir.RIGHT, tf, group));
        tank.getTankFrame().bullets.add(new Bullet(x, y, Dir.DOWN, tf, group));
    }
}
