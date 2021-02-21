package com.liuyao.tank.bullte;

import com.liuyao.tank.Bullet;
import com.liuyao.tank.Tank;
import com.liuyao.tank.TankFrame;
import com.liuyao.tank.enumm.Dir;
import com.liuyao.tank.enumm.Group;

// 四个方向的子弹
public class FireStrategy4 implements FireStrategy {

    private static FireStrategy4 instance = new FireStrategy4();

    private FireStrategy4() {}

    public synchronized static FireStrategy4 neww(){
        return instance;
    }

    @Override
    public void fire(Tank tank) {
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
