package com.cc68.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class ConsoleMessageManger {
    public static void send(HashMap<String,String> message){
        String type = message.get("type");
        switch (type) {
            case "login" -> login(message);
            case "logon" -> logon(message);
            case "list" -> list(message);
            case "offline" -> offline(message);
            case "sideText" -> sideText(message);
        }
    }

    private static void sideText(HashMap<String, String> message) {
        StringBuilder builder = new StringBuilder();
        builder.append(message.get("originator")).append(":").append(message.get("message"));
        System.out.println(builder);
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
        Set<String> keySet = message.keySet();
        for (String key : keySet){
            if ("type".equals(key)){
                continue;
            }
            System.out.println(message.get(key));
        }
    }
}
