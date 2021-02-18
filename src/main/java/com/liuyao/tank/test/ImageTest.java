package com.liuyao.tank.test;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ImageTest {

    @Test
    void tes() throws IOException {

        BufferedImage image = ImageIO.read(new File("D:\\PROJECT\\utilDemo\\src\\main\\resources\\static\\img\\tank.jpg"));
        assertNotNull(image); // 断言 不是空值

        /**
         * 每个 *.class 都是Class对象的一个实例, 且都被一个ClassLoader加载到内存
         * 既然class实例(src路径下,jar包里同样能)能load到内存, 那么肯定能拿到这个
         * 路径下的其他资源, 作为流的形式 --getResourceAsStream()
         */
        BufferedImage img2 = ImageIO.read(ImageTest.class.getClassLoader().
                getResourceAsStream("static/img/tank.jpg"));
        assertNotNull(img2);
    }
}
