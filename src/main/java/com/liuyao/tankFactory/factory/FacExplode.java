package com.liuyao.tankFactory.factory;

import com.liuyao.tankFactory.FacTankFrame;

import java.awt.*;

public abstract class FacExplode extends FacTankParent {

    public FacExplode(FacTankFrame tankFrame, FacSkinFactory factory, int x, int y) {
        super(tankFrame, factory, x, y);
    }
}
