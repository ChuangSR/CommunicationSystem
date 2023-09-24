package com.cc68.manager;

import com.cc68.beans.MessageBean;
import com.cc68.utils.MessageUtil;

import java.io.IOException;
import java.util.Properties;

public class HeartbeatManger implements Runnable{
    private String account;
    private SendManager sendManager;
    //心跳的间隔时间
    private int heartbeatTime;

    private boolean flag = true;

    public HeartbeatManger(){}

    public HeartbeatManger(Properties config) throws IOException {
        account = config.getProperty("account");
        sendManager = new SendManager(config,"heartbeatHost","heartbeatPort");
        heartbeatTime = Integer.parseInt(config.getProperty("heartbeatTime"));
    }

    @Override
    public void run() {
        while (flag){
            try {
                Thread.sleep(heartbeatTime * 1000);
                MessageBean bean = MessageUtil.buildMessage("heart", new String[0], account);
                sendManager.send(bean);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void close(){
        flag = false;
    }
}
