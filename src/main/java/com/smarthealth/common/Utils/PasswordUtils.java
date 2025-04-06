package com.smarthealth.common.Utils;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {

    /**
     * 对字符串进行 MD5 加密
     *
     * @param input 明文
     * @return 加密后的字符串（32 位小写）
     */
    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 算法不可用", e);
        }
    }


    /**
     * 对字符串进行加盐 MD5 加密
     *
     * @param input 明文
     * @return 加密后的字符串（32 位小写）
     */
    public static String md5WithSalt(String input) {
        //盐值
        String salt = "smarthealth";
        return md5(input + salt);
    }






}
