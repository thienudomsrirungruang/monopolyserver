package com.thien.monopolyserver;

import java.net.Socket;

public class ClientInfo {
    private Socket socket;
    private ClientListener cListener;
    private ClientSender cSender;
    private Player player;
    private ServerDispatcher sd;
    public ClientInfo(ServerDispatcher sd){
        this.sd = sd;
    }
    public void initPlayer(Board board){
        try {
            setPlayer(new Player(sendRequestToClient("GetInput~Enter name: ", 1), board, this));
        }catch(InterruptedException e){
            System.out.println("Interrupted");
        }
    }
    public String getNextMessage() throws InterruptedException{
        while(true){
            Thread.sleep(50);
            if(cListener.getMessages().size() > 0){
                break;
            }
        }
        String output = cListener.getMessages().get(cListener.getMessages().size() - 1);
        cListener.getMessages().remove(cListener.getMessages().size() - 1);
        return output;
    }
    public Socket getSocket() {
        return socket;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    public void setClientListener(ClientListener cListener) {
        this.cListener = cListener;
    }
    public ClientSender getClientSender() {
        return cSender;
    }
    public void setClientSender(ClientSender cSender) {
        this.cSender = cSender;
    }
    public Player getPlayer(){
        return player;
    }
    public void setPlayer(Player player){
        this.player = player;
    }
    public synchronized String sendRequestToClient(String request, int lines) throws InterruptedException{
        cSender.sendMessage(lines + "\r\nRequest~" + request);
        return getNextMessage();
    }
    public synchronized void sendCommandToClient(String command, int lines) throws InterruptedException{
        for(ClientInfo ci : sd.getPlayerList()) {
            ci.getClientSender().sendMessage(lines + "\r\nCommand~" + command);
        }
    }
}