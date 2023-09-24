package com.cc68;

import com.alibaba.fastjson2.JSON;
import com.cc68.beans.MessageBean;
import com.cc68.beans.UserBean;
import com.cc68.utils.MessageUtil;
import com.cc68.utils.SqlUtil;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;

/**
 * 用于处理消息
 */
public class HandleMessage {
    public static MessageBean handle(MessageBean messageBean, UserBean userBean,Server server){
        MessageBean reply = null;
        switch (messageBean.getType()){
            case "login":
                reply = login(messageBean,userBean,server);
                break;
        }
        return reply;
    }

    private static MessageBean login(MessageBean messageBean,UserBean userBean,Server server){
        //打开数据库连接
        SqlSession sqlSession = SqlUtil.getSqlSession();

        //查询数据
        Cursor<Object> cursor = sqlSession.selectCursor("user.login", userBean);
        String[] status = new String[1];

        //构建返回数据

        if (cursor != null){
            status[0] = "successful login";
            //向用户管理器添加数据
            server.getUsersManager().addUser(userBean);
        }else {
            status[0] = "failed login";
            //关闭bean对象
            try {
                userBean.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        MessageBean reply = MessageUtil.replyMessage(messageBean.getID(),"login",status,server);

        return reply;
    }
}
