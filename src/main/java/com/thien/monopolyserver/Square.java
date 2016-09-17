package com.thien.monopolyserver;
public abstract class Square {
    private int position;
    private String name;
    private Board board;
    public Square(int position, String name, Board board){
        this.position = position;
        this.name = name;
        this.board = board;
    }
    public int getPosition(){
        return position;
    }
    public String getName(){
        return name;
    }
    public abstract void action(Player player) throws InterruptedException;
    public abstract void printCard(Player player) throws InterruptedException;
    public Board getBoard(){
        return board;
    }
    public String getEveryoneOnSquare(){
        String output = "";
        for(Player player : board.getPlayers()){
            if(player.getPosition() == position){
                if(output != ""){
                    output += ", ";
                }
                output += player.getName();
            }
        }
        if(output.equals("")){
            output = "None";
        }
        return output;
    }
}