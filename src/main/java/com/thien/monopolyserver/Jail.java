package com.thien.monopolyserver;
public class Jail extends Square{
    public Jail(Board board){
        super(10, "Jail", board);
    }
    public void action(Player player) throws InterruptedException {
        if(player.getJailed()){
            player.getClientInfo().sendCommandToClient("Print~You are in jail.\r\n", 2);
        }else{
            player.getClientInfo().sendCommandToClient("Print~You are just visiting jail.\r\n", 2);
        }
    }

    public void printCard(Player player) throws InterruptedException {
        player.getClientInfo().sendCommandToClient("Print~10\t Jail\t Special\t People are jailed here!\t On this square: " + getEveryoneOnSquare() + "\r\n", 2);
    }
}