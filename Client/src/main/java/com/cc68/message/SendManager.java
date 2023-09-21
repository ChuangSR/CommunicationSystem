package com.cc68.message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SendManager {
    private Socket socket;

    private BufferedWriter writer;
    public SendManager(Socket socket) throws IOException {
        this.socket = socket;
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

    }

    public void send(String message) throws IOException {
        writer.write(message);
        writer.write("\n");
        writer.flush();
    }

    public void sendAccountAndpassword(String account,String password) throws IOException {
        send(account);
        send(Integer.toString(password.hashCode()));
    }
}
