package com.cc68.message;

import com.alibaba.fastjson2.JSON;
import com.cc68.Server;
import com.cc68.beans.MessageBean;
import com.cc68.beans.UserBean;
import com.cc68.manager.UsersManager;
import com.cc68.thread.SocketThread;
import com.cc68.utils.MessageUtil;
import com.cc68.utils.SqlUtil;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 用于处理各种类型的消息
 *  如：登录请求
 *      普通消息等
 */
public class HandleMessage {
    private static HashMap<String,String> log;

    public static MessageBean handle(MessageBean messageBean, UserBean userBean, Server server) throws IOException {
        log = new HashMap<>();
        log.put("type",messageBean.getType());
        log.put("account",userBean.getAccount());
        MessageBean bean = switch (messageBean.getType()) {
            case "login" -> login(messageBean, userBean, server);
            case "logon" -> logon(messageBean, userBean, server);
            case "changPwd" -> changPwd(messageBean, userBean, server);
            case "list" -> list(messageBean,userBean,server);
            case "sideText" -> sideText(messageBean,userBean,server);
            default -> null;
        };
        log.put("time",MessageUtil.getTime());

        return bean;
    }

    public static HashMap<String,String> getLog(){
        return log;
    }

    private static MessageBean sideText(MessageBean messageBean, UserBean userBean, Server server) throws IOException {
        HashMap<String, String> data = messageBean.getData();
        log.put("originator",messageBean.getOriginator());
        log.put("receiver",data.get("receiver"));
        log.put("message",data.get("message"));

        //flag属性表示是否回复
        data.put("flag","false");
        data.put("originator",messageBean.getOriginator());
        String receiver = data.get("receiver");
        UserBean receiverUser = server.getUsersManager().getUser(receiver);
        receiverUser.getSendManager().send(messageBean);
        return messageBean;
    }


    private static MessageBean login(MessageBean messageBean,UserBean userBean,Server server){
        //打开数据库连接
        SqlSession sqlSession = SqlUtil.getSqlSession();

        //查询数据
        UserBean temp = sqlSession.selectOne("user.login", userBean);
        String[] data = new String[2];


        if (temp != null){
            data[0] = "200";
            data[1] = "successful login";
            //预先删除防止重复登录
            UserBean oldBean = server.getUsersManager().getUser(userBean.getAccount());
            if (oldBean!=null){
                log.put("loginStatus","true");
                try {
                    server.getUsersManager().deleteUser(oldBean);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            //向用户管理器添加数据
            server.getUsersManager().addUser(userBean);
            Socket socket = server.getReceiveManager().getAccept();
            SocketThread thread;
            try {
                thread = new SocketThread(server, socket, userBean);
                server.getPool().add(thread);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            log.put("status","200");
        }else {
            data[0] = "400";
            data[1] = "failed login";
            log.put("status","400");
        }
        //构建返回数据
        sqlSession.close();
        return MessageUtil.replyMessage(messageBean.getID(), "login", data, server);
    }

    private static MessageBean logon(MessageBean messageBean,UserBean userBean,Server server){
        SqlSession sqlSession = SqlUtil.getSqlSession();
        //查询数据
        UserBean temp = sqlSession.selectOne("user.check", userBean);
        String[] data = new String[2];

        if (temp != null){
            data[0] = "400";
            data[1] = "账户已存在！";
            log.put("status","400");
        }else {
            System.out.println(JSON.toJSONString(userBean));
            sqlSession.insert("user.logon",userBean);
            data[0] = "200";
            data[1] = "注册成功";
            sqlSession.commit();
            log.put("status","200");
        }
        sqlSession.close();
        return MessageUtil.replyMessage(messageBean.getID(), "logon", data, server);
    }

    private static MessageBean changPwd(MessageBean messageBean,UserBean userBean,Server server){
        SqlSession sqlSession = SqlUtil.getSqlSession();

        //查询数据
        Cursor<Object> cursor = sqlSession.selectCursor("user.check", userBean);

        String[] data = new String[2];

        if (cursor == null){
            data[0] = "400";
            data[1] = "账户不存在！";
            log.put("status","400");
        }else {
            userBean.setPassword(messageBean.getData().get("pwdNew"));
            sqlSession.insert("user.changPwd",userBean);
            data[0] = "200";
            data[1] = "密码修改成功!";
            sqlSession.commit();
            log.put("status","200");
        }
        sqlSession.close();
        return MessageUtil.replyMessage(messageBean.getID(),"logon",data,server);
    }

    private static MessageBean list(MessageBean messageBean,UserBean userBean,Server server) {
        UsersManager usersManager = server.getUsersManager();
        ArrayList<UserBean> all = usersManager.getAll();
        String[] data = new String[all.size()];
        for (int i = 0;i < all.size();i++){
            data[i] = all.get(i).getAccount();
        }
        return MessageUtil.replyMessage(messageBean.getID(),"list",data,server);
    }
}
