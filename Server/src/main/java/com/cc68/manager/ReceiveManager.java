package com.cc68.manager;

import com.alibaba.fastjson2.JSON;
import com.cc68.Server;
import com.cc68.beans.MessageBean;
import com.cc68.beans.UserBean;
import com.cc68.utils.MessageUtil;
import com.cc68.utils.SqlUtil;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

public class ReceiveManager implements Runnable{
    private Server server;
    private ServerSocket serverSocket;
    private boolean flage = true;
    //用于存储服务器对于消息的回复，普通的消息将不会被存储

    public ReceiveManager(Server server) throws IOException {
        this.server = server;
        this.serverSocket = server.getServer();
    }

    @Override
    public void run() {
        try {
            Socket accept = serverSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
            String message = null;
            while ((message =reader.readLine()) != null){
                MessageBean bean = JSON.parseObject(message, MessageBean.class);
                if ("login".equals(bean.getType())){
                    login(bean,accept);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void login(MessageBean bean,Socket socket){
        HashMap<String, String> data = bean.getData();
        SqlSession sqlSession = SqlUtil.getSqlSession();

        UserBean user = new UserBean(data.get("account"),data.get("password"));
        Cursor<Object> cursor = sqlSession.selectCursor("user.login", user);
        if (cursor != null){
            server.getUsers().put(data.get("account"),socket);
        }else {
//            MessageUtil.buildMessage();
        }
    }

    public void close(){
        flage = false;
    }
}
