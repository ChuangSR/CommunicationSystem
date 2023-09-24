package com.cc68;


import com.cc68.beans.MessageBean;
import com.cc68.manager.HeartbeatManger;
import com.cc68.manager.ReceiveManager;
import com.cc68.manager.SendManager;
import com.cc68.utils.MessageUtil;
import org.apache.ibatis.io.Resources;

import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

public class Client {
    private String account;

    private Properties config;

    private Socket socket;

    private ReceiveManager receiveManager;

    //ReceiveManager
    private SendManager sendManager;

    private HeartbeatManger heartbeatManger;

    public Socket getSocket() {
        return socket;
    }

    public Properties getConfig() {
        return config;
    }

    public String getAccount() {
        return account;
    }

    public Client() throws IOException {
        config = Resources.getResourceAsProperties("config.properties");
    }

    public boolean login(String account,String password) throws IOException {
        this.socket = new Socket(config.getProperty("ServerHost"),
                Integer.parseInt(config.getProperty("ServerPort")));
        //创建发送管理器
        sendManager = new SendManager(socket);

        //是否登录成功
        boolean flag = false;
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

            heartbeatManger = new HeartbeatManger(config);
            Thread heartbeatThread = new Thread(heartbeatManger);
            heartbeatThread.start();

            Thread receiveThread = new Thread(receiveManager);
            receiveThread.start();

            flag = true;
        }else if ("failed login".equals(status)){
            System.out.println("账户或者密码错误");
            close();

        }else {
            System.out.println("未知异常");
            close();
        }
        return flag;
    }

    public void close() throws IOException {
        sendManager.close();
        receiveManager.close();
    }
}
