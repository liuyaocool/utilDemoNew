package com.liuyao.demo.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCreate {

    private FileOutputStream fos;
    private ZipOutputStream out;

    public ZipCreate(String filepath) {
        try {
            this.fos = new FileOutputStream(filepath);
            this.out = new ZipOutputStream(this.fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
        IOUtil.close(this.out, this.fos);
    }

    public static void main(String[] temp) { // 主方法
        ZipCreate zip = new ZipCreate("c://aa.zip");
        zip.addFile("b.txt", "aaa".getBytes());
        zip.addFile("fol/a.txt", "bbb".getBytes());
        zip.saveFile();
    }

}
