package com.liuyao.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static void main(String[] args) {
        System.out.println(new DateLy().getDate("yyyy"));
        System.out.println(new DateLy().getDate("MM"));
        System.out.println(new DateLy().getDate("hh"));
        System.out.println(new DateLy().add(DateLy.FMT.DAY, 2).getDate(DateLy.DATETIME));
        System.out.println(new DateLy().add(DateLy.FMT.MONTH, -20).getDate());
        System.out.println(new DateLy(2019, 12, 2, 23,2,3).getDate(DateLy.DATETIME));
        System.out.println(new DateLy("20190001 121212", "yyyyMMdd HHmmss").getDate(DateLy.DATETIME));

    }


}
