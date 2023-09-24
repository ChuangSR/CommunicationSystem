package com.cc68.beans;

public class UserBean {
    private String account;
    private String passwowrd;

    public UserBean(){}

    public UserBean(String account, String passwowrd) {
        this.account = account;
        this.passwowrd = passwowrd;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPasswowrd() {
        return passwowrd;
    }

    public void setPasswowrd(String passwowrd) {
        this.passwowrd = passwowrd;
    }
}
