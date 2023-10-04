package com.cc68;


import com.cc68.beans.MessageBean;
import com.cc68.manager.ConsoleMessageManger;
import com.cc68.manager.HeartbeatManger;
import com.cc68.manager.ReceiveManager;
import com.cc68.manager.SendManager;
import com.cc68.message.HandleMessage;
import com.cc68.utils.MessageUtil;
import org.apache.ibatis.io.Resources;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;

public class Client {
    private String account;

    private Properties config;

    private Socket socket;

    private ReceiveManager receiveManager;

    private SendManager sendManager;

    private HeartbeatManger heartbeatManger;

    private boolean status = true;

    public void setAccount(String account) {
        this.account = account;
    }

    public void setConfig(Properties config) {
        this.config = config;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
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

    public HeartbeatManger getHeartbeatManger() {
        return heartbeatManger;
    }

    public void setHeartbeatManger(HeartbeatManger heartbeatManger) {
        this.heartbeatManger = heartbeatManger;
    }

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

    private MessageBean init(String type,String... data) throws IOException {
        this.socket = new Socket(config.getProperty("ServerHost"),
                Integer.parseInt(config.getProperty("ServerPort")));
        //创建发送管理器
        sendManager = new SendManager(this,socket);
        //存储账户名
        config.setProperty("account",data[0]);
        account = data[0];
        MessageBean bean = MessageUtil.buildMessage(type, data, account);
        //发送数据
        sendManager.send(bean);
        //构建接收器
        receiveManager = new ReceiveManager(this);
        //监听服务器对登录消息的返回
        MessageBean receive = receiveManager.getReceiveFrontLogin(bean.getID());

        return receive;
    }

    public boolean login(String account,String password) throws IOException {
        //是否登录成功
        MessageBean bean = init("login", account, password);
        HashMap<String, String> data = HandleMessage.handle(bean, this);
        boolean flag = Boolean.parseBoolean(data.get("status"));
        ConsoleMessageManger.send(data);
        return flag;
    }

    public void logon(String account,String password) throws IOException {
        MessageBean bean = init("logon", account, password);
        HashMap<String, String> data = HandleMessage.handle(bean,this);
    }

    public void changPwd(String account,String password,String pwdNew) throws IOException {
        MessageBean bean = init("changPwd", account, password, pwdNew);
        HashMap<String, String> data = HandleMessage.handle(bean,this);
        ConsoleMessageManger.send(data);
    }

    public void online(MessageBean old) throws IOException {
        status = true;
        this.socket = new Socket(config.getProperty("ServerHost"),
                Integer.parseInt(config.getProperty("ServerPort")));
        //创建发送管理器
        sendManager = new SendManager(this,socket);
        //构建接收器
        receiveManager = new ReceiveManager(this);

        String[] data = {MessageUtil.getTime()};
        MessageBean bean = MessageUtil.buildMessage("online", data, account);

        sendManager.send(bean);

        MessageBean replyBean = receiveManager.getReceiveFrontLogin(bean.getID());
        sendManager.send(old);
//        HashMap<String, String> handle = HandleMessage.handle(replyBean,this);

        Thread receiveThread = new Thread(receiveManager);
        receiveThread.start();

    }

    public void offline() throws IOException {
        receiveManager.close();
        sendManager.close();
        socket.close();
        status = false;
    }

    public void list() throws IOException, InterruptedException {
        MessageBean bean = MessageUtil.buildMessage("list", null, account);
        sendManager.send(bean);
//        MessageBean receive = receiveManager.getReceiveAfterLogin(bean.getID());
//        System.out.println(JSON.toJSONString(receive));
//        HashMap<String, String> data = HandleMessage.handle(receive, this);
//        ConsoleMessageManger.send(data);
    }

    public void sideText() throws IOException {
        System.out.print("请输入用户账号:");
        Scanner scanner = new Scanner(System.in);
        String account = scanner.next();
        System.out.println("输入!exit退出");
        while (true){
            String message = scanner.next();
            if ("!exit".equals(message)){
                break;
            }
            String[] data = {account,message};
            MessageBean bean = MessageUtil.buildMessage("sideText",data,this.account);
            sendManager.send(bean);
        }
    }

    public void close() throws IOException {
        sendManager.close();
        receiveManager.close();
        socket.close();
        if (heartbeatManger != null){
            heartbeatManger.close();
        }
    }

    public boolean getStatus(){
        return status;
    }
}
