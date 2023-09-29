package com.cc68.manager;

import com.cc68.beans.MessageBean;
import com.cc68.utils.MessageUtil;

import java.io.IOException;
import java.util.Properties;

public class HeartbeatManger implements Runnable{
    private String account;
    private SendManager sendManager;

    private String heartbeatHost;

    private String heartbeatPort;

    //心跳的间隔时间
    private int heartbeatTime;


    private boolean flag = true;

    public HeartbeatManger(){}

    public HeartbeatManger(Properties config) throws IOException {
        account = config.getProperty("account");
        heartbeatTime = Integer.parseInt(config.getProperty("heartbeatTime"));
        heartbeatHost = config.getProperty("heartbeatHost");
        heartbeatPort = config.getProperty("heartbeatPort");
    }

    @Override
    public void run() {
        while (flag){
            try {
                Thread.sleep(heartbeatTime * 1000);
                sendManager = new SendManager(heartbeatHost,heartbeatPort);
                MessageBean bean = MessageUtil.buildMessage("heart",null, account);
                sendManager.send(bean);
                sendManager.close(); 
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void close(){
        flag = false;
    }
}
