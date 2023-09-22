package com.cc68.beans;

import java.util.HashMap;

/**
 * 用于存储发出消息的类
 * 在之后会将数据转为json
 */
public class MessageBean {
    private String ID;

    private String originator;

    private String type;

    private HashMap<String,String> data;

    public MessageBean(){

    }

    public MessageBean(String ID, String originator, String type) {
        this.ID = ID;
        this.originator = originator;
        this.type = type;
    }

    public MessageBean(String ID, String originator, String type, HashMap<String, String> data) {
        this.ID = ID;
        this.originator = originator;
        this.type = type;
        this.data = data;
    }

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }
}
