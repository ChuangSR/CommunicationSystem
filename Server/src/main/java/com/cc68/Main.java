package com.cc68;

import java.io.IOException;

public class Main {
//    private static ServerSocket server;
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }
}
