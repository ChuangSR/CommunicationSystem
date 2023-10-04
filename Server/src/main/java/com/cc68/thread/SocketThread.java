package com.cc68.thread;

import com.alibaba.fastjson2.JSON;
import com.cc68.Server;
import com.cc68.beans.MessageBean;
import com.cc68.beans.UserBean;
import com.cc68.manager.ConsoleMessageManger;
import com.cc68.message.HandleMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.Socket;

public class SocketThread implements Runnable {
    private Server server;
    private UserBean userBean;

    private Socket socket;
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
        this.socket = socket;
        this.userBean = userBean;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        linkTime = System.currentTimeMillis()/1000;
    }

    @Override
    public void run() {
        while (flag){
            refresh();
            try {
                String message = reader.readLine();
                if (message == null){
                    continue;
                }
                MessageBean bean = JSON.parseObject(message, MessageBean.class);
                MessageBean replyBean = HandleMessage.handle(bean, userBean,server);
                boolean flag = Boolean.parseBoolean(replyBean.getData().get("flag"));
                if (flag){
                    userBean.getSendManager().send(replyBean);
                }
                ConsoleMessageManger.send(HandleMessage.getLog());
            } catch (IOException e) {
                return;
            }
        }
    }

    private void refresh(){
        linkTime = System.currentTimeMillis()/1000;
    }

    public long getLinkTime(){
        return linkTime;
    }
    public void close() throws IOException {
        flag =false;
        reader.close();
        socket.close();
    }
}
