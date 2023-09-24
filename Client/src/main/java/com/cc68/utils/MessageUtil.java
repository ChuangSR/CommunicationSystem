package com.cc68.utils;

import com.cc68.Client;
import com.cc68.beans.MessageBean;
import com.cc68.beans.MessageDatabaseBean;
import org.apache.ibatis.session.SqlSession;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class MessageUtil {
    //构造消息
    public static MessageBean buildMessage(String type, String[] data, String account){
        HashMap<String,String> temp = new HashMap<>();
        MessageBean bean = new MessageBean(getID(type,account),account,type,temp);
        switch (type){
            case "login":
                temp.put("account",data[0]);
                temp.put("password",getMD5(data[1]));
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

    /**
     *  将MessageBean转为MessageDatabaseBean，MessageDatabaseBean用于存储数据
     * @param messageBean 发送的数据对象
     * @param account 目标账户
     * @return
     */
    private static MessageDatabaseBean toMessageDatabaseBean(MessageBean messageBean,String account){
        MessageDatabaseBean bean = new MessageDatabaseBean();
        bean.setOriginator(messageBean.getOriginator());
        bean.setReceiver(account);
        bean.setType(messageBean.getType());
        bean.setMessage(messageBean.getData().get("message"));
        return bean;
    }

    /**
     * 存储消息到数据库
     * @param bean 被发送的消息
     * @param account 接收对象
     */

    public static void saveMessage(MessageBean bean,String account){
        SqlSession session = SqlUtil.getSqlSession();
        MessageDatabaseBean mdbean = MessageUtil.toMessageDatabaseBean(bean,account);
        session.insert("insert_data",mdbean);
        session.commit();
        session.close();
    }
}
