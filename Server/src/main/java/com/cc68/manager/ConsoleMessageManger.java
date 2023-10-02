package com.cc68.manager;

import com.cc68.beans.MessageBean;

import java.util.HashMap;

public class ConsoleMessageManger {
    public static void send(HashMap<String,String> message){
        String type = message.get("type");
        switch (type){
            case "login" -> login(message);
            case "logon" -> logon(message);
            case "changPwd" -> changPwd(message);
            case "list" -> list(message);
            case "sideText" -> sideText(message);
        }
    }

    private static void sideText(HashMap<String, String> message) {
        String time = message.get("time");
        String originator = message.get("originator");
        String receiver = message.get("receiver");
        String s = message.get("message");

        StringBuilder builder = new StringBuilder();

        builder.append(time).append(":").append("\n\t用户:").
                append(originator).append("\n\t事件:发送信息").
                append("\n\t接收者:").append(receiver).append("\n\t信息：")
                .append(s);
        System.out.println(builder);
    }

    private static void list(HashMap<String, String> message) {
        String time = message.get("time");
        String account = message.get("account");

        StringBuilder builder = new StringBuilder();

        builder.append(time).append(":").append("\n\t用户:").
                append(account).append("\n\t事件:查看用户");

        System.out.println(builder);
    }

    private static void changPwd(HashMap<String, String> message) {
        String time = message.get("time");
        String account = message.get("account");
        String status = message.get("status");

        StringBuilder builder = new StringBuilder();

        builder.append(time).append(":").append("\n\t用户:").
                append(account).append("\n\t事件:修改密码");

        if ("200".equals(status)){
            builder.append("\n\t状态:成功");
        }else {
            builder.append("\n\t状态:失败");
        }

        System.out.println(builder);
    }

    private static void logon(HashMap<String, String> message) {
        String time = message.get("time");
        String account = message.get("account");
        String status = message.get("status");

        StringBuilder builder = new StringBuilder();

        builder.append(time).append(":").append("\n\t用户:").
                append(account).append("\n\t事件:注册");

        if ("200".equals(status)){
            builder.append("\n\t状态:成功");
        }else {
            builder.append("\n\t状态:失败\n\t备注:用户已存在");
        }

        System.out.println(builder);
    }

    private static void login(HashMap<String,String> message){
        String time = message.get("time");
        String account = message.get("account");
        String status = message.get("status");
        String loginStatus = message.get("loginStatus");

        StringBuilder builder = new StringBuilder();

        builder.append(time).append(":").append("\n\t用户:").
                append(account).append("\n\t事件:登录");
        if ("200".equals(status)){
            builder.append("\n\t状态:成功");
        }else {
            builder.append("\n\t状态:失败");
        }

        if (loginStatus!=null){
            builder.append("\n\t备注:已登录");
        }

        System.out.println(builder);
    }
}
