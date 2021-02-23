package com.liuyao.tank.factory;

import com.liuyao.tank.ImgUtil;
import com.liuyao.tank.enumm.Dir;

import java.awt.image.BufferedImage;

public class GoodTank extends AbTank{

    public GoodTank(){
        this.u = ImgUtil.readImg("GoodTank.png");
        this.l = ImgUtil.rotateImage(u, -90);
        this.r = ImgUtil.rotateImage(u, 90);
        this.d = ImgUtil.rotateImage(u, 180);
    }

}
