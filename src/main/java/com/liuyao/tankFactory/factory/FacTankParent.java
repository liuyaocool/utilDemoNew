package com.liuyao.tankFactory.factory;

import com.liuyao.tank.TankFrame;
import com.liuyao.tank.enumm.Group;
import com.liuyao.tankFactory.FacTankFrame;

import java.awt.*;

public abstract class FacTankParent {

    public FacTankFrame tankFrame;
    public FacSkinFactory factory;
    public int x, y, width, height;
    public boolean living = true;
    public Group group;
    public Rectangle rectangle;

    public FacTankParent(FacTankFrame tankFrame, FacSkinFactory factory, int x, int y) {
        this.tankFrame = tankFrame;
        this.factory = factory;
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
