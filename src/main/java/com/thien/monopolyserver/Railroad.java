package com.thien.monopolyserver;

public class Railroad extends BuyableSquare{
    public Railroad(int position, String name, Board board){
        super(position, name, board);
        setSellPrice(2000);
    }
    public void action(Player player) throws  InterruptedException{
        if(getOwner() == null){
            String answer = "view";
            while(answer.equalsIgnoreCase("view")) {
                answer = player.getClientInfo().sendRequestToClient("MakeChoice~true~Do you want to buy this railroad for " + getSellPrice() + "? (\"yes\" to buy, \"no\" to leave, \"view\" to view more) : ~yes~no~view", 1);
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

    public void printCard(Player player) throws InterruptedException{
        player.getClientInfo().sendCommandToClient("Print~" + getPosition() + "\t " + getName() + "\t Railroad\t Land you can charge rent on.\t ", 1);
        if(getOwner() == null){
            player.getClientInfo().sendCommandToClient("Print~For sale at " + getSellPrice(), 1);
        }else{
            player.getClientInfo().sendCommandToClient("Print~Owner: " + getOwner().getName(), 1);
        }
        player.getClientInfo().sendCommandToClient("Print~\t On this square: ", 1);
        player.getClientInfo().sendCommandToClient("Print~" + getEveryoneOnSquare() + "\r\n", 2);
    }

    public void chargeRent(Player otherPlayer) throws InterruptedException {
        int ownedRailroads = 0;
        if(getOwner().getOwnedProperties().contains(getBoard().getSquares().get(5))){
            ownedRailroads++;
        }
        if(getOwner().getOwnedProperties().contains(getBoard().getSquares().get(15))){
            ownedRailroads++;
        }
        if(getOwner().getOwnedProperties().contains(getBoard().getSquares().get(25))){
            ownedRailroads++;
        }
        if(getOwner().getOwnedProperties().contains(getBoard().getSquares().get(35))){
            ownedRailroads++;
        }
        int rent = 125 * (int) Math.pow(2, ownedRailroads);
        otherPlayer.getClientInfo().sendCommandToClient("Print~As the owner owns " + ownedRailroads + " railroad(s), you pay " + rent + "\r\n", 2);
        getOwner().addMoney(rent, false);
        otherPlayer.addMoney(-1 * rent, true);
    }
    public void printRentDetails(Player player) throws InterruptedException {
        player.getClientInfo().sendCommandToClient("Print~Name: " + getName() + "\r\n" + "Position: " + getPosition() + "\r\n----------RENT----------\r\n1 railroad owned: 250\r\n2 railroads owned: 500\r\n3 railroads owned: 1000\r\n4 railroads owned: 2000\r\n", 8);
    }
}