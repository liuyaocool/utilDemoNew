package com.liuyao.demo.mashibing.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 高并发九
 * JMH 性能测试 最好单独一台机器用命令行启动测试
 *  1 添加依赖
 *      org.openjdk.jmh
 *      jmh-core
 *      1.21
 *
 *      org.openjdk.jmh
 *      jmh-generator-annprocess
 *      1.21
 *  2 安装 idea 插件 -- JMH plugin (2020版本是JMH JAVA M*** H*** ***)
 *  3 打开运行程序注解配置
 *      compiler → Annotation Processors → 勾选Enable annotation processors
 *      由于用到了注解 而注解需要写一个程序去解释 JMH对注解进行处理
 *  4 编写测试程序
 *      见 Test_T13_JMH.java
 *
 */
public class T13_JMH {

    static List<Integer> list = new ArrayList<>();

    static {
        Random r = new Random();
        for (int i = 0; i < 1000; i++) {
            list.add(1000000 + r.nextInt(1000000));
        }
    }

    public static void foreach(){
        list.forEach(v -> isPrime(v));
    }

    public static void paraller(){
        list.parallelStream().forEach(T13_JMH::isPrime);
    }

    private static boolean isPrime(int num) {
        for (int i = 2; i < num / 2; i++) {
            if (0 == num % i){
                return false;
            }
        }
        return true;
    }


}
