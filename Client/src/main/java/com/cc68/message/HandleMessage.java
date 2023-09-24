package com.cc68.message;

import com.cc68.Client;
import com.cc68.beans.MessageBean;

/**
 * 用于处理消息
 */
public class HandleMessage {
    public static void handle(MessageBean bean, Client client){
        switch (bean.getType()){
            case "login":

                break;
        }
    }
}
