package com.liuyao.demo.gis;

/**
 * @author Lyu
 * @date 2019/11/6  17:01
 * @Description: 绘制实现接口
 **/

public interface Render {

    public  void drawContour(double startX, double startY, double endX, double endY, double contourLevel);
}
