package com.liuyao.demo.utilutil;


import com.baidu.aip.speech.AipSpeech;
import com.liuyao.demo.utils.HttpUtils;
import com.liuyao.demo.utils.IOUtil;
import com.liuyao.demo.utils.ObjectUtil;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import it.sauronsoftware.jave.Encoder;
//import it.sauronsoftware.jave.MultimediaInfo;

public class BaiduAudioUtil {

    private static final String APP_ID = "18619918",
            API_KEY = "w9SykzDLBLz1aWuSlHrLcBM5",
            SECRET_KEY = "xGNa5HopFMMFyRsMsnVvcEeEafVe6F5s",
            USER_ID = "65bab402-305c-4011-8fb5-73786ac34e9a",//区分用户唯一标识
            //token请求接口
            TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials"
                    + "&client_id=" + API_KEY + "&client_secret=" + SECRET_KEY,
            API_ADDR = "http://vop.baidu.com/server_api";//语音识别接口

    /**
     * 获得token
     */
    private static com.alibaba.fastjson.JSONObject getToken(){
        String result = HttpUtils.ajax(TOKEN_URL, HttpUtils.Method.GET, null, null);
        System.out.println(result);
        return HttpUtils.toJson(result);
    }

    /**
     * 获得语音接口带参地址
     * @param token
     * @param devPid
     * 1537	普通话(纯中文识别)	输入法模型	有标点	支持自定义词库
     * 1737	英语	            英语模型	    无标点	不支持自定义词库
     * 1637	粤语	            粤语模型	    有标点	不支持自定义词库
     * 1837	四川话	        四川话模型	有标点	不支持自定义词库
     * 1936	普通话远场	    远场模型	    有标点	不支持自定义词库
     * @return
     */
    private static String getApiAddr(String token, String devPid){
        StringBuilder sb = new StringBuilder(API_ADDR)
                .append("?cuid=").append(USER_ID)
                .append("&token=").append(token);
        if (!ObjectUtil.isEmpty(devPid)){
            sb.append("&dev_pid=").append(devPid);
        }
        return sb.toString();
    }

    /**
     * https://ai.baidu.com/ai-doc/SPEECH/ek38lxj1u
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
//        com.alibaba.fastjson.JSONObject token = getToken();
//        String accessToken = token.getString("access_token");
        String accessToken = "24.d33947fcb6b1f87128fc312df2880c53.2592000.1587629693.282335-18619918";

        JSONObject data = new JSONObject();
        data.put("format", "pcm");//语音格式
        data.put("rate", 16000);//采样率 固定
        data.put("channel", 1);//声道
        data.put("cuid", USER_ID);
        data.put("token", accessToken);
        File file = new File("D:/test/16k.pcm");
        data.put("speech", IOUtil.fileToBase64(file));
        data.put("len", file.length());
//        data.put("dev_pid", 1537);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");

        HttpURLConnection conn = (HttpURLConnection) new URL(API_ADDR).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setDoOutput(true);
        conn.getOutputStream().write(data.toString().getBytes());
        conn.getOutputStream().close();
        String res = HttpUtils.getResult(conn, HttpUtils.Charset.UTF8);
        System.out.println(res);
//        String result = ConnUtil.getResponseString(conn);

//        String res = HttpUtils.ajax(API_ADDR, HttpUtils.Method.POST, headers, data);

//        System.out.println(audioToText("D:/test/test3_16x.wav"));
//
//        System.out.println(cut("E:\\record.mp3","E:\\record-cut_0_15.mp3", 0, 15)); //大于18（也可能别的值），会读取不了
//      System.out.println(cut("E:\\record_40s.wav","E:\\record-cut_15_30.wav", 15, 30));
//
//        int start = 0;
//        int end = 0;
//        int count = 10;
//        String sourcefile = "E:\\test.wav";
//        long time = getTimeLen(new File(sourcefile));
//        int newTime = (int)time;
//        int internal = newTime - end;
//        while(internal > 0) {
//            if(internal < 10) {
//                cut(sourcefile, "E:\\record-cut_" + start + "_" + (int)time +".wav", start, (int)time);
//                end += count;
//                internal = newTime - end;
//            }else {
//                end += count;
//                cut(sourcefile, "E:\\record-cut_" + start + "_" + end +".wav", start, end);
//                start += count;
//                internal = newTime - end;
//            }
//        }
    }





    //音频转文字
    public static String audioToText(String filePath) throws IOException, InterruptedException {
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        JSONObject res = client.asr(filePath, "wav", 16000, null);
        System.out.println(res);
        return res.getJSONArray("result").toString();
    }

    public static String cover8xTo16x(String filePath) throws IOException, InterruptedException {
        File sourceFile = new File(filePath);
        File ffmpegPath = new File("D:/test/ffmpeg"); //存放ffmpeg程序的目录
        String targetPath = sourceFile.getAbsolutePath().replaceAll(".wav" , "_16x.wav");
        // ffmpeg.exe -i source.wav -ar 16000 target.wav
        List<String> wavToPcm = new ArrayList<String>();
        wavToPcm.add(ffmpegPath.getAbsolutePath());
        wavToPcm.add("-i");
        wavToPcm.add(sourceFile.getAbsolutePath());
        wavToPcm.add("-ar");
        wavToPcm.add("16000");
        wavToPcm.add(targetPath);
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(wavToPcm);
        builder.redirectErrorStream(true);
        Process process = builder.start();
        process.waitFor();
        new File(targetPath);
        return targetPath;
    }

    /**
     * 截取wav音频文件
     * @param sourcepath  源文件地址
     * @param targetpath  目标文件地址
     * @param start  截取开始时间（秒）
     * @param end  截取结束时间（秒）
     *
     * return  截取成功返回true，否则返回false
     */
    public static boolean cut(String sourcefile, String targetfile, int start, int end) {
        try{
            if(!sourcefile.toLowerCase().endsWith(".wav") || !targetfile.toLowerCase().endsWith(".wav")){
                return false;
            }
            File wav = new File(sourcefile);
            if(!wav.exists()){
                return false;
            }
            long t1 = getTimeLen(wav);  //总时长(秒)
            if(start<0 || end<=0 || start>=t1 || end>t1 || start>=end){
                return false;
            }
            FileInputStream fis = new FileInputStream(wav);
            long wavSize = wav.length()-44;  //音频数据大小（44为128kbps比特率wav文件头长度）
            long splitSize = (wavSize/t1)*(end-start);  //截取的音频数据大小
            long skipSize = (wavSize/t1)*start;  //截取时跳过的音频数据大小
            int splitSizeInt = Integer.parseInt(String.valueOf(splitSize));
            int skipSizeInt = Integer.parseInt(String.valueOf(skipSize));

            ByteBuffer buf1 = ByteBuffer.allocate(4);  //存放文件大小,4代表一个int占用字节数
            buf1.putInt(splitSizeInt+36);  //放入文件长度信息
            byte[] flen = buf1.array();  //代表文件长度
            ByteBuffer buf2 = ByteBuffer.allocate(4);  //存放音频数据大小，4代表一个int占用字节数
            buf2.putInt(splitSizeInt);  //放入数据长度信息
            byte[] dlen = buf2.array();  //代表数据长度
            flen = reverse(flen);  //数组反转
            dlen = reverse(dlen);
            byte[] head = new byte[44];  //定义wav头部信息数组
            fis.read(head, 0, head.length);  //读取源wav文件头部信息
            for(int i=0; i<4; i++){  //4代表一个int占用字节数
                head[i+4] = flen[i];  //替换原头部信息里的文件长度
                head[i+40] = dlen[i];  //替换原头部信息里的数据长度
            }
            byte[] fbyte = new byte[splitSizeInt+head.length];  //存放截取的音频数据
            for(int i=0; i<head.length; i++){  //放入修改后的头部信息
                fbyte[i] = head[i];
            }
            byte[] skipBytes = new byte[skipSizeInt];  //存放截取时跳过的音频数据
            fis.read(skipBytes, 0, skipBytes.length);  //跳过不需要截取的数据
            fis.read(fbyte, head.length, fbyte.length-head.length);  //读取要截取的数据到目标数组
            fis.close();

            File target = new File(targetfile);
            if(target.exists()){  //如果目标文件已存在，则删除目标文件
                target.delete();
            }
            FileOutputStream fos = new FileOutputStream(target);
            fos.write(fbyte);
            fos.flush();
            fos.close();
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取音频文件总时长
     * @param filePath  文件路径
     * @return
     */
    public static long getTimeLen(File file){
        long tlen = 0;
//        if(file!=null && file.exists()){
//            Encoder encoder = new Encoder();
//            try {
//                MultimediaInfo m = encoder.getInfo(file);
//                long ls = m.getDuration();
//                tlen = ls/1000;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return tlen;
    }

    /**
     * 数组反转
     * @param array
     */
    public static byte[] reverse(byte[] array){
        byte temp;
        int len=array.length;
        for(int i=0;i<len/2;i++){
            temp=array[i];
            array[i]=array[len-1-i];
            array[len-1-i]=temp;
        }
        return array;
    }


    private static String scope;
    private static String token;
    private static long expiresAt;

    public static String atot() throws Exception {
        String filename = "";

        String baseurl = "http://openapi.baidu.com/oauth/2.0/token";
        String getTokenURL = baseurl + "?grant_type=client_credentials"
                + "&client_id=" + urlEncode(API_KEY)
                + "&client_secret=" + urlEncode(SECRET_KEY);
        System.out.println(getTokenURL);

        URL url = new URL(getTokenURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        int responseCode = conn.getResponseCode();
        InputStream inputStream = conn.getInputStream();
        if (responseCode != 200) {
            System.err.println("http 请求返回的状态码错误，期望200， 当前是 " + responseCode);
            if (responseCode == 401) {
                System.err.println("可能是appkey appSecret 填错");
            }
            System.err.println("response headers" + conn.getHeaderFields());
            if (inputStream == null) {
                inputStream = conn.getErrorStream();
            }
            byte[] result = getInputStreamContent(inputStream);
            System.err.println(new String(result));

            throw new Exception("http response code is" + responseCode);
        }

        return null;
    }

    public static String urlEncode(String str) {
        String result = null;
        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
            .toCharArray();
    public static char[] encode(byte[] data) {
        char[] out = new char[((data.length + 2) / 3) * 4];
        for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
            boolean quad = false;
            boolean trip = false;

            int val = (0xFF & (int) data[i]);
            val <<= 8;
            if ((i + 1) < data.length) {
                val |= (0xFF & (int) data[i + 1]);
                trip = true;
            }
            val <<= 8;
            if ((i + 2) < data.length) {
                val |= (0xFF & (int) data[i + 2]);
                quad = true;
            }
            out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
            val >>= 6;
            out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
            val >>= 6;
            out[index + 1] = alphabet[val & 0x3F];
            val >>= 6;
            out[index + 0] = alphabet[val & 0x3F];
        }
        return out;
    }

    /**
     * 从HttpURLConnection 获取返回的字符串
     *
     * @param conn
     * @return
     * @throws IOException
     * @throws DemoException
     */
    public static String getResponseString(HttpURLConnection conn) throws Exception {
        return new String(getResponseBytes(conn));
    }

    /**
     * 从HttpURLConnection 获取返回的bytes
     * 注意 HttpURLConnection自身问题， 400类错误，会直接抛出异常。不能获取conn.getInputStream();
     *
     * @param conn
     * @return
     * @throws IOException   http请求错误
     * @throws DemoException http 的状态码不是 200
     */
    public static byte[] getResponseBytes(HttpURLConnection conn) throws Exception {
        int responseCode = conn.getResponseCode();
        InputStream inputStream = conn.getInputStream();
        if (responseCode != 200) {
            System.err.println("http 请求返回的状态码错误，期望200， 当前是 " + responseCode);
            if (responseCode == 401) {
                System.err.println("可能是appkey appSecret 填错");
            }
            System.err.println("response headers" + conn.getHeaderFields());
            if (inputStream == null) {
                inputStream = conn.getErrorStream();
            }
            byte[] result = getInputStreamContent(inputStream);
            System.err.println(new String(result));

            throw new Exception("http response code is" + responseCode);
        }

        byte[] result = getInputStreamContent(inputStream);
        return result;
    }

    /**
     * 将InputStream内的内容全部读取，作为bytes返回
     *
     * @param is
     * @return
     * @throws IOException @see InputStream.read()
     */
    public static byte[] getInputStreamContent(InputStream is) throws IOException {
        byte[] b = new byte[1024];
        // 定义一个输出流存储接收到的数据
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 开始接收数据
        int len = 0;
        while (true) {
            len = is.read(b);
            if (len == -1) {
                // 数据读完
                break;
            }
            byteArrayOutputStream.write(b, 0, len);
        }
        return byteArrayOutputStream.toByteArray();
    }

}
