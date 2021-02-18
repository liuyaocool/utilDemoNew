package com.liuyao.demo.ws.client;

import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class HttpWs {

    private String host;
    private String url;
    private ConcurrentHashMap<String, String> data;

//    public String doPost(){
//        PrintWriter out = null;
//        BufferedReader in = null;
//        String result = "";
//        try {
//            URL realUrl = new URL(url);
//            // 打开和URL之间的连接
//            URLConnection conn = realUrl.openConnection();
//            // 设置通用的请求属性
//            conn.setRequestProperty("accept", "*/*");
//            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("user-agent",
//                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//            conn.setRequestProperty("Charsert", "UTF-8");
//            // 发送POST请求必须设置如下两行
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            // 获取URLConnection对象对应的输出流
//            out = new PrintWriter(conn.getOutputStream());
//            // 发送请求参数
//            out.print(param);
//            // flush输出流的缓冲
//            out.flush();
//            // 定义BufferedReader输入流来读取URL的响应
//            in = new BufferedReader(
//                    new InputStreamReader(conn.getInputStream(),"utf-8"));
//            String line;
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
//        } catch (Exception e) {
//            System.out.println("发送 POST 请求出现异常！" + e);
//            e.printStackTrace();
//        }
//        // 使用finally块来关闭输出流、输入流
//        finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//                if (in != null) {
//                    in.close();
//                }
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return result;
//    }

    /**
       * 根据POST请求获取请求内容
       * @param url
       * @return
       */
    public String doPost(){
        String result = "";
        try {
            HttpClient client = new HttpClient();
            PostMethod method = new PostMethod(this.url);
            method.addRequestHeader("Content-type","application/json");
            for (String key : this.data.keySet()) {
                method.addParameter(key, this.data.get(key));
            }
            client.executeMethod(method);
            InputStream stream = method.getResponseBodyAsStream();

            StringBuilder sb = new StringBuilder();
            int rowNum;
            byte[] buffer = new byte[1024 * 4];
            while ((rowNum = stream.read(buffer)) != -1) {

                sb.append(new String(buffer, 0, rowNum, "utf-8"));
            }
            result = sb.toString();
            method.releaseConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public String post() throws Exception{
        URL url = new URL(this.url);

        //开始访问
        StringBuilder postData = new StringBuilder();
        for (String key : this.data.keySet()) {
            if (postData.length() != 0) { postData.append('&'); }
            postData.append(URLEncoder.encode(key, "UTF-8")).append('=')
                    .append(URLEncoder.encode(this.data.get(key), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
//        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        OutputStreamWriter paramout = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");

        InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");

        Reader in = new BufferedReader(isr);

        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0;){
            sb.append((char)c);
        }
        String response = sb.toString();
        System.out.println(response);
        return response;
    }

    public String post1() throws Exception{
        URL url = new URL(this.url);

        JSONObject json = new JSONObject();
        for (String key : this.data.keySet()) {
            json.put(key, this.data.get(key));
        }

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        OutputStreamWriter paramout = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
        paramout.write(json.toString());
        paramout.flush();

        InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");

        BufferedReader br = new BufferedReader(isr);

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null){
            sb.append(line);
        }
        String response = sb.toString();
        System.out.println(response);
        return response;
    }

    public String doPost(String contentType, String encoding){

        CloseableHttpClient client = null;
        HttpEntity resEntity = null;
        CloseableHttpResponse response = null;
        //返回结果
        String result = null;
        try {
            client = HttpClients.createDefault();
            //设置请求参数
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", contentType);
            if (null != this.data){
                List<NameValuePair> httpParams = new ArrayList<>();
                for (String key : this.data.keySet()) {
                    httpParams.add(new BasicNameValuePair(key, this.data.get(key)));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(httpParams, encoding);
                entity.setContentType(contentType);
                httpPost.setEntity(entity);
            }
            //请求并获得响应
            response =  client.execute(httpPost);
            resEntity = response.getEntity();
            if(resEntity!=null){
                if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    result = EntityUtils.toString(response.getEntity(), encoding);// 返回json格式：
                    System.out.println(result);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭一堆连接
            try { EntityUtils.consume(resEntity); } catch (IOException e) { e.printStackTrace(); }
            try { response.close(); } catch (IOException e) { e.printStackTrace(); }
            try { client.close(); } catch (IOException e) { e.printStackTrace(); }
        }
        return result;
    }

    public String jsonPost(){
        return doPost("application/json", "utf-8");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ConcurrentHashMap<String, String> getData() {
        return data;
    }

    public void setData(ConcurrentHashMap<String, String> data) {
        this.data = data;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
        if (this.url.startsWith("/") && host.endsWith("/")){
            this.url = host + this.url.substring(1);
        } else {
            this.url = host + this.url;
        }
    }
}
