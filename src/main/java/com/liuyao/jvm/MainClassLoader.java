package com.liuyao.jvm;

import com.liuyao.demo.utils.IOUtil;

import java.io.*;

public class MainClassLoader {
    static String classPath = "D:\\PROJECT_MY\\utilDemoNew\\target\\classes\\";

    public static void main(String[] args) {

//        loadClass();
//        myLoader();
//        encryLoader();
        // 打破双亲委派
        breakParentLoader("com.liuyao.spring.Test");
//        printClassLoadersLoadPath();
    }

    private static void breakParentLoader(String className) {
        try {
            BreakParentLoader l = new BreakParentLoader();
            Class<?> aClass = l.loadClass(className);
            l = new BreakParentLoader();
            Class<?> bClass = l.loadClass(className);
            System.out.println(aClass == bClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void encryLoader() {
        EncryLoader.encFile("com.liuyao.spring.Test");

        ClassLoader l = new EncryLoader();
        Class clazz = null;
        try {
            clazz = l.loadClass("com.liuyao.jvm.Test");
            Test h = (Test)clazz.newInstance();
            h.m();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        System.out.println(l.getClass().getClassLoader());
        System.out.println(l.getParent());
    }

    private static void myLoader() {
        Class<?> aClass = null;
        try {
            aClass = new MyLoader().loadClass("com.liuyao.jvm.Test");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(aClass.getName());
        System.out.println(aClass.getClassLoader());
    }


    private static void loadClass() {
        try {
            Class<?> aClass = MainClassLoader.class.getClassLoader().loadClass("com.liuyao.jvm.Test");
            System.out.println(aClass.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private static void printClassLoadersLoadPath() {
        String[] loaders = {"sun.boot.class.path", "java.ext.dirs", "java.class.path"};

        for (String path: loaders) {
            path = System.getProperty(path);
            System.out.println(path.replaceAll(";", System.lineSeparator()));
            System.out.println("----------------");
        }
    }


}

class MyLoader extends ClassLoader {
//    @Override
//    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
//        return super.loadClass(name, resolve);
//    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        File f = new File(MainClassLoader.classPath,
                name.replaceAll(".", "/").concat(".class"));
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        try {
            fis = new FileInputStream(f);
            baos = new ByteArrayOutputStream();

            int b;
            while ((b = fis.read()) != 0) {
                baos.write(b);
            }

            byte[] bytes = baos.toByteArray();
            return defineClass(name, bytes, 0, bytes.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.close(baos, fis);
        }
        return null;
    }
}

class EncryLoader extends ClassLoader {
    public static int seed = 0B10110110;

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        File f = new File(MainClassLoader.classPath, name.replace('.', '/').concat(".class"));

        try {
            FileInputStream fis = new FileInputStream(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b = 0;

            while ((b=fis.read()) !=0) {
                baos.write(b ^ seed);
            }

            byte[] bytes = baos.toByteArray();
            baos.close();
            fis.close();//可以写的更加严谨

            return defineClass(name, bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.findClass(name); //throws ClassNotFoundException
    }

    public static void encFile(String name)  {
        File f = new File(MainClassLoader.classPath, name.replace('.', '/').concat(".class"));
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(f);
            fos = new FileOutputStream(new File(MainClassLoader.classPath, name.replaceAll(".", "/").concat(".class")));

            int b = 0;

            while((b = fis.read()) != -1) {
                fos.write(b ^ seed);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.close(fis, fos);
        }
    }
}

class BreakParentLoader extends ClassLoader {
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        File f = new File(MainClassLoader.classPath
                + name.replace(".", "/").concat(".class"));

        if(!f.exists()) return super.loadClass(name);

        try {

            InputStream is = new FileInputStream(f);

            byte[] b = new byte[is.available()];
            is.read(b);
            return defineClass(name, b, 0, b.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.loadClass(name);
    }
}