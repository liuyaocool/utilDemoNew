package com.liuyao.demo.ttt.jna;

import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary.StdCallCallback;

/**
 * @author Administrator
 *  定义回调函数接口并继承StdCallCallback
 *	必须继承自com.sun.jna.Callback接口 （如果回调函数是以stdcall输出，有时候可能引起jvm崩溃，
 *	可以改成继承StdCallCallback接口试试，）
子接口必须定义单个公有方法或一个名为callback的公有方法。必须持有到回调对象的一个存活引用。一个回调应该不抛出异常。
 */
public interface CallBack extends StdCallCallback {
    void  JMp4Server_RegCallBack(int UserID, int MP4BackCode,Pointer pData, int dwDataSize);
}
