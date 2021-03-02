package com.liuyao.tankFactory.factory;

import com.liuyao.tank.Explode;
import com.liuyao.tank.Tank;
import com.liuyao.tank.TankFrame;
import com.liuyao.tank.enumm.Dir;
import com.liuyao.tank.enumm.Group;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GooBullet extends FacBullet {

    public static BufferedImage u = readImg("Bullet.png"),
            l = rotateImage(u, -90),
            r = rotateImage(u, 90),
            d = rotateImage(u, 180);
    private static final int SPEED = 20;

    public Dir dir;

    public GooBullet(TankFrame tankFrame, int x, int y, Dir dir, Group group) {
        super(tankFrame, x, y);
        this.dir = dir;
        this.group = Group.GOOD;
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

    @Override     //碰撞检测
    public void collideWith(FacTank tank) {
        if (this.group == tank.group) return;
        if (this.rectangle.intersects(tank.rectangle)){
            tank.die();
            this.die();
            tankFrame.explodes.add(tank.factory.createExplode(this.tankFrame, this.x, this.y));
        }
    }
}
