package com.example.sys.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    /**
     * 日期转换成字符串
     *
     * @param date
     * @return str
     */
    public static String format(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
    public static Date parse(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 处理日期
     */
    public static String toNormalDate(String time) {
        time = time.replace("Z", " UTC");//UTC是本地时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        Date d = null;
        try {
            d = format.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //此处是将date类型装换为字符串类型，比如：Sat Nov 18 15:12:06 CST 2017转换为2017-11-18 15:12:06
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sf.format(d);
        System.out.println(date);//这里输出的是date类型字符串
        System.out.println(d);//这里输出的是date类型的时间
        System.out.println(d.getTime() / 1000);//这里输出的是以秒为单位的long类型的时间。如果需要一毫秒为单位，可以不用除1000.
        System.out.println(sf.format(d));//这里输出的是字符串类型的时间

        return date;
    }




}


