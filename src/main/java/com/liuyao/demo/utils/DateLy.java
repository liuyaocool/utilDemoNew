package com.liuyao.demo.utils;

import java.text.ParseException;

public class DateLy {

    public static final String TIME = "HH:mm:ss";
//    yyyy-M-d HH:mm:ss
    public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE = "yyyy-MM-dd";//

    public enum FMT {
        YEAR(2, "yyyy"),
        MONTH(1, "MM"),
        DAY(86400000, "dd"),
        HOUR24(86400000, "HH"),//24小时
        HOUR12(43200000, "hh"),//12小时
        HOUR1(3600000, "hh"),//1小时
        MINUTE(60000, "mm"),//分钟
        SECOND(1000, "ss"),//秒
        ;
        private long time;
        private String fmt;

        FMT(long time, String fmt) {
            this.time = time;
            this.fmt = fmt;
        }
        public long getTime() { return time; }
        public String getFmt() { return fmt; }
    }

    private transient long times;

    public DateLy() { this.times = System.currentTimeMillis(); }

    public DateLy(java.util.Date date){
        this();
        this.times = null == date ? this.times : date.getTime();
    }

    public DateLy(String time, String format) {
        this();
        setTimes(time, format);
    }

    //顺序 年月日时分秒 24小时制
    public DateLy(int... time) {
        this();
        StringBuilder t = new StringBuilder();
        int i = 0;
        for (; i < 6 && i < time.length; i++) {
            time[i] = time[i] < 0 ? 0 : time[i];
            t.append(time[i] < 10 ? "0" + time[i] : time[i]);
        }
        for (; i < 3; i++) {
            t.append("01");
        }
        for (; i < 6; i++) {
            t.append("00");
        }
        setTimes(t.toString(), "yyyyMMddHHmmss");
    }

    private void setTimes(String time, String format){
        try {
            this.times = new java.text.SimpleDateFormat(format).parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public java.util.Date getDate(){ return new java.util.Date(this.times); }

    public String getDate(String format){
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
        return sdf.format(getDate());
    }

    public int getDate(FMT fmt){
        return Integer.valueOf(getDate(fmt.getFmt()));
    }

    public DateLy add(FMT fmt, int amount){
        int year = getDate(FMT.YEAR);
        switch (fmt){
            case MONTH:
                int month = getDate(FMT.MONTH);
                int months = 12 * year + month + amount;
                this.times += new DateLy(months/12, months%12).subtract(new DateLy(year, month));
                break;
            case YEAR: this.times += new DateLy(year + amount).subtract(new DateLy(year));break;
            default: this.times += fmt.getTime() * amount; break;
        }
        return this;
    }

    public long subtract(DateLy date){
        return this.times - date.times;
    }

}
