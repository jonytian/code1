package com.legaoyi.protocol.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
public class DateUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private DateUtils() {

    }

    public static String format(Date date) {
        return format(date, DATETIME_FORMAT);
    }

    public static String format(Date date, String format) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(format);
            returnValue = df.format(date);
        }
        return returnValue;
    }

    public static Date parse(String date) {
        return parse(date, DATETIME_FORMAT);
    }

    public static Date parse(String date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        try {
            return df.parse(date);
        } catch (ParseException e) {

        }
        return null;
    }

    public static String bcd2dateTime(String bcd) {
        StringBuilder sb = new StringBuilder(bcd);
        for (int i = 0; i < sb.length(); i++) {
            if (i % 3 == 0) {
                if (i > 8)
                    sb.insert(i, ":");
                else {
                    sb.insert(i, "-");
                }
            }
        }
        String t = sb.deleteCharAt(0).insert(0, "20").toString();
        return t.replaceFirst(":", " ");
    }

    public static long bcd2Timestamp(String bcd) {
        String date = "20" + bcd;
        return parse(date, "yyyyMMddHHmmss").getTime();
    }

    public static String timestamp2bcd(long time) {
        return format(new Date(time), "yyyyMMddHHmmss").substring(2);
    }

    public static String dateTime2bcd(String dateTime) {
        return dateTime.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "").substring(2);
    }

    public static byte[] dateTime2utc(String dateTime) {
        Date d = parse(dateTime);
        long seconds = d.getTime() / 1000L;
        int temp = Integer.valueOf(String.valueOf(seconds));
        byte[] b = new byte[8];
        for (int i = b.length - 1; i > -1; i--) {
            b[i] = new Integer(temp & 0xFF).byteValue();
            temp >>= 8;
        }
        return b;
    }

    public static byte[] date2utc(String date) {
        Date d = parse(date, DATE_FORMAT);
        long seconds = d.getTime() / 1000L;
        int temp = Integer.valueOf(String.valueOf(seconds));
        byte[] b = new byte[8];
        for (int i = b.length - 1; i > -1; i--) {
            b[i] = new Integer(temp & 0xFF).byteValue();
            temp >>= 8;
        }
        return b;
    }

    public static byte[] unix2utc(long time) {
        long seconds = time / 1000L;
        int temp = Integer.valueOf(String.valueOf(seconds));
        byte[] b = new byte[8];
        for (int i = b.length - 1; i > -1; i--) {
            b[i] = new Integer(temp & 0xFF).byteValue();
            temp >>= 8;
        }
        return b;
    }

}
