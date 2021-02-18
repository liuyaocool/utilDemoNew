package com.liuyao.demo.test;

import java.util.ArrayList;
import java.util.List;

public class TestLogic {

    public static void main(String[] args) {

        List<String> exportPersonList = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            exportPersonList.add(String.valueOf(i));
        }
        int divisor = 80;
        //先取整数
        int countOne = (int)Math.floor(exportPersonList.size()/divisor);
        //后取取模
        int lastCountOne = exportPersonList.size()%divisor;
        System.out.println("======================for 循环开始 ====================================");
        for(int z=0; z<countOne; z++) {
            List<String> aa = exportPersonList.subList(divisor*z, divisor*(z+1));
            System.out.println(aa.size());
            for (int i = 0; i < aa.size(); i++) {
                System.out.println(aa.get(i));
            }
        }
        System.out.println("===================== if 判断开始 =====================================");
        if(lastCountOne>0) {
            List<String> aa = exportPersonList.subList(divisor*countOne, exportPersonList.size());
            System.out.println(aa.size());
            for (int i = 0; i < aa.size(); i++) {
                System.out.println(aa.get(i));
            }
        }
        System.out.println("==========================================================");
    }
}
