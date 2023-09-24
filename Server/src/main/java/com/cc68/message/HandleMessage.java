package com.cc68.message;

import com.cc68.Server;
import com.cc68.beans.MessageBean;
import com.cc68.beans.UserBean;
import com.cc68.utils.MessageUtil;
import com.cc68.utils.SqlUtil;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;

/**
 * 用于处理各种类型的消息
 *  如：登录请求
 *      普通消息等
 */
public class HandleMessage {
    public static MessageBean handle(MessageBean messageBean, UserBean userBean, Server server){
        MessageBean reply = null;
        switch (messageBean.getType()){
            case "login":
                reply = login(messageBean,userBean,server);
                break;
            case "logon":
                reply = logon(messageBean,userBean,server);
                break;
        }
        return reply;
    }

    private static MessageBean login(MessageBean messageBean,UserBean userBean,Server server){
        //打开数据库连接
        SqlSession sqlSession = SqlUtil.getSqlSession();

        //查询数据
        Cursor<Object> cursor = sqlSession.selectCursor("user.login", userBean);
        String[] data = new String[2];


        if (cursor != null){
            data[0] = "200";
            data[1] = "successful login";
            //向用户管理器添加数据
            server.getUsersManager().addUser(userBean);
        }else {
            data[0] = "400";
            data[1] = "failed login";
            //关闭bean对象
            try {
                userBean.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //构建返回数据
        MessageBean reply = MessageUtil.replyMessage(messageBean.getID(),"login",data,server);
        sqlSession.close();
        return reply;
    }

    private static MessageBean logon(MessageBean messageBean,UserBean userBean,Server server){
        SqlSession sqlSession = SqlUtil.getSqlSession();

        //查询数据
        Cursor<Object> cursor = sqlSession.selectCursor("user.check", userBean);
        String[] data = new String[2];

        if (cursor != null){
            data[0] = "400";
            data[1] = "账户已存在！";
        }else {
            sqlSession.insert("user.logon",userBean);
            data[0] = "200";
            data[1] = "注册成功";
            sqlSession.commit();
        }


        MessageBean reply = MessageUtil.replyMessage(messageBean.getID(),"logon",data,server);
        sqlSession.commit();
        try {
            userBean.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
