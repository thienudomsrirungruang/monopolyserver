package com.thien.monopolyserver;

import java.util.Random;

public class Property extends BuyableSquare{
    private int[] rent;
    private int houses;
    private int housePrice;
    private int propertySet;
    public Property(int position, String name, Board board, int propertySet){
        super(position, name, board);
        Random random = new Random();
        setSellPrice((int) random.nextDouble() * position * 60 + 90 * position + 500);
        houses = 0;
        rent = new int[6];
        rent[0] = getSellPrice() / (int) (5 + random.nextDouble());
        rent[1] = (int) (rent[0] * (5 + (random.nextDouble() / 2)));
        rent[2] = (int) (rent[1] * (3 + (random.nextDouble() / 5)));
        rent[3] = (int) (rent[2] * (2.3 + random.nextDouble() / 5));
        rent[4] = (int) (rent[3] * (1.2 + (random.nextDouble() / 20)));
        rent[5] = (int) (rent[4] * (1.17 + (3 * random.nextDouble() / 50)));
        housePrice = ((position / 10) + 1) * 500;
        this.propertySet = propertySet;
    }
    public void addAHouse() throws InterruptedException{
        if(houses < 5){
            if(getOwner().getMoney() >= housePrice){
                if(getOwner().checkCompletePropertySet(propertySet)){
                    if(getOwner().getLeastNumberOfHousesInSet(propertySet) + 1 > houses){
                        getOwner().getClientInfo().sendCommandToClient("Print~You buy a house in " + getName() + " for " + housePrice + ".\r\n", 2);
                        getOwner().addMoney(-1 * housePrice, false);
                        houses++;
                    }else{
                        getOwner().getClientInfo().sendCommandToClient("Print~You must not have a property with 2 houses more than any property in the set.\r\n", 2);
                    }
                }else{
                    getOwner().getClientInfo().sendCommandToClient("Print~You must have a complete property set to build houses!\r\n", 2);
                }
            }else{
                getOwner().getClientInfo().sendCommandToClient("Print~You don't have enough money.\r\n", 2);
            }
        }else{
            getOwner().getClientInfo().sendCommandToClient("Print~You cannot build more houses on this plot.\r\n", 2);
        }
    }
    public void chargeRent(Player otherPlayer) throws InterruptedException {
        otherPlayer.addMoney(-1 * rent[houses], true);
        getOwner().addMoney(rent[houses], false);
    }
    public synchronized void action(Player player) throws InterruptedException {
        if(getOwner() == null){
            String answer = "view";
            while(answer.equalsIgnoreCase("view")) {
                answer = player.getClientInfo().sendRequestToClient("MakeChoice~true~Do you want to buy this property for " + getSellPrice() + "? (\"yes\" to buy, \"no\" to leave, \"view\" to view more) : ~yes~no~view", 1);
                if(answer.equalsIgnoreCase("view")){
                    printRentDetails(player);
                }
            }
            if(answer.equalsIgnoreCase("yes")){
                if(player.getMoney() >= getSellPrice()){
                    player.getClientInfo().sendCommandToClient("Print~Property sold.\r\n", 2);
                    buyProperty(player);
                }else{
                    player.getClientInfo().sendCommandToClient("Print~You don't have enough money.\r\n", 2);
                }
            }
        }else{
            if(player == getOwner()){
                getOwner().getClientInfo().sendCommandToClient("Print~You land on your own property, nothing happens.\r\n", 2);
            }else{
                chargeRent(player);
                getOwner().getClientInfo().sendCommandToClient("Print~As the owner has " + houses + " houses, you pay " + getOwner().getName() + " " + rent[houses] + ".\r\n", 2);
            }
        }
    }

    public void printCard(Player player) throws InterruptedException {
        player.getClientInfo().sendCommandToClient("Print~" + getPosition() + "\t " + getName() + "\t Property\t Land you can build and charge rent on.\t ", 1);
        if(getOwner() == null){
            player.getClientInfo().sendCommandToClient("Print~For sale at " + getSellPrice(), 1);
        }else{
            player.getClientInfo().sendCommandToClient("Print~Owner: " + getOwner().getName(), 1);
        }
        player.getClientInfo().sendCommandToClient("Print~\t On this square: ", 1);
        player.getClientInfo().sendCommandToClient("Print~" + getEveryoneOnSquare() + "\r\n", 2);
    }

    public void printRentDetails(Player player) throws InterruptedException {
        player.getClientInfo().sendCommandToClient("Print~Name: " + getName() + "\r\nPosition: " + getPosition() + "\r\n----------RENT----------" + "\r\nNo houses:\t" + rent[0] + "\r\n1 house:\t" + rent[1] + "\r\n2 houses:\t" + rent[2] + "\r\n3 houses:\t" + rent[3] + "\r\n4 houses:\t" + rent[4] + "\r\nHotel:   \t" + rent[5] + "\r\n", 10);
    }
    public int getHouses(){
        return houses;
    }
    public int[] getRent() {
        return rent;
    }
    public int getHousePrice(){
        return housePrice;
    }
    public int getPropertySet(){
        return propertySet;
    }
}