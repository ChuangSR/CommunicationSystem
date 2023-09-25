package com.cc68.message;

import com.alibaba.fastjson2.JSON;
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
        return switch (messageBean.getType()) {
            case "login" -> login(messageBean, userBean, server);
            case "logon" -> logon(messageBean, userBean, server);
            case "changPwd" -> changPwd(messageBean, userBean, server);
            default -> null;
        };
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
            //向用户管理器添加数据
            server.getUsersManager().addUser(userBean);
        }else {
            data[0] = "400";
            data[1] = "failed login";
        }
        MessageBean bean = MessageUtil.replyMessage(messageBean.getID(), "login", data, server);
        //构建返回数据
        sqlSession.close();
        return bean;
    }

    private static MessageBean logon(MessageBean messageBean,UserBean userBean,Server server){
        SqlSession sqlSession = SqlUtil.getSqlSession();
        //查询数据
        UserBean temp = sqlSession.selectOne("user.check", userBean);
        String[] data = new String[2];

        if (temp != null){
            data[0] = "400";
            data[1] = "账户已存在！";
        }else {
            System.out.println(JSON.toJSONString(userBean));
            sqlSession.insert("user.logon",userBean);
            data[0] = "200";
            data[1] = "注册成功";
            sqlSession.commit();
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
        }else {
            userBean.setPassword(messageBean.getData().get("pwdNew"));
            sqlSession.insert("user.changPwd",userBean);
            data[0] = "200";
            data[1] = "密码修改成功!";
            sqlSession.commit();
        }
        sqlSession.close();
        return MessageUtil.replyMessage(messageBean.getID(),"logon",data,server);
    }
}
