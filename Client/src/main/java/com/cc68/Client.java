package com.cc68;


import com.cc68.beans.MessageBean;
import com.cc68.manager.ReceiveManager;
import com.cc68.manager.SendManager;
import com.cc68.utils.MessageUtil;
import org.apache.ibatis.io.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Properties;

public class Client {
    private Socket socket;
    private Properties config;

    private ReceiveManager receiveManager;

    //ReceiveManager
    private SendManager sendManager;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Properties getConfig() {
        return config;
    }

    public void setConfig(Properties config) {
        this.config = config;
    }

    public ReceiveManager getReceiveManager() {
        return receiveManager;
    }

    public void setReceiveManager(ReceiveManager receiveManager) {
        this.receiveManager = receiveManager;
    }

    public SendManager getSendManager() {
        return sendManager;
    }

    public void setSendManager(SendManager sendManager) {
        this.sendManager = sendManager;
    }

    public Client() throws IOException {
        config = Resources.getResourceAsProperties("config.properties");
        socket = new Socket(config.getProperty("ServerHost"),
                Integer.parseInt(config.getProperty("ServerPort")));
        sendManager = new SendManager(socket);
    }

    public boolean login(String account,String password) throws IOException {
        boolean flage = false;
        //存储账户名
        config.setProperty("account",account);
        //构建需要发送的数据
        String[] data = {account, MessageUtil.getMD5(password)};
        MessageBean bean = MessageUtil.buildMessage("login", data, account);
        //发送数据
        sendManager.send(bean);
        //构建接收器
        receiveManager = new ReceiveManager(this);
        //监听服务器对登录消息的返回
        MessageBean receive = receiveManager.getReceive(bean.getID());
        String status = receive.getData().get("status");
        if ("successful login".equals(status)){
            System.out.println("登录成功");
            flage = true;
        }else if ("failed login".equals(status)){
            System.out.println("账户或者密码错误");
        }else {
            System.out.println("未知异常");
        }
        return flage;
    }
}
