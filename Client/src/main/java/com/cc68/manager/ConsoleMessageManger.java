package com.cc68.manager;

import java.util.Collection;
import java.util.HashMap;

public class ConsoleMessageManger {
    public static void send(HashMap<String,String> message){
        String type = message.get("type");
        switch (type) {
            case "login" -> login(message);
            case "logon" -> logon(message);
            case "list" -> list(message);
            case "offline" -> offline(message);
        }
    }

    private static void offline(HashMap<String, String> message) {

    }


    private static void login(HashMap<String, String> message) {
        System.out.println(message.get("message"));
    }

    private static void logon(HashMap<String, String> message) {
        System.out.println(message.get("status"));
        System.out.println(message.get("message"));
    }


    private static void list(HashMap<String, String> message) {
        Collection<String> values = message.values();
        for (String temp:values){
            System.out.println(temp);
        }
    }
}
