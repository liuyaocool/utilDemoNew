package com.liuyao.demo.utilutil.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author MarkWei
 * @data 2018/8/31 9:32
 * @Description TODO
 */
public class SmsUtil {
    private static final String SMS_COPID = "300033";
    private static final String SMS_LOGINNAME = "ax";
    private static final String SMS_PASSWORD = "890215";
    private static final String SMS_URL = "http://sms3.mobset.com/SDK2/Sms_Send.asp";

    /**
     * 发送短信
     * @param mobiles 手机号
     * @param msg 消息
     */
    public static String sendMessage(String[] mobiles , String msg) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");//日期格式化
        String timeStamp = dateFormat.format(now);
        String strPwsd = MD5.getMD5Str(SMS_COPID + SMS_PASSWORD + timeStamp);//MD5加密方式
        String contexts = null;//转码
        try {
            contexts = URLEncoder.encode(msg, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        StringBuffer sb = new StringBuffer(SMS_URL);
//        sb.append("?CorpID=").append(SMS_COPID).
//                append("&LoginName=").append(SMS_LOGINNAME).
//                append("&TimeStamp=").append(timeStamp).
//                append("&passwd=").append(strPwsd).
//                append("&send_no=");
//        for(int i = 0 ; i < mobiles.length ; i++){
//            if(i > 0){
//                sb.append(";");
//            }
//            sb.append(mobiles[i]);
//        }
//        sb.append("&LongSms=1").append("&msg=").append(contexts);
//
//        System.out.println(sb);
        Map<String,String> params = new HashMap<String,String>();
        params.put("CorpID",SMS_COPID);
        params.put("LoginName",SMS_LOGINNAME);
        params.put("TimeStamp",timeStamp);
        params.put("passwd",strPwsd);
        params.put("send_no", StringUtils.join(mobiles,";"));
        params.put("LongSms","1");
        params.put("msg",msg);
        return httpPost(SMS_URL,params);
    }

    public static void main(String[] args) {
        String rslt = sendMessage(new String[]{"17667840012"},"获得微信红包1.0元，关注微信公众号《班组安全》领取！");
        System.out.println(rslt);
    }

    private static String httpPost(String url,Map<String,String> params){
        String rslt = null;
        try{
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-type", "application/x-www-form-urlencoded;GBK");

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if(params != null){
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            //设置参数到请求对象中
            post.setEntity(new UrlEncodedFormEntity(nvps, "GBK"));

            HttpResponse response = client.execute(post);
            //获取结果实体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                //按指定编码转换结果实体为String类型
                rslt = EntityUtils.toString(entity, "GBK");
            }
            EntityUtils.consume(entity);
        }catch (Exception e){
            e.printStackTrace();
        }
        return rslt;
    }

//    /**
//     * 通过http接口发送短信
//     * @param url
//     * @return
//     */
//    private static String httpGet(String url) {
//        String result = "";
//        try {
//            URL u = new URL(url);
//            URLConnection connection = u.openConnection();
//            connection.connect();
//            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "GBK"));
//            String line;
//            StringBuffer sb = new StringBuffer("");
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
//            in.close();
//        } catch (Exception e) {
//            System.out.println("没有结果！" + e);
//            result = "产生异常";
//        }
//        return result.toString();
//    }

}
