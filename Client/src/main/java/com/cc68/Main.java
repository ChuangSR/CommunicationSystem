package com.cc68;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static Client client;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException, InterruptedException {
        //关闭dos回显
        client = new Client();

        while (true){
            System.out.println("===============菜单================");
            System.out.println("=           1  登录               =");
            System.out.println("=           2  注册               =");
            System.out.println("=           3  修改密码            =");
            System.out.println("=           4  退出               =");
            System.out.println("==================================");
            System.out.print("请输入:");
            int choose = scanner.nextInt();
            if (choose == 1){
                login();
            }else if (choose ==2){
                logon();
            }else if (choose == 3){
                changePwd();
            }else if (choose == 4){
                exit();
            }else {
                inputError();
            }
        }
    }

    public static void login() throws IOException, InterruptedException {
        System.out.println("==================================");
        System.out.print("请输入账号：");
        String account = scanner.next();
        System.out.print("请输入密码：");
        String password = scanner.next();
        boolean status = client.login(account, password);
        if (status){
            menu();
        }
        return;
    }

    public static void logon() throws IOException {
        System.out.println("==================================");
        System.out.print("请输入账号：");
        String account = scanner.next();
        System.out.print("请输入密码：");
        String password = scanner.next();
        client.logon(account,password);
    }

    public static void changePwd() throws IOException {
        System.out.println("==================================");
        System.out.print("请输入账号：");
        String account = scanner.next();
        System.out.print("请输入原密码：");
        String password = scanner.next();
        System.out.print("请输入新密码：");
        String pwdNew = scanner.next();
        client.changPwd(account,password,pwdNew);
    }

    public static void menu() throws IOException, InterruptedException {
        System.out.println("==================================");
        System.out.println("=           1  查看在线用户        =");
        System.out.println("=           3  私聊               =");
        System.out.println("=           3  群聊               =");
        System.out.println("=           4  退出               =");
        System.out.println("==================================");
       while (true){
           int choose = scanner.nextInt();
           switch (choose){
               case 1:
                   client.list();
                   break;
               case 2:
                   inputError();
                   break;
               case 3:
                   inputError();
                   break;
               case 4:
                   client.close();
                   return;
           }
       }

    }

    public static void inputError(){
        System.out.println("输入异常");
    }

    public static void exit(){
        System.exit(0);
    }
}
