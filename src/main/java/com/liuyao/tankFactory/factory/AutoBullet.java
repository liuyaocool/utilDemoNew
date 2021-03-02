package com.liuyao.tankFactory.factory;

import com.liuyao.tank.ImgUtil;
import com.liuyao.tank.TankFrame;
import com.liuyao.tank.enumm.Dir;
import com.liuyao.tank.enumm.Group;
import com.liuyao.tankFactory.FacTankFrame;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AutoBullet extends FacBullet {

    public static BufferedImage u = ImgUtil.readImg("Bullet.png"),
            l = ImgUtil.rotateImage(u, -90),
            r = ImgUtil.rotateImage(u, 90),
            d = ImgUtil.rotateImage(u, 180);
    private static final int SPEED = 20;

    public Dir dir;

    public AutoBullet(FacTankFrame tankFrame, FacSkinFactory fac, int x, int y, Dir dir) {
        super(tankFrame, fac, x, y);
        this.factory = fac;
        this.dir = dir;
        this.group = Group.AUTO;
    }

    @Override
    public void paint(Graphics g) {
        if (!living) {
            tankFrame.bullets.remove(this);
            return;
        }
        BufferedImage img;
        switch (dir) {
            case LEFT: img = l; break;
            case RIGHT: img = r; break;
            case UP: img = u; break;
            case DOWN: img = d; break;
            default: return;
        }
        g.drawImage(img, x, y, null);
        this.width = img.getWidth();
        this.height = img.getHeight();

        move();
    }

    @Override
    public BufferedImage getImgU() {
        return u;
    }

    @Override
    public BufferedImage getImgL() {
        return l;
    }

    @Override
    public BufferedImage getImgR() {
        return r;
    }

    @Override
    public BufferedImage getImgD() {
        return d;
    }

    @Override
    protected void move() {
        switch (dir) {
            case LEFT: x -= SPEED; break;
            case RIGHT: x += SPEED; break;
            case UP: y -= SPEED; break;
            case DOWN: y += SPEED; break;
        }

        updateRect();

        if (x < 0 || y < 0 || x > TankFrame.GAME_WIDTH || y > TankFrame.GAME_HEIGHT) living = false;
    }

}
