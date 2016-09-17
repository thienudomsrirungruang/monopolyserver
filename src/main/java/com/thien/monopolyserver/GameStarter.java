package com.thien.monopolyserver;

import java.io.IOException;
import java.util.ArrayList;

public class GameStarter {
    public void startGame(ServerDispatcher sd) throws IOException, InterruptedException {
        Board board = new Board();
        board.setPlayers(getPlayers(sd.getPlayerList(), board));
        board.startGame();
    }
    public synchronized ArrayList<Player> getPlayers(ArrayList<ClientInfo> playerList, Board board){
        ArrayList<Player> players = new ArrayList<Player>();
        for(ClientInfo clientInfo : playerList){
            clientInfo.initPlayer(board);
            players.add(clientInfo.getPlayer());
        }
        return players;
    }
}