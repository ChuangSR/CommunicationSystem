package com.cc68;

import com.alibaba.fastjson2.JSON;
import com.cc68.beans.MessageBean;
import com.cc68.beans.UserBean;
import com.cc68.manager.HeartbeatManger;
import com.cc68.manager.ReceiveManager;
import com.cc68.manager.UsersManager;
import com.cc68.manager.Wrecker;
import com.cc68.message.HandleMessage;
import com.cc68.thread.SocketPool;
import com.cc68.thread.SocketThread;
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

    private SocketPool pool;

    private boolean flage = true;

    public SocketPool getPool() {
        return pool;
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

    public ReceiveManager getReceiveManager() {
        return receiveManager;
    }

    public Server() throws IOException {
        this.config = Resources.getResourceAsProperties("config.properties");
        usersManager = new UsersManager();
        receiveManager = new ReceiveManager(this,config,"serverPort");
        heartbeatManger = new HeartbeatManger(config,usersManager);
        wrecker = new Wrecker(usersManager,config);
        pool = new SocketPool(this,Integer.parseInt(config.getProperty("poolMax")),60);

        Thread heartThread = new Thread(heartbeatManger);
        heartThread.start();

        Thread wreckerThread = new Thread(wrecker);
        wreckerThread.start();

        Thread poolThread = new Thread(pool);
        poolThread.start();
    }

    public void start() throws IOException {
        while (flage){
            MessageBean messageBean = receiveManager.listen();
            if ("online".equals(messageBean.getType())){
                continue;
            }
            System.out.println(JSON.toJSONString(messageBean));

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
            System.out.println(JSON.toJSONString(replyBean));
            userBean.getSendManager().send(replyBean);

            if ("logon".equals(messageBean.getType())||"changPwd".equals(messageBean.getType())
            || ("login".equals(replyBean.getType())&&"400".equals(replyBean.getData().get("status")))){
                SocketThread thread = pool.getThread(userBean);
                thread.close();
                userBean.close();
            }
        }
    }

    public void close(){

    }
}
