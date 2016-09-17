package com.thien.monopolyserver;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class RailroadTest {
    @Test
    public void checkVariables(){
        Railroad railroad = new Railroad(12, "Test", new Board());
        assertEquals(railroad.getPosition(), 12);
        assertEquals(railroad.getName(), "Test");
    }
    @Test
    public void tryBuying() throws IOException, InterruptedException {
        Player player = makePlayers(1, new Board()).get(0);
        Railroad railroad = new Railroad(10, "Railroad", new Board());
        railroad.buyProperty(player);
        assertEquals(player.getName(), railroad.getOwner().getName());
        assertEquals(1, player.getOwnedProperties().size());
        assertEquals("Railroad", player.getOwnedProperties().get(0).getName());
    }
    @Test
    public void tryChargingRent() throws InterruptedException, IOException {
        Board board = new Board();
        ArrayList<Player> players = makePlayers(2, board);
        Railroad railroad1 = (Railroad) board.getSquares().get(5);
        Railroad railroad2 = (Railroad) board.getSquares().get(15);
        Railroad railroad3 = (Railroad) board.getSquares().get(25);
        Railroad railroad4 = (Railroad) board.getSquares().get(35);
        Player owner = players.get(0);
        Player payer = players.get(1);

        owner.setMoney(2000);
        railroad1.buyProperty(owner);
        payer.setMoney(250);
        railroad1.chargeRent(payer);
        assertEquals(0, payer.getMoney());
        assertEquals(250, owner.getMoney());

        owner.setMoney(2000);
        railroad2.buyProperty(owner);
        payer.setMoney(500);
        railroad1.chargeRent(payer);
        assertEquals(0, payer.getMoney());
        assertEquals(500, owner.getMoney());

        owner.setMoney(2000);
        railroad3.buyProperty(owner);
        payer.setMoney(1000);
        railroad1.chargeRent(payer);
        assertEquals(0, payer.getMoney());
        assertEquals(1000, owner.getMoney());

        owner.setMoney(2000);
        railroad4.buyProperty(owner);
        payer.setMoney(2000);
        railroad4.chargeRent(payer);
        assertEquals(0, payer.getMoney());
        assertEquals(2000, owner.getMoney());
    }
    ArrayList<Player> makePlayers(int amount, Board board) throws IOException {
        ArrayList<Player> players = new ArrayList<Player>();
        for(int i = 0; i < amount; i++){
            ClientInfo clientInfo = new ClientInfo(Mockito.mock(ServerDispatcher.class));
            PrintWriter mockedClientOutput = new PrintWriter(new StringWriter());
            ServerSocket mockedServerSocket = Mockito.mock(ServerSocket.class);
            Socket socket = Mockito.mock(Socket.class);
            Mockito.when(mockedServerSocket.accept()).thenReturn(socket);
            ClientSender clientSender = new ClientSender(clientInfo, new ServerDispatcher(new GameStarter(), mockedServerSocket), mockedClientOutput);
            clientInfo.setClientSender(clientSender);
            Player player = new Player("Test", board, clientInfo);
            players.add(player);
        }
        board.setPlayers(players);
        return players;
    }
}