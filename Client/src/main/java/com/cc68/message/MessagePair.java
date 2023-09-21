package com.cc68.message;

/**
 * 用于存储一个消息对
 * 即，客户端的发出和服务器的回复
 */
public class MessagePair {
    private String send;

    private String reply;

    public MessagePair(){}

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
