package com.thien.monopolyserver;

import java.io.IOException;
import java.net.ServerSocket;

public class MonopolyServer {
    private final int PORT = 41875;
    private static MonopolyServer instance = new MonopolyServer();
    public static void main(String[] args) throws IOException {
        ServerSocket port = new ServerSocket(instance.getPort());
        ServerDispatcher sd = new ServerDispatcher(new GameStarter(), port);
        sd.start();
    }
    public int getPort(){
        return PORT;
    }
}