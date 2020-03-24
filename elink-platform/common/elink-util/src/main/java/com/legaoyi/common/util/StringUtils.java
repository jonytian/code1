package com.legaoyi.common.util;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字符串工具类定义
 * 
 * @author liliang
 * @version 1.0
 */
public class StringUtils {
	private static final Logger logger = LoggerFactory.getLogger(StringUtils.class.getName());
    /**
     * 将输入流转换成字符串
     * 
     * @param is 输入流
     * @return 字符串
     * @throws IOException
     */
//    public static String inputStream2String(InputStream is) throws IOException {
//        return IOUtils.toString(is, CommonConst.CHARSET_UTF8);
//    }

    /**
     * 指定的字符串是否是null、空格、控制字符等检查。
     * <p>
     * 以下的情况返回值为true。
     * <ol>
     * <li>null</li>
     * <li>空字符串</li>
     * <li>全角半角空格</li>
     * </p>
     * 
     * @param str 检查字符
     * @return true:空白字符;false:非空白字符
     */
    public static boolean isNullOrSpace(String str) {
        if (str == null || str.equals("")) {
            return true;
        }

        // 左右含有[\t\n\x0B\f\r全角空格]的情况
        Pattern p1 = Pattern.compile("^[\\s　]+|[\\s　]+$");
        Matcher m1 = p1.matcher(str);
        // 除去空白字符
        String checkMoziretu = m1.replaceAll("");
        boolean returnValue = "".equals(checkMoziretu);
        return returnValue;
    }

    /**
     * 返回指定位数的前补零字符串。
     * 
     * @param data 字符串
     * @param size 指定位数
     * @param fixedLenNoTrim 不去除data的空格。
     * @throws IllegalArgumentException
     * @return 前补零字符串
     */
   /* public static String addPreZero(String data, int size, boolean fixedLenNoTrim) {
        int n = 0;

        final char type = '0';

        if (!fixedLenNoTrim && null == data) {
            return null;
        }

        if (!fixedLenNoTrim && "".equals(data.trim())) {
            return "";
        }

        if (data.trim().length() > size) {
            
        }

        if (fixedLenNoTrim) {
            n = size - data.length();
        } else {
            n = size - data.trim().length();
        }

        StringBuffer strBuff = new StringBuffer();
        for (int i = 0; i < n; i++) {
            strBuff.append(type);
        }

        if (fixedLenNoTrim) {
            return strBuff.append(data).toString();
        } else {
            return strBuff.append(data.trim()).toString();
        }
    }*/

    /**
     * 返回指定位数的前补零字符串。
     * 
     * @param data 字符串
     * @param size 指定位数
     * @throws IllegalArgumentException
     * @return 前补零字符串
     */
    /*public static String addPreZero(String data, int size) {
        return addPreZero(data, size, false);
    }*/

    /**
     * 字符串转码 默认是UTF-8
     * 
     * @param str 需要转码的字符串
     * @param format 如果format为空，则转码为UTF-8
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String changeStrEncode(String str, String format) throws UnsupportedEncodingException {
        String newStr = null;
        if (null == format.trim() || str.trim().length() == 0) {
            newStr = new String(str.getBytes(), CommonConst.CHARSET_UTF8);
        } else {
            newStr = new String(str.getBytes(), format);
        }
        return newStr;
    }

    /**
     * 将null对象变换成空白文字列，其它对象的情况下不变换。
     * 
     * @param obj 变换对象
     * @return 变换后对象
     */
    public static Object null2Space(Object obj) {

        if (obj == null) {
            return "";
        }
        return obj;
    }

    /**
     * 检查参数文字列是否为null、长度为0的空文字。
     * <p>
     * 参数不为null且其长度为1以上的情况下为true、其它以外的情况下返回false。
     * </p>
     * 
     * @param strValue 检查文字列
     * @return 判定结果
     */
    public static boolean isNotEmpty(String strValue) {

        if (null == strValue || strValue.trim().length() == 0) {
            return false;
        }

        return true;
    }

    /**
     * 检查参数文字列是否全为半角数字。
     * <p>
     * 只有当文字列为半角数字的文字的情况下，返回true。<br>
     * 不允许'-'（负号），即当是负数文字列的情况下，返回false。<br>
     * </p>
     * 
     * @param target 对象文字列。
     * @return true:文字列为半角数字。false:文字列不全为半角数字。
     */
    public static boolean isNumber(String target) {

        if (!isNotEmpty(target)) {
            return false;
        }

        // 检查是否为半角
        if (target.length() != target.getBytes().length) {
            return false;
        }

        // 检查是否为数字
        for (int i = 0; i < target.length(); i++) {
            if (!isNumber(target.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 检查参数文字列是否全为半角数字。
     * <p>
     * 只有当文字列为半角数字的文字的情况下，返回true。<br>
     * 不允许'-'（负号），即当是负数文字列的情况下，返回false。<br>
     * </p>
     * 
     * @param target 对象文字列。
     * @return true:文字列为半角数字。false:文字列不全为半角数字。
     */
    private static boolean isNumber(char target) {
        // 检查是否为半角
        String targetStr = String.valueOf(target);

        if (targetStr.length() != targetStr.getBytes().length) {
            return false;
        }

        // 检查是否为数字
        return Character.isDigit(target);
    }

    /**
     * <p>
     * 依据给定的正则表达式，判定给定的文字列是否与给定的正则表达式匹配。
     * </p>
     * 
     * @param regex 给定的正则表达式
     * @param str 匹配文字列
     * @return <li>true 文字列与正则表达式匹配。</li> <li>false 文字列与正则表达式不匹配或者文字列为null的情况下。</li>
     */
    public static boolean match(String regex, String str) {
        if (regex == null || str == null) {
            return false;
        }
        return Pattern.matches(regex, str);
    }

    /**
     * 生成指定位数的随机数字组成的字符串。
     * 
     * @param len 位数
     * @return 数字字符串
     */
    public static String getRandomArabicNumerals(int len) {
        char[] inChr = new char[len];
        char[] chr = "0123456789".toCharArray();
        Random r = new Random();
        int n = chr.length;
        int j = 0;
        while (true) {
            int index = r.nextInt(n);
            inChr[j] = chr[index];
            j++;
            if (j >= len) {
                break;
            }
        }
        return String.valueOf(inChr);
    }
}
