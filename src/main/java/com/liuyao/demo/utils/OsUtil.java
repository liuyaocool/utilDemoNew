package com.liuyao.demo.utils;

public class OsUtil {

    public static int getCPUNums() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static void main(String[] args) {
        System.out.println("CPU核心数：" + getCPUNums());
    }
}
