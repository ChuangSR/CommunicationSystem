package com.cc68;

import com.cc68.beans.MessageBean;
import com.cc68.beans.UserBean;
import com.cc68.manager.ReceiveManager;
import com.cc68.manager.UsersManager;
import org.apache.ibatis.io.Resources;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Properties;

public class Server {
    private ServerSocket server;
    //服务器的基本配置

    private Properties config;


    private UsersManager usersManager;

    private ReceiveManager receiveManager;


    private boolean flage = true;
    public ServerSocket getServer() {
        return server;
    }

    public void setServer(ServerSocket server) {
        this.server = server;
    }

    public Properties getConfig() {
        return config;
    }

    public void setConfig(Properties config) {
        this.config = config;
    }

    public UsersManager getUsersManager() {
        return usersManager;
    }

    public void setUsersManager(UsersManager usersManager) {
        this.usersManager = usersManager;
    }

    public Server() throws IOException {
        this.config = Resources.getResourceAsProperties("config.properties");
        usersManager = new UsersManager();
        server = new ServerSocket(Integer.parseInt(config.getProperty("port")));
        receiveManager = new ReceiveManager(this);

    }

    public void start() throws IOException {
        while (flage){
            MessageBean messageBean = receiveManager.listen();
            UserBean userBean = null;
            //判读是否为登录事件，改事件较为特殊
            if ("login".equals(messageBean.getType())){
                HashMap<String, String> data = messageBean.getData();
                userBean = new UserBean(data.get("account"),data.get("password"));
                userBean.setSocket(receiveManager.getAccept());
            }else {
                userBean = usersManager.getUser(messageBean.getOriginator());
            }
            MessageBean replyBean = HandleMessage.handle(messageBean, userBean,this);
            userBean.getSendManager().send(replyBean);
        }
    }

    public void close(){

    }
}
