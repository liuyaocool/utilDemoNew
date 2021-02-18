package com.liuyao.tank;

import com.liuyao.tank.enumm.Dir;
import com.liuyao.tank.enumm.Group;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Tank extends TankParent {

    private static final int SPEED = 5;
    public static BufferedImage goodTankU = readImg("GoodTank.png"),
            goodTankL = rotateImage(goodTankU, -90),
            goodTankR = rotateImage(goodTankU, 90),
            goodTankD = rotateImage(goodTankU, 180);
    public static BufferedImage badTankU = readImg("BadTank.png"),
            badTankL = rotateImage(badTankU, -90),
            badTankR = rotateImage(badTankU, 90),
            badTankD = rotateImage(badTankU, 180);

    private Dir dir = Dir.DOWN;
    private boolean moving = true;
    private Random random = new Random();

    public Dir getDir() { return dir; }
    public void setDir(Dir dir) { this.dir = dir; }
    public boolean isMoving() { return moving; }
    public void setMoving(boolean moving) { this.moving = moving; }

    public Tank(int x, int y, Dir dir, TankFrame tf, Group group) {
        super(tf, x, y);
        this.dir = dir;
        this.group = group;
    }

    public void paint(Graphics g) {
        if (!living) {
            tankFrame.tanks.remove(this);
        }
        BufferedImage img;
        switch (this.group){
            case BAD:
                switch (this.dir) {
                    case LEFT: img = badTankL; break;
                    case RIGHT: img = badTankR; break;
                    case UP: img = badTankU; break;
                    case DOWN: img = badTankD; break;
                    default: return;
                }
                break;
            case GOOD:
                switch (this.dir) {
                    case LEFT: img = goodTankL; break;
                    case RIGHT: img = goodTankR; break;
                    case UP: img = goodTankU; break;
                    case DOWN: img = goodTankD; break;
                    default: return;
                }
                break;
            default: return;
        }
        g.drawImage(img, x, y, null);
        this.width = img.getWidth();
        this.height = img.getHeight();
        
        move();
    }

    private void move() {
        if (!moving) return;
        switch (dir) {
            case LEFT: x -= SPEED; break;
            case RIGHT: x += SPEED; break;
            case UP: y -= SPEED; break;
            case DOWN: y += SPEED; break;
        }
        if (this.group == Group.BAD && random.nextInt(100) > 95) this.fire();

        if (this.group == Group.BAD && random.nextInt(100) > 95) randomDir();

        // 边界检测
        boundsCheck();

        updateRect();

    }

    private void boundsCheck() {
        if (this.x < 2) x = 2;
        if (this.y < 28) y = 28;
        if (this.x > TankFrame.GAME_WIDTH- this.width -2) x = TankFrame.GAME_WIDTH - this.width -2;
        if (this.y > TankFrame.GAME_HEIGHT - this.height -2 ) y = TankFrame.GAME_HEIGHT -this.height -2;
    }

    private void randomDir() {
        this.dir = Dir.values()[random.nextInt(4)];
    }

    public void fire() {
        BufferedImage bimg;
        switch (this.dir){
            case LEFT: bimg = Bullet.bulletL; break;
            case UP: bimg = Bullet.bulletU; break;
            case RIGHT: bimg = Bullet.bulletR; break;
            case DOWN: bimg = Bullet.bulletD; break;
            default: return;
        }
        int bx = this.x + this.width/2 - bimg.getWidth()/2;
        int by = this.y + this.height/2 - bimg.getHeight()/2;
        Bullet b = new Bullet(bx, by, this.dir, this.tankFrame, this.group);
        this.tankFrame.bullets.add(b);
    }
}
