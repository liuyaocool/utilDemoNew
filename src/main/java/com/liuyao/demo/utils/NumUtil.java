package com.liuyao.demo.utils;

public class NumUtil {

    public static String printBin(int i){
        StringBuilder sb = new StringBuilder();
        for (int j = 31; j >= 0; j--) {
            sb.append((i & (1 << j)) == 0 ? "0" : "1");
            if (j%4 == 0 && j != 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}
