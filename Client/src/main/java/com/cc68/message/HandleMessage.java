package com.cc68.message;

import com.cc68.Client;
import com.cc68.beans.MessageBean;
import com.cc68.manager.HeartbeatManger;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

/**
 * 用于处理消息
 */
public class HandleMessage {
    public static HashMap<String,String> handle(MessageBean bean, Client client) throws IOException {
        HashMap<String,String> temp = new HashMap<>();
        temp.put("type",bean.getType());
        switch (bean.getType()) {
            case "login" -> login(bean, temp, client);
            case "logon" -> logon(bean, temp, client);
            case "list" -> list(bean,temp,client);
        }
        return temp;
    }


    private static void login(MessageBean bean,HashMap<String,String> temp, Client client) throws IOException {
        HashMap<String, String> data = bean.getData();
        if ("200".equals(data.get("status"))){
            temp.put("status","true");
            //运行心跳管理器
            client.setHeartbeatManger(new HeartbeatManger(client.getConfig()));
            Thread heartbeatThread = new Thread(client.getHeartbeatManger());
            heartbeatThread.start();

            //运行接收器
            Thread receiveThread = new Thread(client.getReceiveManager());
            receiveThread.start();

            temp.put("message",data.get("message"));
        }else if ("400".equals(data.get("status"))){
            temp.put("status","false");
            temp.put("message",data.get("message"));
            client.close();
        }else {
            temp.put("status","false");
            temp.put("message","未知异常");
            client.close();
        }
    }

    private static void logon(MessageBean bean, HashMap<String, String> temp, Client client) throws IOException {
        HashMap<String, String> data = bean.getData();
        temp.put("status",data.get("status"));
        temp.put("message",data.get("message"));
        client.close();
    }

    private static void list(MessageBean bean, HashMap<String, String> temp, Client client) {
        HashMap<String, String> data = bean.getData();
        Collection<String> values = data.values();
        int i = 0;
        for (String value:values) {
            StringBuilder builder = new StringBuilder();
            builder.append(bean.getOriginator()).append(":").append(value);
            temp.put(Integer.toString(i),builder.toString());
        }
    }
}
