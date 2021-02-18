package com.liuyao.demo.utils;

import javafx.util.Callback;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class IOUtil {

    /**
     * 文件上传 根据实际修改
     * @param file
     * @return
     */
    public static String upload(MultipartFile file, String path){
        HttpServletRequest request = ServletUtil.getRequest();
        String upload = request.getSession().getServletContext().getRealPath("upload");
        String resourse = ServletUtil.getRequest().getServletContext().getRealPath("resources");
        String base = System.getProperty("catalina.home"); //获取tomcat根路径
        System.out.println("upload：" + upload);
        System.out.println("resourse：" + resourse);
        System.out.println("base：" + base);

        String url = "";
        if (!file.isEmpty()) {
            //随机生成一个唯一的名字
            String mz = file.getOriginalFilename();
            System.out.println(mz);//3.jpg
            String[] mmm = mz.split("\\.");
            String fileName = UUID.randomUUID().toString()+"."+mmm[1];

            File targetFile = new File(path, fileName);
            if(!targetFile.exists()){
                targetFile.mkdirs();
            }
            try {
                file.transferTo(targetFile);//保存文件
            } catch (Exception e) {
                e.printStackTrace();
            }

            url = "/upload/"+fileName;
        }
        return url;
    }

    public static boolean saveFile(MultipartFile file, String folderPath, String fileName){
        try {
            if (!file.isEmpty()) {
                newFolder(folderPath);
                file.transferTo(new File(folderPath, fileName));
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean newFile(String folderPath, String fileName, String[] fileContents, String encoding, boolean cover){
        newFolder(folderPath);
        File file = new File(folderPath + "/" + fileName);
        if (cover || !file.exists()){
            encoding = null == encoding ? "utf-8" : encoding;
            FileOutputStream fos = null;
            OutputStreamWriter osw = null;
            BufferedWriter bw = null;
            PrintWriter pw = null;
            try {
                fos = new FileOutputStream(file);
                osw = new OutputStreamWriter(fos, encoding);
                bw = new BufferedWriter(osw);
                if (null != fileContents & fileContents.length > 0){
                    bw.write(fileContents[0] == null ? "" : fileContents[0]);
                    for (int i = 1; i < fileContents.length; i++) {
                        bw.newLine();
                        bw.write(fileContents[i] == null ? "" : fileContents[i]);
                    }
                }
                pw = new PrintWriter(bw);
                pw.flush();
//                fos.write(fileContent.getBytes());
//                file.createNewFile();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close(fos);
                close(osw);
                close(bw);
                close(pw);
            }
        }
        return false;
    }

    public static void newFolder(String folder){
        File targetFile = new File(folder);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
    }

    public static String readFile(MultipartFile file,  String encoding,
                                  Callback<BufferedReader, StringBuilder> callback){
        InputStream is = null;
        try {
            byte[] filebyte = file.getBytes();
            is = new ByteArrayInputStream(filebyte);
            return readFile(is, encoding, callback);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(is);
        }
        return null;
    }

    public static String readFile(String path, String encoding,
                                  Callback<BufferedReader, StringBuilder> callback){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            return readFile(fis, encoding, callback);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            close(fis);
        }
        return null;
    }
    public static String readFile(InputStream is, String encoding,
                                  Callback<BufferedReader, StringBuilder> callback){
        BufferedReader reader = null;
        InputStreamReader isr = null;
        encoding = null == encoding ? "UTF-8" : encoding;
        StringBuilder res = new StringBuilder();
        try{
            isr = new InputStreamReader(is, encoding);
            reader = new BufferedReader(isr);
            if (null == callback){
                String line;
                while((line = reader.readLine()) != null){
                    res.append(line).append("\n");
                }
            } else {
                res = callback.call(reader);

            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            close(isr);
            close(reader);
        }
        return res.toString();
    }

    public static String fileMd5(MultipartFile file){
        try {
            return DigestUtils.md5Hex(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void close(AutoCloseable c){
        try {
            if (null != c) c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>将文件转成base64 字符串</p>
     * @param path 文件路径
     * @return
     * @throws Exception
     */
    public static String fileToBase64(File file) {
        FileInputStream inputFile = null;
        byte[] buffer = new byte[0];
        try {
            buffer = new byte[(int)file.length()];
            inputFile = new FileInputStream(file);
            inputFile.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(inputFile);
        }
        Base64.Encoder encoder = Base64.getEncoder(); // JDK 1.8  推荐方法
        String str = encoder.encodeToString(buffer);
//        return new BASE64Encoder().encode(buffer);
        return str;
    }
    /**
     * <p>将base64字符解码保存文件</p>
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */
    public static void decoderBase64File(String base64Code,String targetPath) throws Exception {
        byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }
    /**
     * <p>将base64字符保存文本文件</p>
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */
    public static void base64ToFile(String base64Code,String targetPath) throws Exception {
        byte[] buffer = base64Code.getBytes();
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }

    public static byte[] getFileByte(File file) throws IOException {
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            byte[] b = new byte[(int) file.length()];
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
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 序列化实例到磁盘文件
     * @param path 文件路径
     * @param obj 实例
     */
    public static void serializableToFile(String path, Object obj){
        FileOutputStream fs = null;
        ObjectOutputStream oos = null;
        try {
            fs = new FileOutputStream(path);
            oos = new ObjectOutputStream(fs);
            oos.writeObject(obj);
            oos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(fs);
            close(oos);
        }
    }

    /**
     * 反序列化文件 获得实例
     * @param path 文件路径
     * @param claz 实例的class
     * @param <T>
     * @return
     */
    public static <T> T deSerializableFromFile(String path, Class<T> claz){
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(path);
            ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            if (obj.getClass() == claz){
                return (T) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close(fis);
            close(ois);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(IOUtil.newFile(
                "C:/JAVA/project/test", "test.dtfb",
                new String[]{null, " aaa"}, "utf-8",true));

        serializableToFile("", new Object[7]);
        deSerializableFromFile("", Object[].class);
    }

}
