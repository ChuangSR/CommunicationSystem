package com.cc68.manager;

import com.alibaba.fastjson2.JSON;
import com.cc68.Server;
import com.cc68.beans.MessageBean;
import com.cc68.thread.SocketThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class ReceiveManager{
    private Server server;

    private ServerSocket serverSocket;
    private boolean flage = true;

    private Socket accept = null;

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public Socket getAccept() {
        return accept;
    }

    public ReceiveManager(Properties config, String key) throws IOException {
        this.serverSocket = new ServerSocket(Integer.parseInt(config.getProperty(key)));
    }

    public ReceiveManager(Server server,Properties config,String key) throws IOException {
        this.server = server;
        this.serverSocket = new ServerSocket(Integer.parseInt(config.getProperty(key)));
    }

    public MessageBean listen(){
        MessageBean bean = null;
        try {
            accept = serverSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
            String message = reader.readLine();
            bean = JSON.parseObject(message, MessageBean.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public void close() throws IOException {
        accept.close();
        serverSocket.close();
    }
}
