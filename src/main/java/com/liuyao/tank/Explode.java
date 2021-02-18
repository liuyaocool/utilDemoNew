package com.liuyao.tank;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

// 爆炸
public class Explode extends TankParent{

    public static BufferedImage[] explodes = new BufferedImage[16];
    public static final int WIDTH, HEIGHT;
    static {
        for(int i=0; i<16; i++) explodes[i] = readImg("e" + (i+1) + ".gif");
        HEIGHT = explodes[0].getHeight();
        WIDTH = explodes[0].getWidth();
    }

    private int step = 0;

    public Explode(TankFrame tankFrame, int x, int y) {
        super(tankFrame, x, y);
    }

    @Override
    public void paint(Graphics g){
        g.drawImage(explodes[step++], this.x, this.y, null);
        if (step >= explodes.length) tankFrame.explodes.remove(this);
    }
}
