package com.liuyao.tankFactory.factory;

import com.liuyao.tank.ImgUtil;
import com.liuyao.tank.TankFrame;
import com.liuyao.tank.TankParent;
import com.liuyao.tankFactory.FacTankFrame;

import java.awt.*;
import java.awt.image.BufferedImage;

// 爆炸
public class Explode extends FacExplode {

    public static BufferedImage[] explodes = new BufferedImage[16];
    public static final int WIDTH, HEIGHT;
    static {
        for(int i=0; i<16; i++) explodes[i] = ImgUtil.readImg("e" + (i+1) + ".gif");
        HEIGHT = explodes[0].getHeight();
        WIDTH = explodes[0].getWidth();
    }

    private int step = 0;

    public Explode(FacTankFrame tankFrame, FacSkinFactory fac, int x, int y) {
        super(tankFrame, fac, x, y);
    }

    @Override
    public void paint(Graphics g){
        g.drawImage(explodes[step++], this.x, this.y, null);
        if (step >= explodes.length) tankFrame.explodes.remove(this);
    }
}
