package com.cc68.message;

import com.cc68.beans.MessageBean;

/**
 * 用于存储一个消息对
 * 即，客户端的发出和服务器的回复
 */
public class MessagePair {
    //消息的发起人
    private String originator;

    private String receiver;

    private MessageBean send;

    private MessageBean reply;

    public MessagePair(){}

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public MessageBean getSend() {
        return send;
    }

    public void setSend(MessageBean send) {
        this.send = send;
    }

    public MessageBean getReply() {
        return reply;
    }

    public void setReply(MessageBean reply) {
        this.reply = reply;
    }
}
