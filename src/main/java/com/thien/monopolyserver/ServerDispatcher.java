package com.thien.monopolyserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerDispatcher extends Thread{
    private ArrayList<ClientInfo> playerList = new ArrayList<ClientInfo>();
    private GameStarter gs;
    private ServerSocket port;
    private boolean open = true;
    private PlayerFinder pf;
    public ServerDispatcher(GameStarter gs, ServerSocket port){
        this.gs = gs;
        this.port = port;
    }

    public void run(){
        Scanner input = new Scanner(System.in);
        System.out.print("How many players? ");
        int gameSize = input.nextInt();
        System.out.println("Dispatcher started");
        pf = new PlayerFinder(this, port);
        pf.start();
        while(playerList.size() < gameSize) {
            try{
                Thread.sleep(10);
            }catch (InterruptedException e){
                System.out.print("Interrupted");
            }
        }
        System.out.println("Closed");
        open = false;
        try {
            gs.startGame(this);
        }catch(IOException e){
            System.out.println("Lost connection");
        }catch(InterruptedException e){
            System.out.println("Interrupted");
        }
    }

    public void addClient(ClientInfo clientInfo){
        playerList.add(clientInfo);
    }
    public ArrayList<ClientInfo> getPlayerList(){
        return playerList;
    }
    public boolean shouldBeOpen(){
        return open;
    }
}