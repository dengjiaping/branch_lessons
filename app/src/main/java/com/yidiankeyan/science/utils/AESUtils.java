package com.yidiankeyan.science.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理 对原始数据进行AES加密后，在进行Base64编码转化；
 */
public class AESUtils {

    /*
     * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    private static final String sKey = "24e43dd92aa94f78";// key，可自行修改
    private static final String ivParameter = "8080955884228368";// 偏移量,可自行修改ssss

    public static String aesValue(String str,String phone){
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            long currentTime = System.currentTimeMillis();  //1508139676445
//            long currentTime = 1508139676445l;
            byte[] encodeBase64 = Base64.encode(str.getBytes(),Base64.DEFAULT);
            String encodedString = new String(encodeBase64);
            String encryptValue = encodedString.trim()+"."+currentTime+"."+AESUtils.sKey;
            String md5Value = md5(encryptValue);  //MD5加密后的value
            String newEncryptValue = encodedString.trim()+"."+currentTime+"."+md5Value.trim();

            byte[] raw = sKey.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encryptedValue = cipher.doFinal(newEncryptValue.trim().getBytes("utf-8"));
            return Base64.encodeToString(encryptedValue,Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(string.getBytes());
            StringBuilder sb =new StringBuilder();
            for(byte b:result){
                int number = b&0xff;
                String hex = Integer.toHexString(number);
                if(hex.length() == 1){
                    sb.append("0"+hex);
                }else{
                    sb.append(hex);
                }
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    // 加密
    public static String encrypt(String sSrc) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        return new String(Base64.encode(encrypted, Base64.DEFAULT), "UTF-8");// 此处使用BASE64做转码。
    }

    // 解密
    public static String decrypt(String sSrc) throws Exception {
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64.decode(sSrc, Base64.DEFAULT);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        // 需要加密的字串
////		String cSrc = "[{\"request_no\":\"1001\",\"service_code\":\"FS0001\",\"contract_id\":\"100002\",\"order_id\":\"0\",\"phone_id\":\"13913996922\",\"plat_offer_id\":\"100094\",\"channel_id\":\"1\",\"activity_id\":\"100045\"}]";
//        String cSrc = "/jys509/p/4768120.html";
//
//        // 加密
//        long lStart = System.currentTimeMillis();
//        String enString = AESUtils.encrypt(cSrc);
//        System.out.println("加密后的字串是：" + enString);
//
//        long lUseTime = System.currentTimeMillis() - lStart;
//        System.out.println("加密耗时：" + lUseTime + "毫秒");
//        // 解密
//        lStart = System.currentTimeMillis();
//        String DeString = AESUtils.decrypt(enString);
//        System.out.println("解密后的字串是：" + DeString);
//        lUseTime = System.currentTimeMillis() - lStart;
//        System.out.println("解密耗时：" + lUseTime + "毫秒");
        String aesValue = aesValue("{\"nationCode\":\"86\",\"phoneNumber\":\"18518122143\"}","18518122143");
        System.out.println("aesValue===="+aesValue);
    }

}