package com.thien.monopolyserver;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class PlayerFinder extends Thread{
    private ServerDispatcher sd;
    private ServerSocket port;
    public PlayerFinder(ServerDispatcher sd, ServerSocket port){
        this.sd = sd;
        this.port = port;
    }
    public void run(){
        try{
            System.out.println("Now finding players...");
            while(sd.shouldBeOpen()){
                Socket socket = port.accept();
                System.out.print("Someone connected! Port: ");
                System.out.println(socket.getInetAddress());
                ClientInfo cInfo = new ClientInfo(sd);
                cInfo.setSocket(socket);
                ClientListener cListener = new ClientListener(cInfo, sd);
                ClientSender cSender = new ClientSender(cInfo, sd, new PrintWriter(new OutputStreamWriter(cInfo.getSocket().getOutputStream())));
                cInfo.setClientListener(cListener);
                cInfo.setClientSender(cSender);
                sd.addClient(cInfo);
                cListener.start();
                cSender.start();
                Thread.sleep(200);
            }
        }catch(IOException e){
            System.out.println("Lost connection");
        }catch(InterruptedException e){
            System.out.println("Interrupted");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}