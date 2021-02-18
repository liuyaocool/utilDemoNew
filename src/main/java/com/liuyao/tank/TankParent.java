package com.liuyao.tank;

import com.liuyao.tank.enumm.Group;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class TankParent extends ImgUtil{

    protected TankFrame tankFrame;
    protected int x, y, width, height;
    protected boolean living = true;
    protected Group group;
    protected Rectangle rectangle;

    public TankParent(TankFrame tankFrame, int x, int y) {
        this.tankFrame = tankFrame;
        this.x = x;
        this.y = y;
    }

    protected void updateRect(){
        if (null == this.rectangle) rectangle = new Rectangle();

        this.rectangle.x = this.x;
        this.rectangle.y = this.y;
        this.rectangle.width = this.width;
        this.rectangle.height = this.height;
    }

    protected void die(){
        this.living = false;
    };

    public abstract void paint(Graphics g);
}
