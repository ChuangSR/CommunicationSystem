package com.cc68.manager;

import com.alibaba.fastjson2.JSON;
import com.cc68.beans.MessageBean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;

public class SendManager {
    private Socket socket;

    private BufferedWriter writer;
    public SendManager(Socket socket) throws IOException {
        this.socket = socket;
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

    }

    public void send(MessageBean bean) throws IOException {
        String data = JSON.toJSONString(bean);
        writer.write(data +"\n");
        writer.flush();
        socket.shutdownOutput();
    }

    public void close() throws IOException {
        writer.close();
    }
}
