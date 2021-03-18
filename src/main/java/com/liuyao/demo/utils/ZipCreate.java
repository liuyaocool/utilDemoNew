package com.liuyao.demo.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCreate {

    private OutputStream os;
    private ZipOutputStream out;
    private String charset;
    private HttpServletResponse response;

    public ZipCreate(String filepath, String charset) {
        try {
            this.charset = charset;
            this.os = new FileOutputStream(filepath);
            this.out = new ZipOutputStream(this.os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ZipCreate(HttpServletResponse response, String charset) throws IOException {
        this(response.getOutputStream(), charset);
        this.response = response;
    }

    public ZipCreate(OutputStream os, String charset) {
        this.charset = charset;
        this.os = os;
        this.out = new ZipOutputStream(this.os);
    }

    public void addFile(String path, byte[] content) {
        try {
            this.out.putNextEntry(new ZipEntry(path));
            for (byte b: content) {
                this.out.write(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFile() {
        try {
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        IOUtil.close(this.out, this.os);
    }

    public void response(String fileName)  {
        try {
            fileName = this.response.encodeURL(new String(fileName.getBytes(), this.charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.response.setContentType("application/octet-stream;charset=" + this.charset);
        this.response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
        saveFile();
    }

    public static void main(String[] temp) { // 主方法
        String a = "http://www.baidu.com/dfas/asdfadf.html";
        String b = "http://www.baidu.com/dfas/asdfadf";
        ZipCreate zip = new ZipCreate("c://aa.zip", "utf-8");
        zip.addFile("b.txt", "aaa".getBytes());
        zip.addFile("fol/a.txt", "bbb".getBytes());
        zip.addFile("fff/aaa/a.txt", "bbb".getBytes());
        zip.saveFile();
    }

}
