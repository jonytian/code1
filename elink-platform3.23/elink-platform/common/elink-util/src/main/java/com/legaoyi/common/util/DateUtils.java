package com.legaoyi.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
public class DateUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private DateUtils() {}

    public static String format(Date date) {
        return format(date, DATE_FORMAT);
    }

    public static Date formatDate(Date date) {
        try {
            return parse(format(date), DATE_FORMAT);
        } catch (Exception e) {
        }
        return date;
    }

    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return returnValue;
    }

    public static Date parse(String date) throws ParseException {
        return parse(date, DATE_FORMAT);
    }

    public static Date parse(String date, String pattern) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.parse(date);
    }

    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(2, n);
        return cal.getTime();
    }

    public static Date addDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(5, n);
        return cal.getTime();
    }

    public static int getYear(Date date) {
        return Integer.parseInt(format(date, "yyyy"));
    }

    public static int getYear(long timestamp) {
        return Integer.parseInt(format(new Date(timestamp), "yyyy"));
    }

    public static int getMonth(Date date) {
        return Integer.parseInt(format(date, "yyyyMM"));
    }

    public static int getMonth(long timestamp) {
        return Integer.parseInt(format(new Date(timestamp), "yyyyMM"));
    }

    public static int getDate(Date date) {
        return Integer.parseInt(format(date, "yyyyMMdd"));
    }

    public static int getDate(long timestamp) {
        return Integer.parseInt(format(new Date(timestamp), "yyyyMMdd"));
    }

    public static int getDateTime(Date date) {
        return Integer.parseInt(format(date, "yyyyMMddHHmmss"));
    }

    public static int getDateTime(long timestamp) {
        return Integer.parseInt(format(new Date(timestamp), "yyyyMMddHHmmss"));
    }

    /**
     * 得到本月的第一天 （yyyy-MM-dd格式）
     * 
     * @return
     */
    public static String getMonthFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return format(calendar.getTime(), DATE_FORMAT);
    }

    /**
     * 得到月份的第一天 （yyyy-MM-dd格式）
     * 
     * @param date yyyy-MM-dd格式
     * @return
     */
    public static String getMonthFirstDay(String date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parse(date, DATE_FORMAT));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return format(calendar.getTime(), DATE_FORMAT);
    }

    /**
     * 得到月份的第一天 （yyyy-MM-dd格式）
     * 
     * @param date
     * @return
     */
    public static String getMonthFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return format(calendar.getTime(), DATE_FORMAT);
    }

    /**
     * 得到本月的最后一天（yyyy-MM-dd格式）
     *
     * @return
     */
    public static String getMonthLastDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return format(calendar.getTime(), DATE_FORMAT);
    }

    /**
     * 得到本月的最后一天（yyyy-MM-dd格式）
     * 
     * @param date yyyy-MM-dd格式
     * @return
     */
    public static String getMonthLastDay(String date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parse(date, DATE_FORMAT));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return format(calendar.getTime(), DATE_FORMAT);
    }

    /**
     * 得到本月的最后一天（yyyy-MM-dd格式）
     * 
     * @param date
     * @return
     */
    public static String getMonthLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return format(calendar.getTime(), DATE_FORMAT);
    }
}
