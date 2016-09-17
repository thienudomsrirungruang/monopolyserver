package com.thien.monopolyserver;

import java.util.Random;

public class CommunityChest extends Square{
    Random random;
    Board board;
    public CommunityChest(int position, Random random, Board board) {
        super(position, "Community Chest", board);
        this.random = random;
        this.board = board;
    }
    public void action(Player player) throws InterruptedException {
        int card = random.nextInt(15);
        switch(card){
            case 0:
                showCard("Advance to Go.", player);
                player.goTo(0, true);
                board.getSquares().get(player.getPosition()).action(player);
                break;
            case 1:
                showCard("Bank error in your favour, collect 2000", player);
                player.addMoney(2000, false);
                break;
            case 2:
                showCard("Doctor's fees, pay 500", player);
                player.addMoney(-500, true);
                break;
            case 3:
                showCard("From sale of stock you get 500", player);
                player.addMoney(500, false);
                break;
            case 4:
                showCard("Go to jail: Do not pass Go, do not collect 2000", player);
                player.goTo(10, false);
                player.setJailed(true);
                break;
            case 5:
                showCard("Grand opera night, collect 500 from each player for seats", player);
                for(Player otherPlayer : board.getPlayers()){
                    if(!otherPlayer.equals(player)){
                        player.addMoney(500, false);
                        otherPlayer.addMoney(-500, true);
                    }
                }
                break;
            case 6:
                showCard("Holiday fund matures, receive 1000", player);
                player.addMoney(1000, false);
                break;
            case 7:
                showCard("Income tax refund, collect 200", player);
                player.addMoney(200, false);
                break;
            case 8:
                showCard("It's your birthday, collect 100 from everyone!", player);
                for(Player otherPlayer : board.getPlayers()){
                    if(!otherPlayer.equals(player)){
                        player.addMoney(100, false);
                        otherPlayer.addMoney(-100, true);
                    }
                }
                break;
            case 9:
                showCard("Life insurance matures, collect 1000", player);
                player.addMoney(1000, false);
                break;
            case 10:
                showCard("Pay hospital fees of 1000", player);
                player.addMoney(-1000, true);
                break;
            case 11:
                showCard("Pay school fees of 1500", player);
                player.addMoney(-1500, true);
                break;
            case 12:
                showCard("Receive 250 consultancy fee", player);
                player.addMoney(250, false);
                break;
            case 13:
                showCard("You have won second prize in a beauty contest, collect 100", player);
                player.addMoney(100, false);
                break;
            case 14:
                showCard("You inherit 1000", player);
                player.addMoney(1000, false);
                break;
        }
    }

    public void printCard(Player player) throws InterruptedException {
        player.getClientInfo().sendCommandToClient("Print~" + getPosition() + "\t Community Chest\t Special\t Test your luck here!\t On this square: " + getEveryoneOnSquare() + "\r\n", 2);
    }

    public void showCard(String message, Player player) throws InterruptedException {
        StringBuilder print = new StringBuilder("Print~");
        for(int i = 0; i < message.length(); i++){
            print.append("-");
        }
        print.append("\r\n" + message + "\r\n");
        for(int i = 0; i < message.length(); i++){
            print.append("-");
        }
        print.append("\r\n");
        player.getClientInfo().sendCommandToClient(print.toString(), 4);
    }
}