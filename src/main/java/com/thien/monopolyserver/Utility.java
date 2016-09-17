package com.thien.monopolyserver;

import java.util.Random;

public class Utility extends BuyableSquare{
    public Utility(int position, String name, Board board){
        super(position, name, board);
        setSellPrice(1500);
    }
    public void action(Player player) throws InterruptedException {
        if(getOwner() == null){
            String answer = "view";
            while(answer.equalsIgnoreCase("view")) {
                answer = player.getClientInfo().sendRequestToClient("MakeChoice~true~Do you want to buy this utility for " + getSellPrice() + " ? (\"yes\" to buy, \"no\" to leave, \"view\" to view more) : ~yes~no~view", 1);
                if(answer.equalsIgnoreCase("view")){
                    printRentDetails(player);
                }
            }
            if(answer.equalsIgnoreCase("yes")){
                if(player.getMoney() >= getSellPrice()){
                    player.getClientInfo().sendCommandToClient("Print~Utility sold.\r\n", 2);
                    buyProperty(player);
                }else{
                    player.getClientInfo().sendCommandToClient("Print~You don't have enough money.\r\n", 2);
                }
            }
        }else{
            if(player == getOwner()){
                player.getClientInfo().sendCommandToClient("Print~You land on your own property, nothing happens.\r\n", 2);
            }else{
                chargeRent(player);
            }
        }
    }

    public void printCard(Player player) throws InterruptedException {
        player.getClientInfo().sendCommandToClient("Print~" + getPosition() + "\t " + getName() + "\t Utility\t Land you can charge rent on.\t ", 1);
        if(getOwner() == null){
            player.getClientInfo().sendCommandToClient("Print~For sale at " + getSellPrice(), 1);
        }else{
            player.getClientInfo().sendCommandToClient("Print~Owner: " + getOwner().getName(), 1);
        }
        player.getClientInfo().sendCommandToClient("Print~" + "\t On this square: ", 1);
        player.getClientInfo().sendCommandToClient("Print~" + getEveryoneOnSquare() + "\r\n", 2);
    }

    public void chargeRent(Player otherPlayer) throws InterruptedException {
        Random random = new Random();
        int diceRoll = random.nextInt(6) + random.nextInt(6) + 2;
        getOwner().getClientInfo().sendCommandToClient("Print~The dice rolls " + diceRoll + "\r\n", 2);
        if(getOwner().getOwnedProperties().contains(getBoard().getSquares().get(12))
                && getOwner().getOwnedProperties().contains(getBoard().getSquares().get(28))){
            getOwner().getClientInfo().sendCommandToClient("Print~You pay " + getOwner().getName() + " " + (diceRoll * 100) + ".\r\n", 2);
            getOwner().addMoney(diceRoll * 100, false);
            otherPlayer.addMoney(diceRoll * -100, true);
        }else{
            getOwner().getClientInfo().sendCommandToClient("Print~You pay " + getOwner().getName() + " " + (diceRoll * 40) + ".\r\n", 2);
            getOwner().addMoney(diceRoll * 40, false);
            otherPlayer.addMoney(diceRoll * -40, true);
        }
    }
    public void printRentDetails(Player player) throws InterruptedException {
        player.getClientInfo().sendCommandToClient("Print~Name: " + getName() + "\r\nPosition: " + getPosition() + "\r\n----------RENT----------\r\n1 Utility owned: 40 times the amount thrown on dice\r\n2 Utilities owned: 100 times the amount thrown on dice\r\n", 6);
    }
}