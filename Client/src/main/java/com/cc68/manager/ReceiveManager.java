package com.cc68.manager;

import com.cc68.message.MessagePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceiveManager implements Runnable{
    private Socket socket;
    private BufferedReader reader;

    private boolean flage = true;

    //用于存储服务器对于消息的回复，普通的消息将不会被存储
    private MessagePair messagePair;

    public ReceiveManager(Socket socket) throws IOException {
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            String message = null;
            while (flage&&(message = reader.readLine())!=null){
                if (message.indexOf("服务器")==0){
//                    messagePair.setReply(message);
                }
                System.out.println(message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void exit(){
        flage = false;
    }
}
