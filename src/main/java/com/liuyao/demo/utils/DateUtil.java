package com.liuyao.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtil {

    /**
     * 转换时间格式
     */
    public static String format(Date date, String format){
        return date == null ? null : new SimpleDateFormat(format).format(date);
    }

    /**
     * 转换为日期格式 string转date
     * 参数 : "2018-05-18 12:12:12", "yyyy-MM-dd HH:mm:ss" hh
     * @return
     */
    public static Date parse(String date, String format) {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日期相减 date2-date1
     * @return 相差多少天
     */
    public static int subtract(String date2, String date1, String format) {
        long times = parse(date2, format).getTime() - parse(date1, format).getTime() ;
        return (int) times/1000/60/60/24;
    }

    /**
     * 日期添加多少天 '年-月-日'
     * @return
     */
    public static String addDay(String date, String format, DateLy.FMT type, int days) {
        DateLy dateLy = new DateLy(date, format).add(type, days);
        return dateLy.getDate(format);
    }

    public static String nowUTC() {
        SimpleDateFormat foo = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        System.out.println("foo:"+foo.format(new Date()));

        Calendar gc = GregorianCalendar.getInstance();
        System.out.println("gc.getTime():"+gc.getTime());
        System.out.println("gc.getTimeInMillis():"+new Date(gc.getTimeInMillis()));

        //当前系统默认时区的时间：
        Calendar calendar=new GregorianCalendar();
        System.out.print("时区："+calendar.getTimeZone().getID()+"  ");
        System.out.println("时间："+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));
        //美国洛杉矶时区
        TimeZone tz= TimeZone.getTimeZone("America/Los_Angeles");
        //时区转换
        calendar.setTimeZone(tz);
        System.out.print("时区："+calendar.getTimeZone().getID()+"  ");
        System.out.println("时间："+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));
        Date time=new Date();

        //1、取得本地时间：
        java.util.Calendar cal = java.util.Calendar.getInstance();

        //2、取得时间偏移量：
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);

        //3、取得夏令时差：
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);

        //4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));

        //之后调用cal.get(int x)或cal.getTimeInMillis()方法所取得的时间即是UTC标准时间。
        System.out.println("UTC:"+new Date(cal.getTimeInMillis()));

        Calendar calendar1 = Calendar.getInstance();
        TimeZone tztz = TimeZone.getTimeZone("GMT");
        calendar1.setTimeZone(tztz);
        System.out.println(calendar.getTime());
        System.out.println(calendar.getTimeInMillis());
        return null;
    }

    public static void main(String[] args) {
//        System.out.println(new DateLy().getDate("yyyy"));
//        System.out.println(new DateLy().getDate("MM"));
//        System.out.println(new DateLy().getDate("hh"));
//        System.out.println(new DateLy().add(DateLy.FMT.DAY, 2).getDate(DateLy.DATETIME));
//        System.out.println(new DateLy().add(DateLy.FMT.MONTH, -20).getDate());
//        System.out.println(new DateLy(2019, 12, 2, 23,2,3).getDate(DateLy.DATETIME));
//        System.out.println(new DateLy("20190001 121212", "yyyyMMdd HHmmss").getDate(DateLy.DATETIME));

        nowUTC();

    }


}
