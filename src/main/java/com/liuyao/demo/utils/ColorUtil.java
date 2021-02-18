package com.liuyao.demo.utils;

public class ColorUtil {

    /**
     * 透明色合并
     * @param f 前
     * @param b 后
     * @return
     */
    public static double[] add(double[] f, double[] b){

        double[] res = new double[4];

        //浮点数版
        double fa = f[3] / 256;
        double ba = b[3] / 256;
        double a = 1 - (1 - fa) * (1 - ba);

        for (int i = 0; i < 3; i++) {
            res[i] = (fa * f[i] + (1 - fa) * ba * b[i]) / a;
        }
        res[3] = a * 256;

        return res;
    }


    /**
     * 透明色减法
     * 经过上述公式逆推得 f - b;
     * f + b = f;
     * @param res 合并后的颜色
     * @param b 下层颜色
     * @return f 返回上层颜色
     */
    public static double[] reduce(double[] res, double[] b){
        double[] f = new double[4];

        double resa = res[3] / 256;
        double ba = b[3] / 256;
        double fa = 1 - (1 - resa) / (1 - ba);
        for (int i = 0; i < 3; i++) {
            f[i] = (res[i] * resa - (1 - fa) * ba * b[i]) / fa;
        }
        f[3] = fa * 256;

        return f;
    }



    public static void main(String[] args) {
        double[] b = {127.5,255,127.5,0.2};
        double[] f = {255,255,0,0.1};

        double[] res = add(f, b);
        for (int i = 0; i < res.length; i++) {
            System.out.println(res[i]);
        }
        System.out.println();
        res = reduce(res , f);
        for (int i = 0; i < res.length; i++) {
            System.out.println(res[i]);
        }

    }
}
