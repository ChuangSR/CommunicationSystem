﻿package com.cc68.thread;

import com.alibaba.fastjson2.JSON;
import com.cc68.Server;
import com.cc68.beans.MessageBean;
import com.cc68.beans.UserBean;
import com.cc68.message.HandleMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.Socket;

public class SocketThread implements Runnable, Serializable {
    private transient Server server;
    private transient UserBean userBean;
    private BufferedReader reader;
    private boolean flag = true;

    private long linkTime;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public SocketThread(){}

    public SocketThread(Server server, Socket socket,UserBean userBean) throws IOException {
        this.server = server;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.userBean = userBean;
    }

    @Override
    public void run() {
        while (flag){
            try {
                String message = reader.readLine();
                refresh();
                MessageBean bean = JSON.parseObject(message, MessageBean.class);
                MessageBean replyBean = HandleMessage.handle(bean, userBean,server);
                userBean.getSendManager().send(replyBean);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void refresh(){
        linkTime = System.currentTimeMillis()/1000;
    }

    public long getLinkTime(){
        return linkTime;
    }
    public void close(){
        flag =false;
    }
}