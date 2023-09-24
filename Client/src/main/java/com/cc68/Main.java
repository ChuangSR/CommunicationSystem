package com.cc68;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static Client client;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
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

            }else if (choose == 4){
                exit();
            }else {
                inputError();
            }
        }
    }

    public static void login() throws IOException {
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

    public static void changePwd(){
        System.out.println("==================================");
        System.out.print("请输入账号：");
        String account = scanner.next();
        System.out.print("请输入原密码：");
        String pwdOld = scanner.next();
        System.out.print("请输入新密码：");
        String pwdNew = scanner.next();
    }

    public static void menu(){

    }

    public static void inputError() throws IOException {
        System.out.println("输入异常");
    }

    public static void exit(){
        System.exit(0);
    }
}
