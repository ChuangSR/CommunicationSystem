package com.cc68.utils;

import java.io.IOException;

/**
 * 这个一个用于执行dos命令的类
 */
public class DosUtil {
    /**
     * 执行一个dos命令
     * @param command dos命令
     * @throws IOException
     */
    public static void exec(String command) throws IOException {
        Process process = Runtime.getRuntime().exec(command);
    }

    /**
     * 执行多个都是命令
     * @param commands  多个dos命令
     * @throws IOException
     */
    public static void execs(String[] commands) throws IOException {
        for (String command : commands){
            exec(command);
        }
    }
}
