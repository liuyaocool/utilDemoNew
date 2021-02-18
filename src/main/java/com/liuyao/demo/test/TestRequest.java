package com.liuyao.demo.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuyao
 * @date 2019/9/27 5:22 PM
 * @Description
 **/
public class TestRequest {
    public static void main(String[] args) throws Exception {

        String addr = "http://127.0.0.1:8980/dataAccess/file";
        Map<String, File> a = new HashMap<>();
        a.put("a", new File("C:/unintall.log"));
        a.put("b", new File("C:/JAVA/apache-maven-3.5.3/README.txt"));
        upLoadFilePost(addr, a);

    }

    public static String upLoadFilePost(String actionUrl, Map<String, File> files) throws IOException {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";
        URL uri = new URL(actionUrl);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(5 * 1000);
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
        // 发送文件数据
        if (files != null)
            for (Map.Entry<String, File> file : files.entrySet()) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                sb1.append("Content-Disposition: form-data; name=\"files\"; filename=\""
                        + file.getKey() + "\"" + LINEND);
                sb1.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINEND);
                sb1.append(LINEND);
                outStream.write(sb1.toString().getBytes());
                InputStream is = new FileInputStream(file.getValue());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }

                is.close();
                outStream.write(LINEND.getBytes());
            }

        // 请求结束标志
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        outStream.write(end_data);
        outStream.flush();

        // 得到响应码
        int res = conn.getResponseCode();
        System.out.println(res);
        if (res == 200) {
            InputStream in = conn.getInputStream();
            InputStreamReader isReader = new InputStreamReader(in);
            BufferedReader bufReader = new BufferedReader(isReader);
            String line = "";
            String data = "";
            while ((line = bufReader.readLine()) != null) {
                data += line;
            }
            outStream.close();
            conn.disconnect();
            return data;
        }
        outStream.close();
        conn.disconnect();
        return null;
    }

    public static int upload(String strURL, File[] allFile) {

        int status = 200;

        for (int i = 0; i < allFile.length; ++i) {

            String localFile = allFile[i].getAbsolutePath();

            if (!(allFile[i].exists())) {

                continue;

            }

            long startPos = 0L;

//            HttpClient headclient = new DefaultHttpClient();

            HttpHead httphead = new HttpHead(strURL);

            try {

                httphead.addHeader("Content-Type", "application/octet-stream");

            } catch (Exception e) {

                e.printStackTrace();

            } finally {

//                headclient.getConnectionManager().shutdown();

            }

            HttpURLConnection conn = null;

            try {

                conn = (HttpURLConnection) new URL(strURL).openConnection();

                RandomAccessFile fis = new RandomAccessFile(new File(localFile), "r");

                if (startPos < fis.length()) {

                    conn.setRequestMethod("PUT");

                    conn.setDoOutput(true);

                    conn.setDoInput(true);

                    conn.setRequestProperty("Content-Type", "application/octet-stream");

                    conn.setRequestProperty("File-Name", allFile[i].getName());

                    OutputStream os = conn.getOutputStream();

                    int rn = 0;

                    byte[] buf = new byte[4096];

                    while ((rn = fis.read(buf, 0, 4096)) > 0) {

                        os.write(buf, 0, rn);

                    }

                    os.close();

                    status = conn.getResponseCode();

                }

                fis.close();

            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                try {

                    conn.getResponseCode();

                } catch (IOException e1) {

                    e1.printStackTrace();

                }

                e.printStackTrace();

            }

        }

        return status;

    }

//    public static String uploadFile(String url, File file) throws ClientProtocolException, IOException {
//        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//        CloseableHttpResponse httpResponse = null;
//        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(200000).setSocketTimeout(200000000).build();
//        HttpPost httpPost = new HttpPost(url);
//        httpPost.setConfig(requestConfig);
//        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
//        //multipartEntityBuilder.setCharset(Charset.forName("UTF-8"));
//
//        //File file = new File("F:\\Ken\\1.PNG");
//        //FileBody bin = new FileBody(file);
//
//
//        //multipartEntityBuilder.addBinaryBody("file", file,ContentType.create("image/png"),"abc.pdf");
//        //当设置了setSocketTimeout参数后，以下代码上传PDF不能成功，将setSocketTimeout参数去掉后此可以上传成功。上传图片则没有个限制
//        //multipartEntityBuilder.addBinaryBody("file",file,ContentType.create("application/octet-stream"),"abd.pdf");
//        multipartEntityBuilder.addBinaryBody("file", file);
//        //multipartEntityBuilder.addPart("comment", new StringBody("This is comment", ContentType.TEXT_PLAIN));
//        multipartEntityBuilder.addTextBody("comment", "this is comment");
//        HttpEntity httpEntity = multipartEntityBuilder.build();
//        httpPost.setEntity(httpEntity);
//
//        httpResponse = httpClient.execute(httpPost);
//        HttpEntity responseEntity = httpResponse.getEntity();
//        int statusCode = httpResponse.getStatusLine().getStatusCode();
//        if (statusCode == 200) {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
//            StringBuffer buffer = new StringBuffer();
//            String str = "";
//            while (!StringUtils.isEmpty(str = reader.readLine())) {
//                buffer.append(str);
//            }
//            System.out.println(buffer.toString());
//            return buffer.toString();
//        }
//
//        httpClient.close();
//        if (httpResponse != null) {
//            httpResponse.close();
//        }
//        return null;
//    }

    private static void doRequest(String urlstr, JSON data) throws Exception{
        System.out.println("=============请求======================");
        System.out.println(urlstr);
        System.out.println(data.toJSONString());

        String encoding = "utf-8";
        URL url = new URL(urlstr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        OutputStreamWriter paramout = new OutputStreamWriter(conn.getOutputStream(), encoding);
        paramout.write(data.toJSONString());
        paramout.flush();

        InputStreamReader isr = new InputStreamReader(conn.getInputStream(), encoding);
        BufferedReader br = new BufferedReader(isr);

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null){
            sb.append(line);
        }
        JSONObject datas = JSONObject.parseObject(sb.toString());
        JSONArray res = datas.getJSONArray("data");
        System.out.println("=============响应======================");
        System.out.println(datas);
        for (int i = 0; i < res.size(); i++) {
            System.out.println(res.get(i));
        }
        System.out.println("=============结束======================");

        paramout.close();
        isr.close();
        br.close();
    }

    private static void fileUpload(){
        boolean stop = false;
        int port = 8080;
        String fileDir = "";
        Socket socket = null;
        try {
            ServerSocket ss = new ServerSocket(port);
            do {
                socket = ss.accept();
                // public Socket accept() throws
                // IOException侦听并接受到此套接字的连接。此方法在进行连接之前一直阻塞。
                System.out.println("建立socket链接");
                DataInputStream inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                //本地保存路径，文件名会自动从服务器端继承而来
                int bufferSize = 8192;
                byte[] buf = new byte[bufferSize];
                long passedlen = 0;
                long len = 0;

                //获取文件名
                String file = fileDir + inputStream.readUTF();
                DataOutputStream fileOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
                len = inputStream.readLong();
                System.out.println("文件的长度为:  " + len  + "\n");
                System.out.println("开始接收文件!" + "\n");

                while(true) {
                    int read = 0;
                    if (inputStream != null) {
                        read = inputStream.read(buf);
                    }
                    passedlen += read;
                    if(read == -1) {
                        break;
                    }
                    //进度条输出
                    //System.out.println("文件完成度:  " + (passedlen/len * 100) + "%");

								/*System.out.println(passedlen/len );

								System.out.println(passedlen/len * 100);*/

                    fileOut.write(buf, 0, read);
                    fileOut.flush();
                    double a  = Double.valueOf(String.valueOf(passedlen))/Double.valueOf(String.valueOf(len))*100;
                    System.out.println("文件完成度:  " +a+ "%");
                }
                fileOut.close();
            } while (!stop);
            System.out.println("*********************服务器关闭*********************");
        } catch (Exception e) {
            System.out.println("接收消息错误" + "\n");
            e.printStackTrace();
            return ;
        }
    }

    private static void uploadFile(String host, int port) throws Exception{
        String filePath = "", fileName = "";
        Socket s = new Socket(host, port);

            //选择进行文件传输的文件
            File fi = new File(filePath + fileName);
            System.out.println("文件名为:  " + fileName + "\n文件路径为: " + filePath );
            DataInputStream fis = new DataInputStream(new FileInputStream(filePath + fileName));
            DataOutputStream ps  = new DataOutputStream(s.getOutputStream());
            ps.writeUTF(fi.getName());
            ps.flush();
            ps.writeLong(fi.length());
            ps.flush();

            int bufferSize = 8192;
            byte[] buf = new byte[bufferSize];

            while(true) {
                int read = 0;
                if (fis != null) {
                    read = fis.read(buf);
                }

                if(read == -1) {
                    break;
                }
                ps.write(buf,0,read);
            }
            ps.flush();
            //注意关闭socket链接哦，不然客户端会等待server的数据过来
            //直到socket超时，导致数据不完整
            fis.close();
            ps.close();
            s.close();
            System.out.println("文件传输完成");
    }
}
