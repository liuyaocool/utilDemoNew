package com.liuyao.demo.test;


import com.liuyao.demo.anno.achieve.MyAnnoFuncAch;

public class TestAnnotation {

    public static void main(String[] args) {

        try {
            MyAnnoFuncAch.achieveAnno("com.liuyao.demo.entity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
