package com.liuyao.demo.utils;

import sun.misc.Unsafe;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;

class Person {
    private String name = "张三";
    public void print() {
        System.out.println(this.name);
    }
}

public class UnSafeUtil {

    public static void main(String[] args) {
        final Person person = new Person();
        System.out.println(person);
        long memoryAddr = gainMemoryAddr(person);
        System.out.println(Long.toHexString(memoryAddr));
        storeObjByUnsafe("c:/a.class", person,128);
        IOUtil.serializableToFile("c:/Person.class", Person.class);
    }

    private static final Unsafe UNSAFE;
    static final boolean is64bit = true; // auto detect if possible.

    static {
        Unsafe unsafe = null;
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe) theUnsafe.get(null);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        UNSAFE = unsafe;
    }

    public static long gainMemoryAddr(Object obj) {
        Object[] objs = {obj};
        long last = 0;
        int offset = UNSAFE.arrayBaseOffset(objs.getClass());
        int scale = UNSAFE.arrayIndexScale(objs.getClass());
        switch (scale) {
            case 4:
                long factor = is64bit ? 8 : 1;
                return  (UNSAFE.getInt(objs, offset) & 0xFFFFFFFFL) * factor;
//                last = i1;
//                for (int i = 1; i < objs.length; i++) {
//                    final long i2 = (UNSAFE.getInt(obj, offset + i * 4) & 0xFFFFFFFFL) * factor;
//                    if (i2 > last)
//                        System.out.print(", +" + Long.toHexString(i2 - last));
//                    else
//                        System.out.print(", -" + Long.toHexString(last - i2));
//                    last = i2;
//                }
            case 8:
                throw new AssertionError("Not supported");
            default: break;
        }
        return 0;
    }

    public static void storeObjByUnsafe(String path, Object obj, int len) {

        byte[] bytes = new byte[len];
        long objAddr = gainMemoryAddr(obj);
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = UNSAFE.getByte(objAddr++);
        }
        IOUtil.serializableToFile(path, bytes);
    }

    public static void storeClass(String path, Class c) {
        IOUtil.serializableToFile(path, c);
    }



}
