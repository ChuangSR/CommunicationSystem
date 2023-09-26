package com.cc68.manager;

import com.alibaba.fastjson2.JSON;
import com.cc68.beans.MessageBean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Properties;

public class SendManager {
    private Socket socket;

    private BufferedWriter writer;

    public SendManager(Properties config,String serverHost,String serverPort) throws IOException {
        this.socket = new Socket(config.getProperty(serverHost),
                Integer.parseInt(config.getProperty(serverPort)));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

    }

    public SendManager(Socket socket) throws IOException {
        this.socket = socket;
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }


    public void send(MessageBean bean) throws IOException {
        String data = JSON.toJSONString(bean);
        writer.write(data);
        writer.write("\n");
        writer.flush();
    }

    public void close() throws IOException {
        writer.close();
        socket.close();
    }
}
