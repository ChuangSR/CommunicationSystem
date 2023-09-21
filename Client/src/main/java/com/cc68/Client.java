package com.cc68;

import com.cc68.message.ListenServer;
import com.cc68.message.SendManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Properties;

public class Client {
    private Socket socket;
    private Properties config;

    private ListenServer listenServer;

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

        sendManager.sendAccountAndpassword(account,password);
        listenServer = new ListenServer(socket);
        listenServer.run();

        return false;
    }
}
