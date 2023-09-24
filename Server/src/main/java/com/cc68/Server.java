package com.cc68;

import org.apache.ibatis.io.Resources;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Properties;

public class Server {
    private ServerSocket server;

    private HashMap<String, Socket> users;

    private Properties config;

    public ServerSocket getServer() {
        return server;
    }

    public void setServer(ServerSocket server) {
        this.server = server;
    }

    public HashMap<String, Socket> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, Socket> users) {
        this.users = users;
    }

    public Properties getConfig() {
        return config;
    }

    public void setConfig(Properties config) {
        this.config = config;
    }

    public Server() throws IOException {
        this.config = Resources.getResourceAsProperties("config.properties");
        users = new HashMap<>();
    }

    public void start() throws IOException {
        server = new ServerSocket(Integer.parseInt(config.getProperty("port")));


    }
}
