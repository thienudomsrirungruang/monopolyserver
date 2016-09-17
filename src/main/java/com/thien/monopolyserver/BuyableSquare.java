package com.thien.monopolyserver;
public abstract class BuyableSquare extends Square{
    private Player owner;
    private int sellPrice;
    public BuyableSquare(int position, String name, Board board){
        super(position, name, board);
        owner = null;
    }
    public abstract void printRentDetails(Player player) throws InterruptedException;
    public abstract void chargeRent(Player otherPlayer) throws InterruptedException;
    public void buyProperty(Player player) throws InterruptedException {
        player.addMoney(-1 * getSellPrice(), true);
        setOwner(player);
        getOwner().addProperty(this);
    }
    public Player getOwner(){
        return owner;
    }
    public void setOwner(Player owner){
        this.owner = owner;
    }
    public int getSellPrice() {
        return sellPrice;
    }
    public void setSellPrice(int sellPrice){
        this.sellPrice = sellPrice;
    }
}