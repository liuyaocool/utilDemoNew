package com.liuyao.tankFactory;

import com.liuyao.tank.PropertyMgr;
import com.liuyao.tank.Tank;
import com.liuyao.tank.TankFrame;
import com.liuyao.tank.enumm.Dir;
import com.liuyao.tank.enumm.Group;
import com.liuyao.tankFactory.factory.BadFactory;
import com.liuyao.tankFactory.factory.FacSkinFactory;
import com.liuyao.tankFactory.factory.GoodFactory;

public class FacStart {

    // 进度 第7节 第1部分 00:00
    public static void main(String[] args) {
        // 窗口类
        FacTankFrame tf = new FacTankFrame(new GoodFactory());

        FacSkinFactory fac = new BadFactory();
        int tankcount = Integer.parseInt(PropertyMgr.getProperty("initTankCount"));
        // 初始化敌方坦克
        for (int i = 0; i < tankcount; i++) {
            tf.tanks.add(fac.createTank(tf));
        }

        while (true) {
            try {
                Thread.sleep(50);
                tf.repaint();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }




    }
}
