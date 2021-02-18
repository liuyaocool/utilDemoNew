package com.liuyao.demo.utilutil;

import com.alibaba.fastjson.JSONArray;
import com.liuyao.demo.utils.IOUtil;
import com.liuyao.demo.utils.ServletUtil;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpUtilsss extends IOUtil {

    private String host;
    private String url;
    private String sessionId;
    private String type = "POST";
    private String contentType = "application/json; charset=utf-8";
    private String encoding = "utf-8";
    private Map<String, String> data;


    public String doRequest(){
        return doPost(getAddress(), jsonData(this.data), this.type, this.contentType, this.sessionId, this.encoding, 0);
    }

    public HttpUtilsss() {
        HttpServletRequest request = ServletUtil.getRequest();
        StringBuilder sb = new StringBuilder(request.getScheme());
        sb.append("://").append(request.getLocalAddr()).append(":")
                .append(request.getLocalPort()).append("/");
        this.host = sb.toString();
    }

    public String getAddress(){
        if (this.url.startsWith("/") && this.host.endsWith("/")){
            return this.host + this.url.substring(1);
        } else if (!this.url.startsWith("/") && !this.host.endsWith("/")){
            return this.host + "/" + this.url;
        } else {
            return this.host + this.url;
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getHost() {
        return host;
    }

    public String getType() {
        return type.toUpperCase();
    }

    public void setType(String type) {
        this.type = type.toUpperCase();
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public static String doGet(String reqUrl, String encoding, int timeout){
        encoding = isEmpty(encoding) ? "utf-8" : encoding;
        try{
            HttpURLConnection conn = obtainConnection(reqUrl, timeout);
            conn.setRequestMethod("GET");
//            conn.setRequestProperty("accept", "*/*");
//            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//            conn.connect();
            //返回结果
            return findResponse(conn, encoding);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String doPost(String reqUrl, String data, String type, String contentType,
                                   String sessionId, String encoding, int timeout){
        encoding = isEmpty(encoding) ? "utf-8" : encoding;

        OutputStream os = null;
        try{
            HttpURLConnection conn = obtainConnection(reqUrl, timeout);
            conn.setRequestMethod(type);
            if (!isEmpty(contentType)){
                conn.setRequestProperty("Content-Type", contentType);
            }
            if (!isEmpty(sessionId)){
                conn.setRequestProperty("Cookie", "JSESSIONID="+sessionId+".node0");
            }
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            os = conn.getOutputStream();
            if (!isEmpty(data)){
                os.write(data.getBytes(encoding));
            }
            os.flush();
            //返回结果
            return findResponse(conn, encoding);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(os);
        }
        return null;
    }

    private static HttpURLConnection obtainConnection(String reqUrl, int timeout) throws IOException {
        URL url = new URL(reqUrl);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        if (timeout > 0) {
            conn.setReadTimeout(timeout);
            conn.setConnectTimeout(timeout);
        }
        return conn;
    }

    private static String findResponse(HttpURLConnection conn, String encoding) {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        String result = null;
        try {
            System.out.println(conn.getResponseCode());
            if (200 == conn.getResponseCode()) {
                is = conn.getInputStream();
            }else {
                is = conn.getErrorStream();
            }
            isr = new InputStreamReader(is, encoding);
            br = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
        } catch (IOException e){
            e.printStackTrace();
        }finally {
            close(is);
            close(isr);
            close(br);
        }
        return result;
    }

    public static String jsonData(Map<String, String> params){
        JSONObject json = new JSONObject();
        if (null != params && params.size() > 0) {
            for (String key : params.keySet()) {
                json.put(key, params.get(key));
            }
        }
        return json.toString();
    }

    public static String normalData(Map<String, String> params){
        StringBuilder sb = new StringBuilder();
        if (null != params && params.size() > 0) {
            for (String key : params.keySet()) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(key).append("=").append(params.get(key));
            }
        }
        return sb.toString();
    }

    public static boolean isEmpty(String str){
        return null == str || "".equals(str.trim());
    }

    public static String doGet(String url, String contentType) throws Exception{
        contentType = isEmpty(contentType) ? "application/json" : contentType;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet method = new HttpGet(url);
        method.setHeader("Accept", contentType);
        method.setHeader("Content-type", contentType);
        CloseableHttpResponse response = client.execute(method);

        String result = EntityUtils.toString(response.getEntity());// 返回json格式：
        return result;
    }

    public static void upLoadFilePost(String actionUrl, Map<String, File> files) throws IOException {
        System.out.println("=========================请求=================================");
        System.out.println(actionUrl);
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";
        URL uri = new URL(actionUrl);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
//        conn.setReadTimeout(5 * 1000);
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false);
        conn.setRequestMethod("POST"); // Post方式
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

        for (String k : conn.getRequestProperties().keySet()) {
            for (int i = 0; i < conn.getRequestProperties().get(k).size(); i++) {
                System.out.println(k + ":" + conn.getRequestProperties().get(k).get(i));
            }
        }
        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
        System.out.println("---------------------------------------------------");

        // 发送文件数据
        if (files != null)
            for (Map.Entry<String, File> file : files.entrySet()) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                sb1.append("Content-Disposition: form-data; name=\"files\"; filename=\"" + file.getKey() + "\"" + LINEND);
                sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
                sb1.append(LINEND);
                System.out.println(sb1);
                outStream.write(sb1.toString().getBytes());
                InputStream is = new FileInputStream(file.getValue());
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }

                is.close();
                outStream.write(LINEND.getBytes());
            }
        System.out.println("-------------------------------");

        // 请求结束标志
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        outStream.write(end_data);
        outStream.flush();

        System.out.println("=========================响应=================================");
        // 得到响应码
        System.out.println("responseCode："+conn.getResponseCode());
        if (conn.getResponseCode() == 200) {
            InputStream in = conn.getInputStream();
            InputStreamReader isReader = new InputStreamReader(in);
            BufferedReader bufReader = new BufferedReader(isReader);
            String line;StringBuilder sb = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                sb.append(line);
            }
            com.alibaba.fastjson.JSONObject datas = com.alibaba.fastjson.JSONObject.parseObject(sb.toString());
            System.out.println(datas.getInteger("code"));
            System.out.println(datas.getString("message"));
            JSONArray res = datas.getJSONArray("data");
            for (int i = 0; i < res.size(); i++) {
                System.out.println(res.get(i));
            }
            outStream.close();
            conn.disconnect();
        }
        outStream.close();
        conn.disconnect();
    }

}
