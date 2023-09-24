package com.cc68.manager;

import com.alibaba.fastjson2.JSON;
import com.cc68.Server;
import com.cc68.beans.MessageBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ReceiveManager{
    private Server server;
    private ServerSocket serverSocket;
    private boolean flage = true;
    //用于存储服务器对于消息的回复，普通的消息将不会被存储

    private Socket accept = null;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public Socket getAccept() {
        return accept;
    }

    public ReceiveManager(Server server) throws IOException {
        this.server = server;
        this.serverSocket = server.getServer();
    }

    public MessageBean listen() throws IOException {
        accept = serverSocket.accept();
        System.out.println("接收到一个连接");
        MessageBean bean = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
        String message = null;
        while ((message =reader.readLine()) != null){
            System.out.println("读取数据成功");
            bean = JSON.parseObject(message, MessageBean.class);
            message = null;
        }

        return bean;
    }

//    private void login(MessageBean bean,Socket socket){
//        HashMap<String, String> data = bean.getData();
//        SqlSession sqlSession = SqlUtil.getSqlSession();
//
//        UserBean user = new UserBean(data.get("account"),data.get("password"));
//        Cursor<Object> cursor = sqlSession.selectCursor("user.login", user);
//        String[] status = new String[1];
//        MessageBean replyBean = MessageUtil.replyMessage(bean.getID(),"login",status,server);
//
//        if (cursor != null){
//            server.getUsers().put(data.get("account"),socket);
//            status[0] = "successful login";
//        }else {
//            status[0] = "failed login";
//        }
//
//    }

    public void close(){
        flage = false;
    }
}
