package com.liuyao.tankFactory.factory;

import com.liuyao.tank.ImgUtil;
import com.liuyao.tankFactory.Strategy.FacFireStrategy;
import com.liuyao.tankFactory.Strategy.FacFireStrategy1;
import com.liuyao.tank.TankFrame;
import com.liuyao.tank.enumm.Dir;
import com.liuyao.tank.enumm.Group;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BadTank extends FacTank {

    private static final int SPEED = 5;
    protected static BufferedImage u = ImgUtil.readImg("BadTank.png"),
            l = ImgUtil.rotateImage(u, -90),
            r = ImgUtil.rotateImage(u, 90),
            d = ImgUtil.rotateImage(u, 180);

    protected FacFireStrategy bulletStrategy = FacFireStrategy1.neww();

    public BadTank(TankFrame tankFrame, int x, int y) {
        super(tankFrame, x, y);
    }

    @Override
    public void fire() {
        bulletStrategy.fire(this);
    }

    @Override
    protected void move() {
        if (!this.moving) return;
        switch (this.dir) {
            case LEFT: x -= SPEED; break;
            case RIGHT: x += SPEED; break;
            case UP: y -= SPEED; break;
            case DOWN: y += SPEED; break;
        }
        if (this.group == Group.BAD && RANDOM.nextInt(100) > 95) this.fire();

        if (this.group == Group.BAD && RANDOM.nextInt(100) > 95) randomDir();

        // 边界检测
        boundsCheck();

        updateRect();
    }

    private void randomDir() {
        this.dir = Dir.values()[RANDOM.nextInt(4)];
    }

    @Override
    public void paint(Graphics g) {
        if (!this.living) {
            this.tankFrame.tanks.remove(this);
        }
        BufferedImage img;
        switch (this.dir) {
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

}
