package com.cc68.thread;

import com.cc68.Server;
import com.cc68.beans.UserBean;

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
    @Override
    public void run() {
        while (flag){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void add(SocketThread thread){
        if (pool.size() == MAX){

        }else {
            pool.add(thread);
        }
    }

    private void checkThread(){
        for (SocketThread thread:pool){
            long linkTime = thread.getLinkTime();
            long now = System.currentTimeMillis()/1000;
            if (now - linkTime >= timeout){

            }
        }
    }

    public void serialize(SocketThread thread) throws IOException {
        UserBean userBean = thread.getUserBean();
        StringBuilder fileName = new StringBuilder();
        fileName.append("./ThreadCache/").append(userBean.getAccount()).append(".").append("st");
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileName.toString()));
        os.writeObject(thread);
        os.flush();
        os.close();
        pool.remove(thread);
    }

//    public void deserialize(){
//        new ObjectInputStream(new FileInputStream());
//    }
}
