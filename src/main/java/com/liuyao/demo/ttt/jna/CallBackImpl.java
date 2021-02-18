package com.liuyao.demo.ttt.jna;



import com.sun.jna.Pointer;

/**
 * @author Administrator
 *	定义回调接口的实现类
 */
public class CallBackImpl implements CallBack {
    public static final int ER_MP4_NORMAL=0; //MP4正常播放
    public static final int ER_MP4_VEDIO_IN=1; //MP4文件的分包视频流
    public static final int ER_MP4_VEDIO=2; //MP4文件的视频流
    public static final int ER_MP4_AUDIO=3; //MP4文件的音频流
    public static final int ER_MP4_OVER=4; //MP4文件播放结束
    public static final int ER_MP4_STOP=5; //停止MP4播放(手动)
    public static final int ER_MP4_NEXT=6; //MP4播放下一首(手动)
    public static final int ER_MP4_NO_EXIST=-1; //MP4文件不存在
    public static final int ER_MP4_FORMAT=-2; //MP4文件格式错误
    @Override
    public void JMp4Server_RegCallBack(int UserID, int MP4BackCode, Pointer pData, int dwDataSize) {
        // TODO Auto-generated method stub
        System.out.println("UserID："+UserID);
        byte[] byteArray = pData.getByteArray(0, dwDataSize);
        System.out.println(byteArray.length+"==="+dwDataSize);
        switch(MP4BackCode) {
            case ER_MP4_NORMAL:System.out.println("MP4正常播放");break;
            case ER_MP4_VEDIO_IN:System.out.println("MP4文件的分包视频流");break;
            case ER_MP4_VEDIO:System.out.println("MP4文件的视频流");break;
            case ER_MP4_AUDIO:System.out.println("MP4文件的音频流");break;
            case ER_MP4_OVER:System.out.println("MP4文件播放结束 ");break;
            case ER_MP4_STOP:System.out.println("停止MP4播放(手动)");break;
            case ER_MP4_NEXT:System.out.println("MP4播放下一首(手动)");break;
            case ER_MP4_NO_EXIST:System.out.println("MP4文件不存在");break;
            case ER_MP4_FORMAT:System.out.println("MP4文件格式错误");break;
            default:System.out.println("未知类型");break;
        }
    }
}
