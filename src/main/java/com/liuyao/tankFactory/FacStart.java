package com.liuyao.tankFactory;

import com.liuyao.tank.PropertyMgr;
import com.liuyao.tankFactory.factory.AutoFactory;
import com.liuyao.tankFactory.factory.FacSkinFactory;
import com.liuyao.tankFactory.factory.HandFactory;

public class FacStart {

    public static void main(String[] args) {
        // 窗口类
        FacTankFrame tf = new FacTankFrame(new HandFactory());

        FacSkinFactory fac = new AutoFactory();
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
