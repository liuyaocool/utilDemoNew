package com.liuyao.demo.ttt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;
import org.xvolks.jnative.pointers.Pointer;
import org.xvolks.jnative.pointers.memory.HeapMemoryBlock;
import org.xvolks.jnative.pointers.memory.MemoryBlock;
import org.xvolks.jnative.pointers.memory.MemoryBlockFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Java通过jnative调用pi实时数据库dll类库piapi32.dll获取tag标签数据
 */

public class PIClientUtil {

    private static PIClientUtil piClientUtil = new PIClientUtil();

    public static void main(String[] args) {

        //PIClientUtil.getPIClientUtil().getTimeFromInt("");

        //PIClientUtil.getPIClientUtil().getTagValue("picompress_Compression Ratio_CALC");

        //PIClientUtil.getPIClientUtil().getTagValueByTime("CDT158","2012-05-17 11:11:11");

        //PIClientUtil.getPIClientUtil().getTagValuesByTimeToTime("CDT158","2012-05-17 11:11:11","2012-05-17 18:00:00",2);

        PIClientUtil.getPIClientUtil().getTagMaxValue("CDT158", "2012-05-17 11:11:11", "2012-05-17 18:00:00");

        //PIClientUtil.getPIClientUtil().getTimeSecint(1);

        //PIClientUtil.getPIClientUtil().getPiTime("");

        //PIClientUtil.getPIClientUtil().getTimeIntSec("2012-03-03 12:00:00");

    }

    public static PIClientUtil getPIClientUtil() { return piClientUtil; }

    private PIClientUtil() {
        try {
            System.setProperty("jnative.debug", "true"); //没有这一行会 报类库没有加载错误
            // System.setProperty("jnative.loadNative", "C:/Windows/System32/JNativeCpp.dll");
            // *********************连接PI数据库**************************//
            JNative messageBox = new JNative("piapi32.dll", "piut_setservernode");
            messageBox.setRetVal(Type.INT);
            messageBox.setParameter(0, Type.STRING, "10.127.4.158");//服务器ip
            messageBox.invoke();
            System.out.println("piut_setservernode:" + messageBox.getRetValAsInt());
        } catch (NativeException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取tag最新值
     * @param tagName
     * @return
     */
    public float getTagValue(String tagName) {
        try {
            JNative messageBox = new JNative("piapi32.dll", "pipt_findpoint");
            messageBox.setRetVal(Type.INT);
            messageBox.setParameter(0, Type.STRING, tagName);
            Pointer p = new Pointer(new HeapMemoryBlock(1024));
            messageBox.setParameter(1, p);
            messageBox.invoke();
            int ptId = p.getAsInt(0);
            if (0 == messageBox.getRetValAsInt()) {
                System.out.println("测点id:" + ptId);
                messageBox = new JNative("piapi32.dll", "pisn_getsnapshot");
                messageBox.setRetVal(Type.INT);
                messageBox.setParameter(0, Type.INT, "" + ptId);
                Pointer pp = new Pointer(new HeapMemoryBlock(1024));
                messageBox.setParameter(1, pp);
                messageBox.setParameter(2, new Pointer(new HeapMemoryBlock(1024)));
                //messageBox.setParameter(3, new Pointer(new HeapMemoryBlock(1024)));
                //messageBox.setParameter(4, new Pointer(new HeapMemoryBlock(1024)));
                messageBox.invoke();
                if (0 == messageBox.getRetValAsInt()) {
                    System.out.println(tagName + "测点值:" + pp.getAsFloat(0));
                    return pp.getAsFloat(0);
                }
            } else {
                System.out.println("查询测点失败");
            }
        } catch (NativeException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0F;
    }


    /**
     *      * 获取测点制定时间点的值
     *      * @param tagName
     *      * @return
     *     
     */

    public float getTagValueByTime(String tagName, String time) {


        try {


            JNative messageBox = new JNative("piapi32.dll", "pipt_findpoint");

            messageBox.setRetVal(Type.INT);

            messageBox.setParameter(0, Type.STRING, tagName);

            Pointer p = new Pointer(new HeapMemoryBlock(8));

            messageBox.setParameter(1, p);

            messageBox.invoke();

            int ptId = p.getAsInt(0);

            if (0 == messageBox.getRetValAsInt()) {

                System.out.println("测点id:" + ptId);

                messageBox = new JNative("piapi32.dll", "piar_value");

                messageBox.setRetVal(Type.INT);

                messageBox.setParameter(0, Type.INT, "" + ptId);

                Pointer pp = new Pointer(new HeapMemoryBlock(1024));

                Pointer pp_status = new Pointer(new HeapMemoryBlock(1024));

                messageBox.setParameter(1, getTimeIntSec(time));

                messageBox.setParameter(2, Type.INT, 3 + "");

                messageBox.setParameter(3, pp);

                messageBox.setParameter(4, pp_status);

                messageBox.invoke();

                if (0 == messageBox.getRetValAsInt()) {

                    System.out.println(tagName + "测点值:" + pp.getAsFloat(0));

                    System.out.println(tagName + "status值:" + pp_status.getAsInt(0));

                    return pp.getAsFloat(0);

                } else {

                    System.out.println(tagName + "查询返回值:" + messageBox.getRetValAsInt());

                }


            } else {

                System.out.println("查询测点失败");

            }


        } catch (NativeException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        } catch (IllegalAccessException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }

        return 0F;

    }


    /**
     *      * 查询测点某时间段内最小值
     * <p>
     *      * @param tagName
     * <p>
     *      * @param time1
     * <p>
     *      * @param time2
     * <p>
     *     
     */

    public void getTagMinValue(String tagName, String time1, String time2) {

        int code = 1;

        this.getTagValuesByTimeToTime(tagName, time1, time2, code);

    }


    /**
     *      * 查询测点某时间段内最大值
     * <p>
     *      * @param tagName
     * <p>
     *      * @param time1
     * <p>
     *      * @param time2
     * <p>
     *     
     */

    public void getTagMaxValue(String tagName, String time1, String time2) {

        int code = 2;

        this.getTagValuesByTimeToTime(tagName, time1, time2, code);

    }

    /**
     *      * 查询测点某时间段内平均值
     * <p>
     *      * @param tagName
     * <p>
     *      * @param time1
     * <p>
     *      * @param time2
     * <p>
     *     
     */

    public void getTagAvgValue(String tagName, String time1, String time2) {

        int code = 5;

        this.getTagValuesByTimeToTime(tagName, time1, time2, code);

    }


    /**
     *      *
     * <p>
     *      * @param tagName
     * <p>
     *      * @return
     * <p>
     *     
     */

    public void getTagValuesByTimeToTime(String tagName, String time1, String time2, int code) {


        try {


            JNative messageBox = new JNative("piapi32.dll", "pipt_findpoint");

            messageBox.setRetVal(Type.INT);

            messageBox.setParameter(0, Type.STRING, tagName);

            Pointer p = new Pointer(new HeapMemoryBlock(1024));

            messageBox.setParameter(1, p);

            messageBox.invoke();

            int ptId = p.getAsInt(0);

            if (0 == messageBox.getRetValAsInt()) {

                System.out.println("测点id:" + ptId);

                messageBox = new JNative("piapi32.dll", "piar_summary");

                messageBox.setRetVal(Type.INT);

                //下面开始设置参数

                messageBox.setParameter(0, Type.INT, "" + ptId);


                Pointer p_startTime = this.getTimeIntSec(time1);

                Pointer p_endTime = this.getTimeIntSec(time2);


                Pointer p_retVal = new Pointer(new HeapMemoryBlock(8));


                Pointer p_pctGood = new Pointer(new HeapMemoryBlock(8));


                messageBox.setParameter(1, p_startTime);

                messageBox.setParameter(2, p_endTime);

                messageBox.setParameter(3, p_retVal);

                messageBox.setParameter(4, p_pctGood);

                messageBox.setParameter(5, Type.INT, code + "");

                messageBox.invoke();

                if (0 == messageBox.getRetValAsInt()) {

                    System.out.println(tagName + "测点返回值:" + p_retVal.getAsFloat(0));

                } else {

                    System.out.println(tagName + "查询状态值:" + messageBox.getRetValAsInt());

                }


            } else {

                System.out.println("查询测点失败");

            }


        } catch (NativeException e) {

            e.printStackTrace();

        } catch (IllegalAccessException e) {

            e.printStackTrace();

        }

    }


    /**
     *      * 将时间串转换为int
     * <p>
     *      * 格式:11-Aug-17  18:00:00
     * <p>
     *      * @param time
     * <p>
     *      * @return
     * <p>
     *     
     */

    public int getPiTime(String time) {

        // 获取时间点

        try {

            String tt = time;

            Pointer pointer = new Pointer(MemoryBlockFactory.createMemoryBlock(8));


            JNative messageBox = new JNative("piapi32.dll", "pitm_parsetime");

            messageBox.setRetVal(Type.INT);

            messageBox.setParameter(0, Type.STRING, tt);

            messageBox.setParameter(1, Type.INT, "0");

            messageBox.setParameter(2, pointer);

            messageBox.invoke();

            if (0 == messageBox.getRetValAsInt()) {

                System.out.println("执行成功,getPiTime结果是:" + pointer.getAsInt(0));

                return pointer.getAsInt(0);

            } else {

                System.out.println("执行失败");

                return 0;

            }

        } catch (NativeException e) {

            e.printStackTrace();

            return 0;

        } catch (IllegalAccessException e) {

            e.printStackTrace();

            return 0;

        }

    }


    /**
     * 将整数转换为时间,同上方法互逆
     * <p>
     *      * @param time
     * <p>
     *      * @return
     * <p>
     *     
     */

    public String getTimeFromInt(int time) {


        try {

            Pointer pointer = new Pointer(MemoryBlockFactory.createMemoryBlock(8));


            JNative messageBox = new JNative("piapi32.dll", "pitm_formtime");

            messageBox.setRetVal(Type.INT);

            messageBox.setParameter(0, Type.INT, this.getPiTime("") + "");

            messageBox.setParameter(1, pointer);

            messageBox.setParameter(2, 19);

            messageBox.invoke();


            System.out.println("结果是:" + pointer.getAsString());

            return pointer.getAsString();

        } catch (NativeException e) {

            e.printStackTrace();

        } catch (IllegalAccessException e) {

            e.printStackTrace();

        }

        return "";

    }


    /**
     *      * 将PI日期转为数组型
     * <p>
     *      * @param time
     * <p>
     *      * @return
     * <p>
     *     
     */

    public int[] getTimeSecint(int time) {

        int[] time_arrays = new int[6];

        try {

            Pointer pointer = new Pointer(MemoryBlockFactory.createMemoryBlock(4 * time_arrays.length));

            JNative messageBox = new JNative("piapi32.dll", "pitm_secint");


            messageBox.setParameter(0, Type.INT, this.getTimeIntSec("2012-05-17 11:11:11").getAsInt(0) + "");

            messageBox.setParameter(1, pointer);

            messageBox.invoke();


            for (int i = 0; i < time_arrays.length; i++) {

                time_arrays[i] = pointer.getAsInt(i * 4);

            }


            return time_arrays;

        } catch (NativeException e) {

            e.printStackTrace();

        } catch (IllegalAccessException e) {

            e.printStackTrace();

        }

        return null;

    }


    /**
     *      * 将日期转为数组型，与pitm_secint互相逆
     * <p>
     *      * @param time
     * <p>
     *      * @return
     * <p>
     *     
     */

    public Pointer getTimeIntSec(String time) {


        PIDate date = PIDate.getPIDate(time);

        try {


            int time_array[] = new int[]{date.getMonth(), date.getDay(), date.getYear(), date.getHour(), date.getMinutes(), date.getSeconds()};


            Pointer pointer = new Pointer(MemoryBlockFactory.createMemoryBlock(8));

            Pointer pointer_array = new Pointer(MemoryBlockFactory.createMemoryBlock(time_array.length * 4));

            JNative messageBox = new JNative("piapi32.dll", "pitm_intsec");

            /**这种方法也可以

             pointer_array.setIntAt(0, 2012);

             pointer_array.setIntAt(4, 5);

             pointer_array.setIntAt(8, 16);

             pointer_array.setIntAt(12, 21);

             pointer_array.setIntAt(16, 33);

             pointer_array.setIntAt(20, 33);

                    **/


            // 初始化数组

            for (int i = 0; i < time_array.length; i++) {

                pointer_array.setIntAt(4 * i, time_array[i]);

            }


            messageBox.setParameter(0, pointer);

            messageBox.setParameter(1, pointer_array);

            messageBox.invoke();

            System.out.println("getTimeIntSec=" + pointer.getAsInt(0));

            return pointer;

        } catch (NativeException e) {

            e.printStackTrace();

        } catch (IllegalAccessException e) {

            e.printStackTrace();

        }

        return null;

    }


    /**
     *      * 不能直接返回int[0]
     * <p>
     *      * @param time
     * <p>
     *      * @return
     * <p>
     *     
     */

    public int getTimeIntSecForInt(String time) {

        PIDate date = PIDate.getPIDate(time);

        try {


            int time_array[] = new int[]{date.getMonth(), date.getDay(), date.getYear(), date.getHour(), date.getMinutes(), date.getSeconds()};

            Pointer pointer = new Pointer(MemoryBlockFactory.createMemoryBlock(8));

            Pointer pointer_array = new Pointer(MemoryBlockFactory.createMemoryBlock(time_array.length * 4));

            JNative messageBox = new JNative("piapi32.dll", "pitm_intsec");


            // 初始化数组

            for (int i = 0; i < time_array.length; i++) {

                pointer_array.setIntAt(4 * i, time_array[i]);

            }


            messageBox.setParameter(0, pointer);

            messageBox.setParameter(1, pointer_array);

            messageBox.invoke();

            System.out.println("getTimeIntSec=" + pointer.getAsInt(0));

            return pointer.getAsInt(0);

        } catch (NativeException e) {

            e.printStackTrace();

        } catch (IllegalAccessException e) {

            e.printStackTrace();

        }

        return 0;

    }


}


//        时间工具类：


class PIDate {

    private int year;

    private int month;

    private int day;

    private int hour;

    private int minutes;

    private int seconds;


    public int getYear() {

        return year;

    }

    public void setYear(int year) {

        this.year = year;

    }

    public int getMonth() {

        return month;

    }

    public void setMonth(int month) {

        this.month = month;

    }

    public int getDay() {

        return day;

    }

    public void setDay(int day) {

        this.day = day;

    }

    public int getHour() {

        return hour;

    }

    public void setHour(int hour) {

        this.hour = hour;

    }

    public int getMinutes() {

        return minutes;

    }

    public void setMinutes(int minutes) {

        this.minutes = minutes;

    }

    public int getSeconds() {

        return seconds;

    }

    public void setSeconds(int seconds) {

        this.seconds = seconds;

    }


    public static PIDate getPIDate(String time) {

        Pattern p = Pattern.compile("(\\d{4})-(\\d{1,2})-(\\d{1,2}) (\\d{1,2}):(\\d{1,2}):(\\d{1,2})");

        Matcher m = p.matcher(time);

        PIDate date = new PIDate();

        if (m.find()) {

    /*System.out.println("日期:"+m.group()); 

    System.out.println("年:"+m.group(1)); 

    System.out.println("月:"+m.group(2)); 

    System.out.println("日:"+m.group(3));

    System.out.println("时:"+m.group(4));

    System.out.println("分:"+m.group(5));

    System.out.println("秒:"+m.group(6));*/

            date.setYear(Integer.parseInt(m.group(1)));

            date.setMonth(Integer.parseInt(m.group(2)));

            date.setDay(Integer.parseInt(m.group(3)));

            date.setHour(Integer.parseInt(m.group(4)));

            date.setMinutes(Integer.parseInt(m.group(5)));

            date.setSeconds(Integer.parseInt(m.group(6)));

        }

        return date;


    }

}