package com.liuyao.demo.utils;

import sun.misc.BASE64Decoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类
 */
public class SecurityUtil {

    //解密base64
    public static String decodeBase64(String s) {
        if (s == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(s);
            return new String(b,"utf-8");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 利用java原生的类实现SHA256加密
     * @return
     */
    public static String encryptSHA256(String str){

        MessageDigest messageDigest;
        String res = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            res = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 将byte转为16进制
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes){
        StringBuffer res = new StringBuffer();
        String temp;
        for (int i=0;i<bytes.length;i++){
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1){
                //1得到一位的进行补0操作
                res.append("0");
            }
            res.append(temp);
        }
        return res.toString();
    }


    public static void main(String[] args) {
        byte[] aa = {-12, -122, -34, -1, 0};
        System.out.println(byte2Hex(aa));
    }

}
