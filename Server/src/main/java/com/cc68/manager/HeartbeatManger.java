package com.cc68.manager;


import com.cc68.beans.MessageBean;
import com.cc68.utils.MessageUtil;

import java.io.IOException;
import java.util.Properties;


/**
 * 心跳管理器
 */
public class HeartbeatManger implements Runnable{
    private UsersManager usersManager;

    private ReceiveManager receiveManager;

    private boolean flag = true;

    public HeartbeatManger(){}

    public HeartbeatManger(Properties config,UsersManager usersManager) throws IOException {
        this.usersManager = usersManager;
        receiveManager = new ReceiveManager(config,"heartbeatReceiveManagerPort");
    }

    @Override
    public void run() {
        try {
            System.out.print(MessageUtil.getTime());
            System.out.println(":心跳管理器开始运行！");
            while (flag){
                MessageBean bean = receiveManager.listen();
                usersManager.getUser(bean.getOriginator()).refresh();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close(){
        flag = false;
    }
}
