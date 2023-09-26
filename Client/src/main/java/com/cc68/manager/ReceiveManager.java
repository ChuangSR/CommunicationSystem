package com.cc68.manager;

import com.alibaba.fastjson2.JSON;
import com.cc68.Client;
import com.cc68.beans.MessageBean;
import com.cc68.message.HandleMessage;
import com.cc68.message.MessagePair;
import com.cc68.utils.MessageUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

public class ReceiveManager implements Runnable{
    private Client client;
    private Socket socket;
    private BufferedReader reader;

    private boolean flag = true;

    //用于存储服务器对于消息的回复，普通的消息将不会被存储
    private MessagePair messagePair;

    public ReceiveManager(Client client) throws IOException {
        this.socket = client.getSocket();
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.client = client;
    }

    @Override
    public void run() {
        try {
            String message;
            while (flag&&(message = reader.readLine())!=null){
                //存储数据到数据库
                MessageBean bean = JSON.parseObject(message, MessageBean.class);
                MessageUtil.saveMessage(bean, client.getConfig().get("account").toString());
                //向控制台发送数据
                HashMap<String, String> data = HandleMessage.handle(bean, client);
                ConsoleMessageManger.send(data);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 一个通过id获取数据的函数
     * @param ID 需要数据的id
     * @return
     */
    public MessageBean getReceive(String ID){
        String message = null;
        try {
            message = reader.readLine();
            MessageBean bean = JSON.parseObject(message, MessageBean.class);
            if (ID.equals(bean.getID())){
                //返回需要的数据
                return bean;
            }else {
                //在非需要的消息的情况下存储消息
                MessageUtil.saveMessage(bean, client.getConfig().get("account").toString());
                getReceive(ID);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void close() throws IOException {
        flag = false;

        reader.close();

    }
}
