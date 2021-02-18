package com.liuyao.tank;

import com.liuyao.tank.enumm.Dir;
import com.liuyao.tank.enumm.Group;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class TankFrame extends Frame {

    public static final int GAME_WIDTH = 800, GAME_HEIGHT = 600;

    private Tank myTank = new Tank(200, 200, Dir.DOWN, this, Group.GOOD);
    public ArrayList<Bullet> bullets = new ArrayList<>();
    public ArrayList<Tank> tanks = new ArrayList<>();
    public ArrayList<Explode> explodes = new ArrayList<>();

    public TankFrame() throws HeadlessException {
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        this.setResizable(false);
        this.setTitle("tank war");
        this.setVisible(true);

        this.addKeyListener(new TankKeyAdapter());
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });

    }

    Image offScreenImage = null;
    @Override
    public void update(Graphics g) {
        if (null == offScreenImage){
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        // 用双缓冲解决闪烁问题（不重要）
        // repaint - update
        // 截获update
        // 首先把该画出来的东西（坦克， 子弹）先画在内存的图片中，图片大小和游戏画面一致
        // 把内存中图片一次性画到屏幕上（内存的内容复制到显存）
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    @Override
    public void paint(Graphics g) {
        // 窗口需要重新绘制的时候 自动调用
        // x,y 是从整个窗口左上角为(0,0) 包括关闭按钮

        Color c = g.getColor();
        g.setColor(Color.WHITE);
        g.drawString("bullets: " + bullets.size(), 10, 60);
        g.drawString("tanks: " + tanks.size(), 10, 80);
        g.drawString("explodes: " + explodes.size(), 10, 100);
        g.setColor(c);

        myTank.paint(g);

        // 有异常: list 的 iteator 只能在这里删除, 其他地方删除 迭代会有问题
//        for (Bullet b :bullets){
//            b.paint(g);
//        }
        // 画子弹
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).paint(g);
        }
        // 画坦克
        for (int i = 0; i < tanks.size(); i++) {
            tanks.get(i).paint(g);
        }
        // 画爆炸
        for (int i = 0; i < explodes.size(); i++) {
            explodes.get(i).paint(g);
        }
        // 碰撞检测
        for (int i = 0; i < bullets.size(); i++) {
            for (int j = 0; j < tanks.size(); j++) {
                bullets.get(i).collideWith(tanks.get(j));
            }
        }
    }

    class TankKeyAdapter extends KeyAdapter {

        boolean bL = false;
        boolean bU = false;
        boolean bR = false;
        boolean bD = false;
        
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT: bL = true; break;
                case KeyEvent.VK_UP: bU = true; break;
                case KeyEvent.VK_RIGHT: bR = true; break;
                case KeyEvent.VK_DOWN: bD = true; break;
                default: break;
            }
            setMainDir();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT: bL = false; break;
                case KeyEvent.VK_UP: bU = false; break;
                case KeyEvent.VK_RIGHT: bR = false; break;
                case KeyEvent.VK_DOWN: bD = false; break;
                case KeyEvent.VK_Q: // 打子弹
                    myTank.fire();
                    break;
                default: break;
            }
            setMainDir();
        }

        private void setMainDir() {
            if (!bL && !bU && !bR && !bD) {
                myTank.setMoving(false);
            } else {
                myTank.setMoving(true);
                if (bL) myTank.setDir(Dir.LEFT);
                if (bU) myTank.setDir(Dir.UP);
                if (bR) myTank.setDir(Dir.RIGHT);
                if (bD) myTank.setDir(Dir.DOWN);
            }
        }

    }

}
