package com.liuyao.demo.utils;



import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class BaiduApi {



    public static class Video{

        //grant_type为固定参数
        private static String
                APP_ID = "18619918",
                API_KEY = "w9SykzDLBLz1aWuSlHrLcBM5",
                SECRET_KEY = "xGNa5HopFMMFyRsMsnVvcEeEafVe6F5s",
                AUTH_HOST = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials"
                        + "&client_id=" + API_KEY + "&client_secret=" + SECRET_KEY;


        public static String getAccessToken(){

            return "";
        }

        /**
         * 获取权限token
         * @return 返回示例：
         * {
         * "access_token": "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567",
         * "expires_in": 2592000
         * }
         * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
         * @param ak - 百度云官网获取的 API Key
         * @param sk - 百度云官网获取的 Securet Key
         * @return assess_token 示例：
         * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
         */
        public static String getAuth() {

            try {
                URL realUrl = new URL(AUTH_HOST);
                // 打开和URL之间的连接
                HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                // 获取所有响应头字段
                Map<String, List<String>> map = connection.getHeaderFields();
                // 遍历所有的响应头字段
                for (String key : map.keySet()) {
                    System.err.println(key + "--->" + map.get(key));
                }
                // 定义 BufferedReader输入流来读取URL的响应
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String result = "";
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
                /**
                 * 返回结果示例
                 */
                System.err.println("result:" + result);
                JSONObject jsonObject = new JSONObject(result);
                String access_token = jsonObject.getString("access_token");
                return access_token;
            } catch (Exception e) {
                System.err.printf("获取token失败！");
                e.printStackTrace(System.err);
            }
            return null;
        }
    }



}
