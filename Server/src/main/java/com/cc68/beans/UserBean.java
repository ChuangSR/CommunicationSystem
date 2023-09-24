package com.cc68.beans;

import com.cc68.manager.SendManager;

import java.io.IOException;
import java.net.Socket;

public class UserBean {
    private String account;
    private String passwowrd;

    private Socket socket;

    private SendManager sendManager;



    public UserBean(){}

    public UserBean(String account, String passwowrd) {
        this.account = account;
        this.passwowrd = passwowrd;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) throws IOException {
        this.socket = socket;
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

    public String getPasswowrd() {
        return passwowrd;
    }

    public void setPasswowrd(String passwowrd) {
        this.passwowrd = passwowrd;
    }


    public void close() throws IOException {
        sendManager.close();
        socket.close();
    }
}
