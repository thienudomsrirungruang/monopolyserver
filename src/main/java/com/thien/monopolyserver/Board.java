package com.thien.monopolyserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Board {
    private ArrayList<Square> squares;
    private ArrayList<Player> players;
    public Board(){
        createSquares();
    }
    public void createSquares(){
        squares = new ArrayList<Square>();
        String[] names = {"Go","Old Kent Road","Community Chest","Whitechapel Road","Income Tax","King's Cross Station","The Angel Islington","Chance","Euston Road","Pentonville Road","Jail",
                "Pall Mall","Electric Company","Whitehall","Northnumberland Avenue","Marylebone Station","Bow Street","Community Chest","Marlborough Street","Vine Street","Free Parking",
                "Strand","Chance","Fleet Street","Trafalgar Square","Fenchurch St Station","Leicester Square","Coventry Street","Water Works","Picadilly","Go to Jail",
                "Regent Street","Oxford Street","Community Chest","Bond Street","Liverpool Street Station","Chance","Park Lane","Super Tax","Mayfair"};
        for(int i = 0; i < 40; i++) {
            switch (i) {
                case(0):
                    squares.add(new Go(this));
                    break;
                case(10):
                    squares.add(new Jail(this));
                    break;
                case(20):
                    squares.add(new FreeParking(this));
                    break;
                case(30):
                    squares.add(new GoToJail(this));
                    break;
                case(12):
                    squares.add(new Utility(12, names[i], this));
                    break;
                case(28):
                    squares.add(new Utility(28, names[i], this));
                    break;
                case(5):
                    squares.add(new Railroad(5, names[i], this));
                    break;
                case(15):
                    squares.add(new Railroad(15, names[i], this));
                    break;
                case(25):
                    squares.add(new Railroad(25, names[i], this));
                    break;
                case(35):
                    squares.add(new Railroad(35, names[i], this));
                    break;
                case(7):
                    squares.add(new Chance(7, new Random(), this));
                    break;
                case(22):
                    squares.add(new Chance(22, new Random(), this));
                    break;
                case(36):
                    squares.add(new Chance(36, new Random(), this));
                    break;
                case(2):
                    squares.add(new CommunityChest(2, new Random(), this));
                    break;
                case(17):
                    squares.add(new CommunityChest(17, new Random(), this));
                    break;
                case(33):
                    squares.add(new CommunityChest(33, new Random(), this));
                    break;
                case(4):
                    squares.add(new Tax(4, names[i], this, 1000));
                    break;
                case(38):
                    squares.add(new Tax(38, names[i], this, 2000));
                    break;
                default:
                    squares.add(new Property(i, names[i], this, i / 5));
                    break;
            }
        }
    }
    public void setPlayers(ArrayList<Player> playerList){
        players = playerList;
    }
    public void startGame() throws IOException, InterruptedException {
        for(Player player : players){
            player.addMoney(15000, false);
        }
        int turn = 0;
        while(players.size() > 1) {
            players.get(turn).doTurn(new Random());
            turn = (turn + 1) % players.size();
        }
    }
    public void printBoard(Player player) throws InterruptedException {
        for(Square square : squares){
            square.printCard(player);
        }
    }
    public ArrayList<Square> getSquares(){
        return squares;
    }
    public ArrayList<Player> getPlayers(){
        return players;
    }
}