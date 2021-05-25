package com.liuyao.demo.ttt;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class TestSHA1 {

    static Mac mac;
    static {
        try {
            mac = Mac.getInstance("HmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    static String secretKey = "MNzikcenxQZ1GcW89tTmsEG8BZHyeq&";

    public static void main(String[] args) {
        final String asdfg = "asdfg";

        byte[] cast = new byte[asdfg.length()];
        for (int c = 0; c < cast.length; c++) {
            cast[c] = (byte) asdfg.charAt(c);
        }

//        System.out.println(getSha1("asdfg"));
        System.out.println(toBase64(hamcsha1(asdfg.getBytes())));
        secretKey = "ADfaDSFasdgfASDgASDgASD";
        System.out.println(toBase64(hamcsha1(asdfg.getBytes())));

    }

    public static String getSha1(String str) {

        char[] hexDigits = secretKey.toCharArray();
        MessageDigest mdTemp = null;
        try {
            mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] md = mdTemp.digest();
        int j = md.length;
        char buf[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = md[i];
            buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
            buf[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(buf);
    }

    public static byte[] hamcsha1(byte[] data) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), "HmacSHA1");
            mac.init(signingKey);
            return mac.doFinal(data);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    //二行制转字符串
    public static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b!=null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF); // 越界变负数 这里回正数
            if (stmp.length() == 1) {
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

    public static String toBase64(byte[] data)  {
        final Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }

    public static String testBase64() throws UnsupportedEncodingException {
        final Base64.Decoder decoder = Base64.getDecoder();
        final Base64.Encoder encoder = Base64.getEncoder();
        final String text = "字串文字";
        final byte[] textByte = text.getBytes("UTF-8");
//编码
        final String encodedText = encoder.encodeToString(textByte);
        System.out.println(encodedText);
//解码
        System.out.println(new String(decoder.decode(encodedText), "UTF-8"));

        return encodedText;
    }
}
