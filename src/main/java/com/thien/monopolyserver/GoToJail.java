package com.thien.monopolyserver;
public class GoToJail extends Square{
    public GoToJail(Board board){
        super(30, "Go To Jail", board);
    }
    public void action(Player player) throws InterruptedException {
        player.getClientInfo().sendCommandToClient("Print~You go to jail!\r\n", 2);
        player.setJailed(true);
        player.goTo(10, false);
    }

    public void printCard(Player player) throws InterruptedException {
        player.getClientInfo().sendCommandToClient("Print~30\t Go To Jail\t Special\t You will be sent to jail here!\r\n", 2);
    }
}