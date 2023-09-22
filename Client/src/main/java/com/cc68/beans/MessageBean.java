package com.cc68.beans;

import java.util.HashMap;

public class MessageBean {
    private String ID;

    private String type;

    private HashMap<String,String> data;

    public MessageBean(){

    }

    public MessageBean(String ID,String type){
        this.type = type;
    }

    public MessageBean(String ID, String type, HashMap<String, String> data) {
        this.ID = ID;
        this.type = type;
        this.data = data;
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
