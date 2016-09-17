package com.thien.monopolyserver;
public class FreeParking extends Square{
    public FreeParking(Board board){
        super(20, "Free Parking", board);
    }
    public void action(Player player) throws InterruptedException {
        player.getClientInfo().sendCommandToClient("Print~Nothing happens.\r\n", 2);
    }
    public void printCard(Player player) throws InterruptedException {
        player.getClientInfo().sendCommandToClient("Print~20\t Free Parking\t Special\t Nothing happens here!\t On this square: " + getEveryoneOnSquare() + "\r\n", 2);
    }
}