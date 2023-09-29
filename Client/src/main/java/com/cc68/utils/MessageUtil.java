package com.cc68.utils;

import com.alibaba.fastjson2.JSON;
import com.cc68.Client;
import com.cc68.beans.MessageBean;
import com.cc68.beans.MessageDatabaseBean;
import org.apache.ibatis.session.SqlSession;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MessageUtil {
    //构造消息
    public static MessageBean buildMessage(String type, String[] data, String account){
        HashMap<String,String> temp = new HashMap<>();
        MessageBean bean = new MessageBean(getID(type,account),account,type,temp);
        switch (type) {
            case "login" -> login(temp, data);
            case "logon" -> logon(temp, data);
            case "changPwd" -> changPwd(temp, data);
            case "heart" -> heart(temp,data);
            case "list" -> list(temp,data);
            case "online" -> online(temp,data);
        }
        return bean;
    }

    private static void online(HashMap<String, String> temp, String[] data) {
        temp.put("time",data[0]);
    }


    //获取消息的ID
    private static String getID(String type,String account){
        long timeMillis = System.currentTimeMillis();
        StringBuilder builder = new StringBuilder();
        builder.append(timeMillis).append(type).append(account);
        return getMD5(builder.toString());
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
            StringBuilder result = new StringBuilder(bigInt.toString(16));
            // 不足32位高位补零
            while(result.length() < 32) {
                result.insert(0, "0");
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  将MessageBean转为MessageDatabaseBean，MessageDatabaseBean用于存储数据
     * @param messageBean 发送的数据对象
     * @param account 目标账户
     * @return MessageDatabaseBean
     */
    private static MessageDatabaseBean toMessageDatabaseBean(MessageBean messageBean,String account){
        MessageDatabaseBean bean = new MessageDatabaseBean();
        bean.setID(messageBean.getID());
        bean.setOriginator(messageBean.getOriginator());
        bean.setReceiver(account);
        bean.setType(messageBean.getType());
        bean.setMessage(JSON.toJSONString(messageBean.getData()));
        bean.setTime(getTime());
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

    public static void readMessage(String ID){

    }
    public static String getTime(){
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        return date.toString();
    }
    private static void login(HashMap<String,String> temp,String[] data){
        temp.put("account",data[0]);
        temp.put("password",getMD5(data[1]));
    }

    private static void logon(HashMap<String,String> temp,String[] data){
        temp.put("account",data[0]);
        temp.put("password",getMD5(data[1]));
    }

    private static void changPwd(HashMap<String,String> temp,String[] data){
        temp.put("account",data[0]);
        temp.put("password",getMD5(data[1]));
        temp.put("pwdNew",getMD5(data[2]));
    }

    private static void list(HashMap<String,String> temp,String[] data){

    }

    private static void heart(HashMap<String, String> temp, String[] data) {

    }
}
