package com.cc68.beans;

/**
 * 存储数据库数据的类
 */
public class MessageDatabaseBean {
    private String ID;

    private String originator;

    private String receiver;

    private String message;

    private String time;

    private String type;

    public MessageDatabaseBean(){}

    public MessageDatabaseBean(String originator, String receiver, String message, String time, String type) {
        this.originator = originator;
        this.receiver = receiver;
        this.message = message;
        this.time = time;
        this.type = type;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
