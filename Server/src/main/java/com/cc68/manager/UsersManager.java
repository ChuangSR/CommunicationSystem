package com.cc68.manager;

import com.cc68.beans.UserBean;

import java.util.ArrayList;

/**
 * 用于统一管理登录的用户
 */
public class UsersManager {
    private final ArrayList<UserBean> beans = new ArrayList<>(100);

    private int online = 0;



    public void addUser(UserBean bean){
        beans.add(bean);
        online++;
    }

    public UserBean getUser(String account){
        UserBean reply = null;
        for (UserBean bean:beans){
            if (bean.getAccount().equals(account)){
                reply = bean;
                break;
            }
        }
        return reply;
    }

    public int getOnline(){
        return online;
    }

}
