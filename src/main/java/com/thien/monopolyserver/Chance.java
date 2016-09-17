package com.thien.monopolyserver;

import java.util.Random;

public class Chance extends Square{
    Random random;
    Board board;
    public Chance(int position, Random random, Board board){
        super(position, "Chance", board);
        this.random = random;
        this.board = board;
    }
    public void action(Player player) throws InterruptedException {
        int card = random.nextInt(14);
        switch(card){
            case 0:
                showCard("Advance to Go.", player);
                player.goTo(0, true);
                board.getSquares().get(player.getPosition()).action(player);
                break;
            case 1:
                showCard("Advance to Trafalgar Square.", player);
                player.goTo(24, true);
                board.getSquares().get(player.getPosition()).action(player);
                break;
            case 2:
                showCard("Advance to Pall Mall.", player);
                player.goTo(11, true);
                board.getSquares().get(player.getPosition()).action(player);
                break;
            case 3:
                showCard("Advance to the next Utility.", player);
                if(player.getPosition() >= 12 && player.getPosition() <= 28){
                    player.goTo(28, true);
                }else{
                    player.goTo(12, true);
                }
                board.getSquares().get(player.getPosition()).action(player);
                break;
            case 4:
                showCard("Advance to the next Railroad.", player);
                player.goTo((((player.getPosition() / 10) * 10) + 5) % 40, true);
                board.getSquares().get(player.getPosition()).action(player);
                break;
            case 5:
                showCard("Bank pays you dividend of 500", player);
                player.addMoney(500, false);
                break;
            case 6:
                showCard("Go back 3 spaces", player);
                player.goTo(player.getPosition() - 3, false);
                board.getSquares().get(player.getPosition()).action(player);
                break;
            case 7:
                showCard("Go to jail: Do not pass Go, do not collect 2000", player);
                player.goTo(10, false);
                player.setJailed(true);
                break;
            case 8:
                showCard("Pay poor tax of 150", player);
                player.addMoney(-150, true);
                break;
            case 9:
                showCard("Take a trip to King's Cross Station - If you pass go, collect 2000", player);
                player.goTo(5, true);
                board.getSquares().get(player.getPosition()).action(player);
                break;
            case 10:
                showCard("Take a walk on Mayfair - Advance to Mayfair", player);
                player.goTo(39, true);
                board.getSquares().get(player.getPosition()).action(player);
                break;
            case 11:
                showCard("You have been elected Chairman of the Board - pay each player 500", player);
                for(Player otherPlayer : board.getPlayers()){
                    if(!otherPlayer.equals(player)){
                        player.addMoney(-500, true);
                        otherPlayer.addMoney(500, false);
                    }
                }
                break;
            case 12:
                showCard("Your building loan matures, collect 1500", player);
                player.addMoney(1500, false);
                break;
            case 13:
                showCard("You have won a crossword competition, collect 1000", player);
                player.addMoney(1000, false);
                break;
        }
    }

    public void printCard(Player player) throws InterruptedException {
        player.getClientInfo().sendCommandToClient("Print~" + getPosition() + "\t Chance\t Special\t Test your luck here!\t On this square: " + getEveryoneOnSquare() + "\r\n", 2);
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