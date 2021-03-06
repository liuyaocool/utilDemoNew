package com.liuyao.demo.utils;

import com.alibaba.fastjson.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.http.MimeHeaders;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;

public class HttpUtils {

    public enum Method{

        OPTIONS("OPTIONS"), POST("POST"), GET("GET"), HEAD("HEAD"),
        PUT("PUT"), DELETE("DELETE"), CONNECT("CONNECT"), TRACE("TRACE");
        private String method;
        Method(String method) { this.method = method; }
        public String getMethod() { return method; }
    }

    public enum Charset{
        UTF8("utf-8");
        private String code;
        Charset(String code) { this.code = code; }
        public String getCode() { return code; }
    }


    /**
     * 在JAVA代码开发过程中需要用的HTTP请求发送和接收消息。
     * 碰到了个很奇怪的问题，是通过HttpURLConnection的setRequestMethod方法已经设置了请求方式为GET，可最终传递到服务端的却是POST。
     * 通过调试发现关键问题出在getOutputStream方法上。
     * POST请求是将发送的内容放在请求正文中，而GET是放在url之后。两种不同的请求最好用两个不同的方法，或判断。
     *
     * //如果请求方式为POST
     * if("POST".equals(requestMethod)){
     *     PrintWriter printout = new PrintWriter(new OutputStreamWriter(httpurlcon.getOutputStream()));
     *     printout.write(contents);
     *     printout.flush();
     *     printout.close();
     *      ...
     * }
     * GET不需要用 OutputStream将内容写入。
     *
     * @param url
     * @param method
     * @param headers
     * @param data
     * @return
     */
    public static byte[] ajax(String url, Method method, Map<String, String> headers, Map data){
        Charset encoding = Charset.UTF8;
        OutputStreamWriter osw = null;
        try{
            HttpURLConnection conn = getConn(url, method, headers);
//            System.out.println("param：" + data);
            //添加其他设置
            conn.setDoOutput(true);

//            conn.setConnectTimeout(5000);
//            conn.setReadTimeout(5000);
            //添加数据包
            switch (method){
                case POST:
                    osw = new OutputStreamWriter(conn.getOutputStream(), encoding.getCode());
                    if (null != data && data.size() > 0){
                        JSONObject json = new JSONObject();
                        for (Object key : data.keySet()) {
                            json.put(key, data.get(key));
                        }
                        osw.write(json.toString());
                    }
                    osw.flush();
                    break;
                default: break;
            }
            //请求
            conn.connect();
            //并获得响应数据
            byte[] result;
            if (conn.getResponseCode() == 200) {
                return byteResult(conn, 500);
            } else {
                result = String.valueOf(conn.getResponseCode()).getBytes();
            }
            conn.disconnect();
            return result;
        }catch (Exception e){
//            e.printStackTrace();
            System.out.println("ERROR：" + e.getMessage());
        }finally {
            close(osw);
        }
        return null;
    }

    private static byte[] byteResult(HttpURLConnection conn, Integer timeout) {
        List<byte[]> ret = new ArrayList<>();
        int len;
        int total = 0;
        byte[] result;
        try {
            InputStream is = conn.getInputStream();
            while ((len = is.available()) > 0) {
                is.read(result = new byte[len]);
                ret.add(result);
                total += len;
                Thread.sleep(timeout);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        result = new byte[total];
        len = 0;
        for (byte[] b: ret) {
            System.arraycopy(b, 0, result, len, b.length);
            len += b.length;
        }
        return result;
    }

    public static void fileRequest(String url, Method method, Map<String, File> files) {
        String BOUNDARY = java.util.UUID.randomUUID().toString(),
                PREFIX = "--",
                LINEND = "\r\n",
                MULTIPART_FROM_DATA = "multipart/form-data";
        Charset encoding = Charset.UTF8;
        DataOutputStream dos = null;
        InputStream is = null;

        Map<String, String> headers = new HashMap<>();
        headers.put("connection", "keep-alive");
        headers.put("Charsert", "UTF-8");
        headers.put("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

        try {
            HttpURLConnection conn = getConn(url, method, headers);

//        conn.setReadTimeout(5 * 1000);
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setUseCaches(false);

            dos = new DataOutputStream(conn.getOutputStream());

            // 发送文件数据
            if (ObjectUtil.isEmpty(files)){
                return;
            }
            for (Map.Entry<String, File> file : files.entrySet()) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                sb1.append("Content-Disposition: form-data; name=\"files\"; filename=\"" + file.getKey() + "\"" + LINEND);
                sb1.append("Content-Type: application/octet-stream; charset=" + encoding + LINEND);
                sb1.append(LINEND);

                dos.write(sb1.toString().getBytes());
                is = new FileInputStream(file.getValue());
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    dos.write(buffer, 0, len);
                }
                close(is);
                dos.write(LINEND.getBytes());
            }

            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            dos.write(end_data);
            dos.flush();

            // 得到响应码
            System.out.println("responseCode："+conn.getResponseCode());
            if (conn.getResponseCode() == 200) {
                String result = getResult(conn, encoding);

                com.alibaba.fastjson.JSONObject datas = com.alibaba.fastjson.JSONObject.parseObject(result);
                JSONArray res = datas.getJSONArray("data");
                for (int i = 0; i < res.size(); i++) {
                    System.out.println(res.get(i));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(dos);
            close(is);
        }

    }

    private static HttpURLConnection getConn(String url, Method method,
                                             Map<String, String> headers) throws IOException {
        HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
        conn.setRequestMethod(method.getMethod());
        StringBuilder sb = new StringBuilder(conn.getRequestMethod()).append(" ").append(url).append(" ");
        //添加请求头
        if (!ObjectUtil.isEmpty(headers)){
            for(String key: headers.keySet()){
                conn.setRequestProperty(key, headers.get(key));
                sb.append(key).append("=").append(headers.get(key)).append("; ");
            }
        }
        System.out.println(sb.toString());
        return conn;
    }

    public static String getResult(HttpURLConnection conn, Charset encoding){
        InputStreamReader isr = null;
        BufferedReader br = null;
        String result = null;
        try {
            isr = new InputStreamReader(conn.getInputStream(), encoding.getCode());
            br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null){
                sb.append(line);
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            close(isr);
            close(br);
            conn.disconnect();
        }
        return result;
    }


    public static void close(AutoCloseable clo){
        try {
            if (null != clo){ clo.close(); }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T extends com.alibaba.fastjson.JSON> T toJson(String jsonStr) {
        return (T) com.alibaba.fastjson.JSON.parse(jsonStr);
    }

    public static void main(String[] args) throws IOException {
        System.out.println(UUID.randomUUID().toString());
        Map<String, String> headers = new HashMap<>();
        Map<String, String> data = new HashMap<>();

        String addr = "http://127.0.0.1:8083/all/getPage";
        addr = "http://1770.ztshangwu.com/?channelNo=1770#/video?video_id=153";
        headers.put("Content-Type", "text/html");
        headers.put("User-Agent", "iPhone");
        byte[] res = ajax(addr, Method.GET, headers, data);
        System.out.println(new String(res));
//        headers.clear();
//        doAjax(addr + "getString", Method.GET, headers, data);//ok
//        doAjax(addr + "getJsonString", Method.GET, headers, data);//ok
//        doAjax(addr + "getJsonMap", Method.GET, headers, data);//ok
//        doAjax(addr + "postString", Method.POST, headers, data);
//        doAjax(addr + "postJsonString", Method.POST, headers, data);//ok
//        doAjax(addr + "postJson", Method.POST, headers, data);//ok
//        doAjax(addr + "postJsonMap", Method.POST, headers, data);//ok

    }

    //保留方式
    public static <T extends com.alibaba.fastjson.JSON> T doGet(String url) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet method = new HttpGet(url);
//        method.setHeader("Accept", "application/json");
//        method.setHeader("Content-type", "application/json");

        Object jsonResult = null;
        try {
            CloseableHttpResponse response = client.execute(method);
            String result = EntityUtils.toString(response.getEntity());// 返回json格式：
            jsonResult = com.alibaba.fastjson.JSON.parse(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (T) jsonResult;
    }


    public static void printHeaders(HttpServletRequest request) {
        try {
            Enumeration<String> headers = request.getHeaderNames();
            Class<? extends Enumeration> clz = headers.getClass();
            Field h1 = clz.getDeclaredField("headers");
            h1.setAccessible(true);
            final MimeHeaders o = (MimeHeaders) h1.get(headers);
            for (int i = 0; i < o.size(); i++) {
                System.out.println(o.getName(i) + ": " + o.getValue(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
