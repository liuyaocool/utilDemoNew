package com.liuyao.tank.bullte;

import com.liuyao.tank.Tank;

/**
 * 原类名为 BulletStrategy
 *  思想错误
 *  这不是子弹的策略 而是发射子弹的策略
 */
public interface FireStrategy {
    void fire(Tank tank);
}
