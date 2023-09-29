package com.cc68.beans;

import com.cc68.manager.SendManager;

import java.io.IOException;
import java.net.Socket;

/**
 * 一个用户的类，用于存储用户的一些信息和执行一些操作
 */
public class UserBean {
    private String account;
    private String password;

    private SendManager sendManager;

    private long heartbeat;

    private boolean threadStatus = true;
    public UserBean(){}

    public UserBean(String account, String passwowrd) {
        this.account = account;
        this.password = passwowrd;
        heartbeat = System.currentTimeMillis()/1000;
    }

    public void setSocket(Socket socket) throws IOException {
        sendManager = new SendManager(socket);
    }

    public SendManager getSendManager() {
        return sendManager;
    }

    public void setSendManager(SendManager sendManager) {
        this.sendManager = sendManager;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void close() throws IOException {
        sendManager.close();
    }

    public long getHeartbeat() {
        return heartbeat;
    }

    public boolean isThreadStatus() {
        return threadStatus;
    }

    public void setThreadStatus(boolean threadStatus) {
        this.threadStatus = threadStatus;
    }

    public void refresh(){
        heartbeat = System.currentTimeMillis()/1000;
    }
}
