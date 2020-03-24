package com.legaoyi.protocol.util;

import java.math.BigInteger;
import java.nio.charset.Charset;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-11-12
 */
public class ByteUtils {

    private ByteUtils() {}

    public static int byte2int(byte b) {
        return b & 0xFF;
    }

    public static String byte2bin(byte b) {
        return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1) + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1) + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1) + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }

    public static String byte2hex(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return hex.toUpperCase();
    }

    public static int bytesToInt(byte[] bytes, int offset) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (3 - i) * 8;
            value += ((bytes[i + offset] & 0xFF) << shift);
        }
        return value;
    }

    public static int bytesToInt(byte[] bytes) {
        StringBuilder buffer = new StringBuilder();
        for (byte b : bytes) {
            String bin = Integer.toBinaryString(byte2int(b));
            StringBuilder standardBin = new StringBuilder(bin);
            for (int i = bin.length(); i < 8; i++) {
                standardBin.insert(0, "0");
            }
            buffer.append(standardBin.toString());
        }
        BigInteger rs = new BigInteger(buffer.toString(), 2);
        return rs.intValue();
    }

    public static String bytes2gbk(byte[] bytes) {
        return new String(bytes, Charset.forName("GBK")).replaceAll("\\u0000", "");
    }

    public static String bytes2gb2312(byte[] bytes) {
        return new String(bytes, Charset.forName("GB2312")).replaceAll("\\u0000", "");
    }

    public static String bytes2ascii(byte[] bytes) throws Exception {
        return new String(bytes, "ascii");
    }

    public static String bytes2hex(byte[] bytes) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret.append(hex.toUpperCase());
        }
        return ret.toString();
    }

    public static int word2int(byte[] bytes) {
        StringBuilder rst = new StringBuilder();
        StringBuilder bin = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            int n = byte2int(bytes[i]);
            bin.append(Integer.toBinaryString(n));
            for (int j = bin.toString().length(); j < 8; j++) {
                bin.insert(0, "0");
            }
            rst.append(bin.toString());
            bin.delete(0, bin.length());
        }
        BigInteger result = new BigInteger(rst.toString(), 2);
        return result.intValue();
    }

    public static String word2bin(byte[] bytes) {
        StringBuilder rst = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            StringBuilder bin = new StringBuilder(Integer.toBinaryString(byte2int(bytes[i])));
            for (int j = bin.toString().length(); j < 8; j++) {
                bin.insert(0, "0");
            }
            rst.append(bin.toString());
        }
        return rst.toString();
    }

    public static long dword2long(byte[] bytes) {
        StringBuilder buffer = new StringBuilder("");
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; i--) {
            String binary = Integer.toBinaryString(byte2int(bytes[i]));
            sb.append(binary);
            for (int j = binary.length(); j < 8; j++) {
                sb.insert(0, "0");
            }
            buffer.insert(0, sb);
            sb.delete(0, sb.length());
        }
        BigInteger rs = new BigInteger(buffer.toString(), 2);
        return rs.longValue();
    }

    public static String dword2bin(byte[] bytes) {
        StringBuilder buffer = new StringBuilder("");
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; i--) {
            String binary = Integer.toBinaryString(byte2int(bytes[i]));
            sb.append(binary);
            for (int j = binary.length(); j < 8; j++) {
                sb.insert(0, "0");
            }
            buffer.insert(0, sb);
            sb.delete(0, sb.length());
        }
        return buffer.toString();
    }

    public static byte[] gbk2bytes(String s) {
        return s.getBytes(Charset.forName("GBK"));
    }

    public static byte[] gbk2bytes(String s, int length) {
        MessageBuilder mb = new MessageBuilder();
        byte[] b = s.getBytes(Charset.forName("GBK"));
        int n = b.length;
        if (n < length) {
            mb.append(b);
            byte b1 = 0x00;
            for (int i = n; i < length; i++) {
                mb.append(b1);
            }
        } else if (n > length) {
            byte[] dest = new byte[length];
            System.arraycopy(b, 0, dest, 0, length);
            mb.append(dest);
        } else {
            mb.append(b);
        }
        return mb.getBytes();
    }

    public static byte[] gb23122bytes(String s, int length) {
        byte[] srcb = s.getBytes(Charset.forName("GB2312"));
        byte[] descb = new byte[length];
        int l = descb.length - srcb.length;
        if (l > 0) {
            System.arraycopy(srcb, 0, descb, 0, srcb.length);
            for (int i = 0; i < l; i++) {
                descb[srcb.length + i] = 0;
            }
        } else if (l < 0) {
            System.arraycopy(srcb, 0, descb, 0, descb.length);
        } else {
            return srcb;
        }
        return descb;
    }

    public static byte[] bcd2bytes(String s, int lenth) {
        String temp = s;
        int l = lenth;
        l <<= 1;
        while (l - temp.length() > 0) {
            temp = "0" + temp;
        }
        int len = temp.length() / 2;
        byte[] result = new byte[len];
        char[] achar = temp.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (char2byte(achar[pos]) << 4 | char2byte(achar[pos + 1]));
        }
        return result;
    }

    public static byte[] ascii2bytes(String s) throws Exception {
        return s.getBytes("ascii");
    }

    public static byte[] ascii2bytes(String s, int length) throws Exception {
        byte[] src = s.getBytes("ascii");
        byte[] desc = new byte[length];
        int l = desc.length - src.length;
        if (l > 0) {
            System.arraycopy(src, 0, desc, 0, src.length);
            for (int i = 0; i < l; i++) {
                desc[src.length + i] = 0;
            }
        } else if (l < 0) {
            System.arraycopy(src, 0, desc, 0, desc.length);
        } else {
            return src;
        }
        return desc;
    }


    public static byte int2byte(int val) {
        String binary = Integer.toBinaryString(val);
        StringBuilder buffer = new StringBuilder(binary);
        for (int i = binary.length(); i < 8; i++) {
            buffer.insert(0, "0");
        }
        BigInteger vals = new BigInteger(buffer.toString(), 2);
        return vals.byteValue();
    }

    public static byte[] int2word(int val) {
        byte[] result = new byte[2];
        String binary = Integer.toBinaryString(val);
        StringBuilder buffer = new StringBuilder(binary);
        for (int i = binary.length(); i < 16; i++) {
            buffer.insert(0, "0");
        }
        for (int i = 0; i < 2; i++) {
            String vv = buffer.substring(8 * i, 8 * (i + 1));
            BigInteger vals = new BigInteger(vv, 2);
            result[i] = vals.byteValue();
        }
        return result;
    }

    public static byte[] int2dword(int val) {
        byte[] result = new byte[4];
        String binary = Integer.toBinaryString(val);
        StringBuilder buffer = new StringBuilder(binary);
        for (int i = binary.length(); i < 32; i++) {
            buffer.insert(0, "0");
        }
        for (int i = 0; i < 4; i++) {
            String vv = buffer.substring(8 * i, 8 * (i + 1));
            BigInteger vals = new BigInteger(vv, 2);
            result[i] = vals.byteValue();
        }
        return result;
    }

    public static byte[] long2dword(long val) {
        byte[] result = new byte[4];
        String binary = Long.toBinaryString(val);
        StringBuilder buffer = new StringBuilder(binary);
        for (int i = binary.length(); i < 32; i++) {
            buffer.insert(0, "0");
        }
        for (int i = 0; i < 4; i++) {
            String vv = buffer.substring(8 * i, 8 * (i + 1));
            BigInteger vals = new BigInteger(vv, 2);
            result[i] = vals.byteValue();
        }
        return result;
    }

    public static int hex2int(String s) {
        BigInteger bint = new BigInteger(s, 16);
        return bint.intValue();
    }

    public static byte[] hex2word(String s) {
        return int2word(hex2int(s));
    }

    public static byte[] hex2dword(String s) {
        return int2dword(hex2int(s));
    }

    public static byte[] hex2bytes(String s) {
        String hex = s.toUpperCase();
        if (hex.length() % 2 == 1) {
            hex = "0" + hex;
        }
        int len = hex.length() / 2;
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (char2byte(achar[pos]) << 4 | char2byte(achar[pos + 1]));
        }
        return result;
    }

    public static int bin2int(String s) {
        BigInteger big = new BigInteger(s, 2);
        return big.intValue();
    }

    public static long bin2long(String s) {
        BigInteger big = new BigInteger(s, 2);
        return big.longValue();
    }

    private static byte char2byte(char c) {
        return (byte) ("0123456789ABCDEF".indexOf(c));
    }

    public static byte bcd2byte(byte asc) {
        byte bcd;
        if ((asc >= '0') && (asc <= '9'))
            bcd = (byte) (asc - '0');
        else if ((asc >= 'A') && (asc <= 'F'))
            bcd = (byte) (asc - 'A' + 10);
        else if ((asc >= 'a') && (asc <= 'f'))
            bcd = (byte) (asc - 'a' + 10);
        else
            bcd = (byte) (asc - 48);
        return bcd;
    }

    public static byte[] bcd2bytes(byte[] ascii, int len) {
        byte[] bcd = new byte[len / 2];
        int j = 0;
        for (int i = 0; i < (len + 1) / 2; i++) {
            bcd[i] = bcd2byte(ascii[j++]);
            bcd[i] = (byte) (((j >= len) ? 0x00 : bcd2byte(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }

    /***
     * 适用于"0123456789ABCDEF"
     * @param bytes
     * @return
     */
    public static String bytes2bcd(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp).toLowerCase();
    }
    
//    /***
//     * 字符串为纯数字（0~9）时适用
//     * 
//     * @param bytes
//     * @return
//     */
//    public static String bytes2bcd(byte[] bytes) {
//        StringBuilder buffer = new StringBuilder();
//        for (byte b : bytes) {
//            int rs = bcdToInt(b);
//            if (rs < 10) {
//                buffer.append("0");
//            }
//            buffer.append(rs);
//        }
//        return buffer.toString();
//    }
}
