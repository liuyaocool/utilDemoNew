package com.liuyao.jvm;

public class MainExecuteModel {


    /**
     * 添加执行VM参数
     *  -Xint
     *  -Xcomp
     *  -Xmixed 默认
     * @param args
     */
    public static void main(String[] args) {

        for (int i = 0; i < 10_0000; i++) {
            m();
        }

        long t = System.currentTimeMillis();
        for (int i = 0; i < 10_0000; i++) {
            m();
        }
        System.out.println(System.currentTimeMillis() - t);

    }

    public static void m() {
        for(long i=0; i<10_0000L; i++) {
            long j = i%3;
        }
    }
}
