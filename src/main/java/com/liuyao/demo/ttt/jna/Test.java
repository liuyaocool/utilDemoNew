package com.liuyao.demo.ttt.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.ptr.IntByReference;

public class Test {

    /*需要定义一个接口，继承自Library 或StdCallLibrary。
      默认的是继承Library ，如果动态链接库里的函数是以stdcall方式输出的，那么就继承StdCallLibrary，比如kernel32库。
    */
    public interface CLibrary extends Library {
        //加载msvcrt.dll库,此处不需要后缀.dll或.so
        CLibrary INSTANCE = (CLibrary)
                Native.loadLibrary((Platform.isWindows() ? "piapi32" : "c"),
                        CLibrary.class);
        //对msvcrt.dll中需要使用的printf函数进行声明
        void printf(String format, Object... args);
    }

    public static void main(String[] args) {
//        //使用printf函数
//        CLibrary.INSTANCE.printf("Hello, World\n");
//        for (int i=0;i < args.length;i++) {
//            CLibrary.INSTANCE.printf("Argument %d: %s\n", i, args[i]);
//        }


        System.out.println(Test.class.getResource("/"));
        IntByReference pt = new IntByReference();
        CallBack callBack = new CallBackImpl();//回调函数实例
        Clibrary c = Clibrary.INSTANCE;//Dll实例
        System.out.println(c.pitm_systime());
        c.pipt_findpoint("aaa", pt);
        pt.getValue();
//        c.JMp4Server_RegCallBack(callBack);//注册回调函数
//        c.JMp4ServerPlay(1,"D:\\KuGou\\audio\\76990144\\1.MP4");

        System.out.println(c.pitm_systime());
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
