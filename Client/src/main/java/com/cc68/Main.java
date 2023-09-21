package com.cc68;

import com.cc68.utils.DosUtil;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static Client client;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        //关闭dos回显
//        DosUtil.exec("@echo off");
        client = new Client();

        while (true){
            System.out.println("===============菜单================");
            System.out.println("=           1  登录               =");
            System.out.println("=           2  退出               =");
            System.out.println("==================================");
            System.out.println("请输入");
            int choose = scanner.nextInt();
            if (choose == 1){
                login();
            }else if (choose ==2){
                exit();
            }else {
                inputError();
            }
        }
    }

    public static void login(){
        System.out.println("==================================");
        System.out.print("请输入账号：");
        String account = scanner.next();
        System.out.print("请输入密码：");
        String password = scanner.next();
    }

    public static void inputError() throws IOException {
//        String[] commands = {"cls","color red","echo 输入异常"};
//        DosUtil.execs(commands);
        System.out.println("输入异常");
    }

    public static void exit(){
        System.exit(0);
    }
}
