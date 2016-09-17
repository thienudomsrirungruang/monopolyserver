package com.thien.monopolyserver;
public class Tax extends Square{
    int taxAmount;
    public Tax(int position, String name, Board board, int taxAmount){
        super(position, name, board);
        this.taxAmount = taxAmount;
    }
    public void action(Player player) throws InterruptedException {
        player.addMoney(-1 * taxAmount, true);
    }

    public void printCard(Player player) throws InterruptedException {
        player.getClientInfo().sendCommandToClient(getPosition() + "\t " + getName() + "\t Tax\t Pay " + taxAmount + " if you land here!\t On this square: " + getEveryoneOnSquare() + "\r\n", 2);
    }
}