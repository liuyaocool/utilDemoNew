package com.liuyao.tank.factory;

import com.liuyao.tank.enumm.Dir;

import java.awt.image.BufferedImage;

public abstract class AbTank {

    protected static AbTank tank;
    protected BufferedImage u, l, r, d;

    public BufferedImage getImage(Dir dir) {
        switch (dir) {
            case LEFT: return l;
            case UP: return u;
            case RIGHT: return r;
            case DOWN: return d;
        }
        return null;
    }

    public BufferedImage getImageU(){ return u;};
    public BufferedImage getImageR(){ return r;};
    public BufferedImage getImageL(){ return l;};
    public BufferedImage getImageD(){ return d;};
}
