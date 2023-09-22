package com.cc68;


import com.cc68.beans.MessageBean;
import com.cc68.manager.ReceiveManager;
import com.cc68.manager.SendManager;
import com.cc68.utils.MessageBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Properties;

public class Client {
    private Socket socket;
    private Properties config;

    private ReceiveManager receiveManager;

    //ReceiveManager
    private SendManager sendManager;

    public Client() throws IOException {
        config = readConfig();
        socket = new Socket(config.getProperty("ServerHost"),
                Integer.parseInt(config.getProperty("ServerPort")));
        sendManager = new SendManager(socket);
    }
    private Properties readConfig() throws IOException {
        InputStream resourceAsStream = this.getClass().getResourceAsStream("config.properties");
        Properties config = new Properties();
        config.load(resourceAsStream);
        resourceAsStream.close();
        return config;
    }
    public boolean login(String account,String password) throws IOException {
        config.setProperty("account",account);
        String[] data = {account,MessageBuilder.getMD5(password)};
        MessageBean bean = MessageBuilder.buildMessage("login", data, account);
        sendManager.send(bean);

        return false;
    }
}
