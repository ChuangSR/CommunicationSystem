package com.cc68.utils;

import com.cc68.Client;
import com.cc68.beans.MessageBean;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class MessageBuilder {
    //构造消息
    public static MessageBean buildMessage(String type, String[] data, String account){
        HashMap<String,String> temp = new HashMap<>();
        MessageBean bean = new MessageBean(getID(type,account),type,temp);
        switch (type){
            case "login":
                temp.put("account",data[0]);
                temp.put("password",Integer.toString(data[1].hashCode()));
                break;
        }
        return bean;
    }

    //获取消息的ID
    private static String getID(String type,String account){
        long timeMillis = System.currentTimeMillis();
        StringBuilder builder = new StringBuilder();
        builder.append(timeMillis).append(type).append(account);
        String md5 = getMD5(builder.toString());
        return md5;
    }

    //加密函数
    public static String getMD5(String input) {
        if(input == null || input.length() == 0) {
            return null;
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(input.getBytes());
            byte[] byteArray = md5.digest();

            BigInteger bigInt = new BigInteger(1, byteArray);
            // 参数16表示16进制
            String result = bigInt.toString(16);
            // 不足32位高位补零
            while(result.length() < 32) {
                result = "0" + result;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
