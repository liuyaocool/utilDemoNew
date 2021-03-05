package com.liuyao.tank;

import com.liuyao.tank.enumm.Dir;
import com.liuyao.tank.enumm.Group;

public class Start {


    /**
     * 代码重构
     *  工厂:
     *      抽象出父类 用到哪个抽哪个 不要一次性全部抽象 --这样思路不清晰 容易乱
     *      然后 一步一测试
     * @param args
     */
    public static void main(String[] args) {
        // 窗口类
        TankFrame tf = new TankFrame();

        int tankcount = Integer.parseInt(PropertyMgr.getProperty("initTankCount"));
        // 初始化敌方坦克
        for (int i = 0; i < tankcount; i++) {
            tf.tanks.add(new Tank(100 + i * 80, 100, Dir.DOWN, tf, Group.BAD));
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
