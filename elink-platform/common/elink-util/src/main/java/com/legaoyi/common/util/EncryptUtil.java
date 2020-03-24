package com.legaoyi.common.util;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;

public class EncryptUtil {

    private static final byte[] DESIV = new byte[] {0x12, 0x34, 0x56, 120, (byte) 0x90, (byte) 0xab, (byte) 0xcd, (byte) 0xef};// 向量

    private AlgorithmParameterSpec iv = null;// 加密算法的参数接口

    private Key key = null;

    public static final String charset = "utf-8";

    public static final String DEFAULT_DES_KEY = "809e1fbaddae4ae98faa97045db91c47";

    /**
     * 初始化
     * 
     * @param deSkey 密钥
     * @throws Exception
     */
    public EncryptUtil(String deSkey) throws Exception {
        DESKeySpec keySpec = new DESKeySpec(deSkey.getBytes(charset));// 设置密钥参数
        iv = new IvParameterSpec(DESIV);// 设置向量
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 获得密钥工厂
        key = keyFactory.generateSecret(keySpec);// 得到密钥对象
    }

    /**
     * 加密
     * 
     * @param data
     * @return
     * @throws Exception
     */
    public String encode(String data) throws Exception {
        Cipher enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");// 得到加密对象Cipher
        enCipher.init(Cipher.ENCRYPT_MODE, key, iv);// 设置工作模式为加密模式，给出密钥和向量
        byte[] pasByte = enCipher.doFinal(data.getBytes("utf-8"));
        return Base64.encodeBase64String(pasByte);
    }

    /**
     * 解密
     * 
     * @param data
     * @return
     * @throws Exception
     */
    public String decode(String data) throws Exception {
        Cipher deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        deCipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] pasByte = deCipher.doFinal(Base64.decodeBase64(data));
        return new String(pasByte, "UTF-8");
    }

    public static void main(String[] args) {
        try {
            String test = "ershuai";
            String key = "9ba45bfd500642328ec03ad8ef1b6e75";// 自定义密钥
            EncryptUtil des = new EncryptUtil(key);
            System.out.println("加密前的字符：" + test);
            System.out.println("加密后的字符：" + des.encode(test));
            System.out.println("解密后的字符：" + des.decode(des.encode(test)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
