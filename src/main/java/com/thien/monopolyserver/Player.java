package com.thien.monopolyserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Player {
    private int position;
    private String name;
    private int money;
    private boolean jailed;
    private Board board;
    private int jailBreakAttempts;
    private ArrayList<BuyableSquare> ownedProperties;
    private ClientInfo clientInfo;
    public Player(String name, Board board, ClientInfo clientInfo) {
        position = 0;
        this.name = name;
        money = 0;
        jailed = false;
        this.board = board;
        jailBreakAttempts = 0;
        ownedProperties = new ArrayList<BuyableSquare>();
        this.clientInfo = clientInfo;
    }

    public void doTurn(Random random) throws IOException, InterruptedException {
        clientInfo.sendCommandToClient("Print~\r\n\r\n---------------------------------------------\r\n\r\n" + name + "'s turn" + "\r\nMoney: " + money + "\r\n\r\n", 8);
        if (jailed) {
            if (jailBreakAttempts <= 1 && money >= 500) {
                String answer = clientInfo.sendRequestToClient("MakeChoice~true~Do you want to pay 500 to get out of jail? (yes/no): ~yes~no", 1);
                if (answer.equalsIgnoreCase("yes")) {
                    addMoney(-500, true);
                    jailBreakAttempts = 0;
                    setJailed(false);
                    normalTurn(0, random);
                } else {
                    if (tryBreakingOutOfJail(random)) {
                        normalTurn(0, random);
                    } else if (jailBreakAttempts == 2) {
                        clientInfo.sendCommandToClient("Print~You can't stay in jail any longer.\r\n", 2);
                        addMoney(-500, true);
                        normalTurn(0, random);
                    } else {
                        jailBreakAttempts++;
                    }
                }
            } else {
                if (tryBreakingOutOfJail(random)) {
                    normalTurn(0, random);
                } else if (jailBreakAttempts == 2) {
                    clientInfo.sendCommandToClient("Print~You can't stay in jail any longer.\r\n", 2);
                    addMoney(-500, true);
                    normalTurn(0, random);
                } else {
                    jailBreakAttempts++;
                }
            }
        } else {
            normalTurn(0, random);
        }
        String answer = "";
        while(!answer.equalsIgnoreCase("no")){
            answer = clientInfo.sendRequestToClient("MakeChoice~true~Do you want to do anything different? (yes/no)~yes~no", 1);
            if(answer.equalsIgnoreCase("yes")){
                String choice = getClientInfo().sendRequestToClient("MakeChoice~true~What do you want to do?\r\n\"House\"\tto buy a house\r\n\"Board\"\tto view the board\r\n\"Info\"\tto view detailed information for a square\r\n\"Cancel\"\tto cancel", 5);
                if(choice.equalsIgnoreCase("house")){
                    clientInfo.sendCommandToClient("Print~Which property do you want to build on?\r\n", 2);
                    int i = 0;
                    ArrayList<Property> list = new ArrayList<Property>();
                    for(BuyableSquare bs : ownedProperties){
                        if(bs.getClass().equals(Property.class)){
                            clientInfo.sendCommandToClient("\"" + i + "\"" + " for " + bs.getName() + "\r\n", 2);
                            list.add((Property) bs);
                            i++;
                        }
                    }
                    clientInfo.sendCommandToClient("Print~Type anything else to cancel\r\n", 2);
                    try{
                        list.get(Integer.parseInt(clientInfo.sendRequestToClient("GetInput", 1))).addAHouse();
                    }catch(Exception e){}
                }
                else if(choice.equalsIgnoreCase("board")){
                    board.printBoard(this);
                }
                else if(choice.equalsIgnoreCase("info")){
                    board.printBoard(this);
                    clientInfo.sendCommandToClient("Print~Please give the square number (0-39) to view the square's stats, type anything else to cancel.\r\n", 2);
                    try{
                        int selected = Integer.parseInt(getClientInfo().sendRequestToClient("GetInput", 1));
                        if(board.getSquares().get(selected).getClass().equals(Property.class)
                                || board.getSquares().get(selected).getClass().equals(Utility.class)
                                || board.getSquares().get(selected).getClass().equals(Railroad.class)){
                            BuyableSquare bs = (BuyableSquare) board.getSquares().get(selected);
                            bs.printRentDetails(this);
                        }else{
                            clientInfo.sendCommandToClient("Print~There is no detailed information for this square.\r\n", 2);
                        }
                    }catch(Exception e){}
                }
            }
        }
    }

    public boolean tryBreakingOutOfJail(Random random) throws InterruptedException {
        clientInfo.sendCommandToClient("Print~You try to roll a double to get out of jail.\r\n", 2);
        if(diceRoll(random, true)){
            clientInfo.sendCommandToClient("Print~You break out of jail.\r\n", 2);
            return true;
        }else{
            return false;
        }
    }
    public void normalTurn(int doubles, Random random) throws InterruptedException {
        boolean doubled = false;
        if(diceRoll(random, true)){
            doubles++;
            doubled = true;
            if(doubles == 4){
                clientInfo.sendCommandToClient("Print~You rolled doubles 3 times and got sent to jail!\r\n", 2);
                jailed = true;
                goTo(10, false);
            }else{
                board.getSquares().get(position).action(this);
                if(doubled){
                    normalTurn(doubles + 1, random);
                }
            }
        }else{
            board.getSquares().get(position).action(this);
            if(doubled){
                normalTurn(doubles + 1, random);
            }
        }
    }
    public boolean diceRoll(Random random, boolean move) throws InterruptedException {
        int[] diceRolls = new int[2];
        diceRolls[0] = random.nextInt(6) + 1;
        diceRolls[1] = random.nextInt(6) + 1;
        clientInfo.sendCommandToClient("Print~You roll " + diceRolls[0] + " and " + diceRolls[1] + ", with a total of " + (diceRolls[0] + diceRolls[1]), 2);
        if(move){
            move(diceRolls[0] + diceRolls[1]);
        }
        if(diceRolls[0] == diceRolls[1]){
            clientInfo.sendCommandToClient("Print~It's a double!\r\n", 2);
            return true;
        }else{
            return false;
        }
    }
    public int move(int steps) throws InterruptedException {
        if (position + steps >= 40) {
            clientInfo.sendCommandToClient("Print~You passed Go and got 2000!\r\n", 2);
            addMoney(2000, false);
        }
        position = (position + steps) % 40;
        clientInfo.sendCommandToClient("Print~You land at " + board.getSquares().get(position).getName() + ". (Position is " + position + ")\r\n", 2);
        return position;
    }
    public void goTo(int pos, boolean checkPassGo) throws InterruptedException {
        if (checkPassGo && position > pos) {
            addMoney(2000, false);
            clientInfo.sendCommandToClient("Print~You passed Go and got 2000!\r\n", 2);
        }
        position = pos;
        clientInfo.sendCommandToClient("Print~You go to " + board.getSquares().get(position).getName() + ". (Position is " + position + ")\r\n", 2);
    }
    public String getName() {
        return name;
    }
    public int getPosition() {
        return position;
    }
    public int getMoney() {
        return money;
    }
    public void setMoney(int money) {
        this.money = money;
    }
    public void addMoney(int money, boolean checkBankrupt) throws InterruptedException {
        setMoney(this.money + money);
        if(money > 0){
            clientInfo.sendCommandToClient("Print~" + money + " has been added to " + name + "'s account.\r\n", 2);
        }else if(money < 0){
            clientInfo.sendCommandToClient("Print~" + (-1 * money) + " has been taken from " + name + "'s account.\r\n", 2);
        }
        if(checkBankrupt){
            if(this.money < 0){
                clientInfo.sendCommandToClient("Print~You have gone bankrupt!\r\n", 2);
                board.getPlayers().remove(this);
            }
        }
    }
    public boolean checkCompletePropertySet(int set){
        int[] propertiesInEachSet = {2, 3, 3, 3, 3, 3, 3, 2};
        int ownedPropertiesInSet = 0;
        for(BuyableSquare property : ownedProperties){
            if(property.getClass().equals(Property.class)){
                Property realProperty = (Property) property;
                if(realProperty.getPropertySet() == set){
                    ownedPropertiesInSet++;
                }
            }
        }
        return ownedPropertiesInSet >= propertiesInEachSet[set];
    }
    public int getLeastNumberOfHousesInSet(int set){
        if(!checkCompletePropertySet(set)){
            throw(new IllegalStateException(name + " doesn't own all properties in set " + set));
        }
        int minHouses = 10000000;
        for(BuyableSquare property : ownedProperties){
            if(property.getClass().equals(Property.class)){
                Property realProperty = (Property) property;
                if(realProperty.getPropertySet() == set){
                    if(realProperty.getHouses() < minHouses){
                        minHouses = realProperty.getHouses();
                    }
                }
            }
        }
        return minHouses;
    }
    public void setJailed(boolean isJailed) {
        this.jailed = isJailed;
    }
    public boolean getJailed() {
        return jailed;
    }
    public void addProperty(BuyableSquare square){
        ownedProperties.add(square);
    }
    public ArrayList<BuyableSquare> getOwnedProperties(){
        return ownedProperties;
    }
    public ClientInfo getClientInfo() {
        return clientInfo;
    }
}