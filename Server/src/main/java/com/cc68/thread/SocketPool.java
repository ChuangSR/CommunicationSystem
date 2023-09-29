package com.cc68.thread;

import com.cc68.Server;
import com.cc68.beans.MessageBean;
import com.cc68.beans.UserBean;
import com.cc68.manager.SendManager;
import com.cc68.utils.MessageUtil;

import java.io.*;
import java.util.ArrayList;

public class SocketPool implements Runnable{
    private Server server;

    private int MAX;
    private ArrayList<SocketThread> pool;

    private int timeout;

    private boolean flag = true;

    public SocketPool(){}

    public SocketPool(Server server,int MAX,int timeout){
        this.server = server;
        this.MAX = MAX;
        pool = new ArrayList<>(MAX);
        this.timeout = timeout;
    }

    /**
     * 后台运行的线程，为了查看超时的线程
     */
    @Override
    public void run() {
        while (flag){
            try {
                Thread.sleep(1000);
                checkThread();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 添加一个线程，如果在以满的情况下会关闭一个线程
     * @param socketThread 被添加的线程
     * @throws IOException
     */

    public void add(SocketThread socketThread) throws IOException {
        System.out.println("run");
        Thread thread = new Thread(socketThread);
        thread.start();
        if (pool.size() == MAX){
            long MAX = 0;
            int index = 0;
            for (int i = 0;i < pool.size();i++){
                SocketThread temp = pool.get(i);
                long linkTime = temp.getLinkTime();
                long time = System.currentTimeMillis()/1000 - linkTime;
                if (time > timeout){
                    closeThread(temp);
                    pool.remove(temp);
                    pool.add(socketThread);
                    return;
                }else {
                    MAX = Math.max(MAX,time);
                    index = i;
                }
            }
            closeThread(pool.get(index));
            pool.remove(index);
            pool.add(socketThread);
        }else {
            pool.add(socketThread);
        }
    }

    public void delete(SocketThread thread){
        pool.remove(thread);
    }

    public SocketThread getThread(UserBean bean){
        for (SocketThread thread:pool){
            if (bean.getAccount().equals(thread.getUserBean().getAccount())){
                return thread;
            }
        }
        return null;
    }

    /**
     * 循环查找要被关闭的线程
     * @throws IOException
     */
    private void checkThread() throws IOException {
        for (SocketThread thread:pool){
            long linkTime = thread.getLinkTime();
            long now = System.currentTimeMillis()/1000;
            if (now - linkTime >= timeout){
                closeThread(thread);
            }
        }
    }

    /**
     * 负责关闭一个线程
     * @param thread 被关闭的线程
     * @throws IOException
     */
    private void closeThread(SocketThread thread) throws IOException {
        //获取bean对象
        UserBean userBean = thread.getUserBean();

        if (userBean == null){
            thread.close();
            delete(thread);
            return;
        }

        String[] data = new String[1];
        data[0] = MessageUtil.getTime();
        //构造消息
        MessageBean bean = MessageUtil.buildMessage("offline",data, server.getConfig().getProperty("account"));

        //发送消息
        SendManager sendManager = userBean.getSendManager();
        sendManager.send(bean);

        //设置线程状态
        userBean.setThreadStatus(false);

        //关闭对象
        thread.close();
        userBean.close();
    }

}
