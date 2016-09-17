package com.thien.monopolyserver;
public class Go extends Square{
    public Go(Board board){
        super(0, "Go", board);
    }
    public void action(Player player) throws InterruptedException {
        player.getClientInfo().sendCommandToClient("Print~You land on Go, nothing happens except that you get 2000.\r\n", 2);
    }
    public void printCard(Player player) throws InterruptedException {
        player.getClientInfo().sendCommandToClient("Print~0\t Go\t Special\t Collect 2000 when you pass this square!\t On this square: " + getEveryoneOnSquare() + "\r\n", 2);
    }
}