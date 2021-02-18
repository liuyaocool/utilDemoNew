package com.liuyao.demo.ws.client;

import com.liuyao.demo.utils.ServletUtil;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Ajax{

    private String host;
    private String url;
    private String sessionId;
    private String type = "POST";
    private Map<String, String> data;

    public String doPost(String contentType, String encoding){

        OutputStreamWriter paramout = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        String result = null;

        try{
            URL url = new URL(getAddress());

            JSONObject json = new JSONObject();
            for (String key : this.data.keySet()) {
                json.put(key, this.data.get(key));
            }
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod(this.type);
            conn.setRequestProperty("Content-Type", contentType);
            conn.setRequestProperty("Cookie", "JSESSIONID="+this.sessionId+".node0");
            conn.setDoOutput(true);

            paramout = new OutputStreamWriter(conn.getOutputStream(),encoding);
            paramout.write(json.toString());
            paramout.flush();

            isr = new InputStreamReader(conn.getInputStream(), encoding);
            br = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null){
                sb.append(line);
            }
            result = sb.toString();
            System.out.println(result);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(paramout);
            close(isr);
            close(br);
        }
        return result;
    }

    public String jsonPost(){
        return doPost("application/json", "utf-8");
    }

    public Ajax() {
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
        return type;
    }

    public void setType(String type) {
        this.type = type.toLowerCase();
    }

    private void close(AutoCloseable obj){
        try {
            obj.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
