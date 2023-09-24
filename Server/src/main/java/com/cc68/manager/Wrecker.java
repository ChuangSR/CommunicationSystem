package com.cc68.manager;

import com.cc68.beans.UserBean;
import com.cc68.utils.MessageUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * 在客户端下线之后，会对于客户端对象的连接进行摧毁
 */
public class Wrecker implements Runnable{
    private UsersManager usersManager;
    private int heartbeatTime;

    private boolean flage = true;

    public Wrecker(){}

    public Wrecker(UsersManager usersManager, Properties config) {
        this.usersManager = usersManager;
        heartbeatTime = Integer.parseInt(config.getProperty("HeartbeatTime"));
    }

    @Override
    public void run() {
        System.out.print(MessageUtil.getTime());
        System.out.println(":摧毁程序开始运行！");
        while (flage){
            try {
                Thread.sleep(1000);
                ArrayList<UserBean> all = usersManager.getAll();
                for (UserBean bean:all){
                    long heartbeat = bean.getHeartbeat();

                    if (heartbeat - System.currentTimeMillis()/1000 > heartbeatTime){
                        usersManager.deleteUser(bean);
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void close(){
        flage = false;
    }
}
