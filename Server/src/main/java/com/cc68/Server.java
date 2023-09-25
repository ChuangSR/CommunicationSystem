package com.cc68;

import com.cc68.beans.MessageBean;
import com.cc68.beans.UserBean;
import com.cc68.manager.HeartbeatManger;
import com.cc68.manager.ReceiveManager;
import com.cc68.manager.UsersManager;
import com.cc68.manager.Wrecker;
import com.cc68.message.HandleMessage;
import org.apache.ibatis.io.Resources;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 *
 */
public class Server {
    //服务器的基本配置信息
    private Properties config;

    private UsersManager usersManager;

    private ReceiveManager receiveManager;


    private HeartbeatManger heartbeatManger;


    private Wrecker wrecker;


    private boolean flage = true;

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
        receiveManager = new ReceiveManager(config,"serverPort");
        heartbeatManger = new HeartbeatManger(config,usersManager);
        wrecker = new Wrecker(usersManager,config);


        Thread heartThread = new Thread(heartbeatManger);
        heartThread.start();

        Thread wreckerThread = new Thread(wrecker);
        wreckerThread.start();
    }

    public void start() throws IOException {
        while (flage){
            MessageBean messageBean = receiveManager.listen();
            UserBean userBean;
            //判读是否为登录事件，改事件较为特殊
            if ("login".equals(messageBean.getType()) || "logon".equals(messageBean.getType())
            ||"changPwd".equals(messageBean.getType())){
                HashMap<String, String> data = messageBean.getData();
                userBean = new UserBean(data.get("account"),data.get("password"));
                userBean.setSocket(receiveManager.getAccept());
            }else {
                userBean = usersManager.getUser(messageBean.getOriginator());
            }
            MessageBean replyBean = HandleMessage.handle(messageBean, userBean,this);
            userBean.getSendManager().send(replyBean);

            if ("logon".equals(messageBean.getType())||"changPwd".equals(messageBean.getType())){
                userBean.close();
            }
        }
    }

    public void close(){

    }
}
