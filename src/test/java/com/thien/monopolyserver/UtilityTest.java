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
import static org.junit.Assert.assertTrue;

public class UtilityTest {
    @Test
    public void checkVariables(){
        Utility utility = new Utility(12, "Test", new Board());
        assertEquals(utility.getPosition(), 12);
        assertEquals(utility.getName(), "Test");
    }
    @Test
    public void tryBuying() throws IOException, InterruptedException {
        Player player = makePlayers(1, new Board()).get(0);
        Utility utility = new Utility(10, "Utility", new Board());
        utility.buyProperty(player);
        assertEquals(player.getName(), utility.getOwner().getName());
        assertEquals(1, player.getOwnedProperties().size());
        assertEquals("Utility", player.getOwnedProperties().get(0).getName());
    }
    @Test
    public void tryChargingRent() throws InterruptedException, IOException {
        Board board = new Board();
        ArrayList<Player> players = makePlayers(2, board);
        Utility utility = (Utility) board.getSquares().get(12);
        Player owner = players.get(0);
        Player payer = players.get(1);
        utility.setOwner(owner);
        owner.setMoney(1500);
        utility.buyProperty(owner);
        for(int i = 0; i < 10000; i++){
            payer.setMoney(10000);
            owner.setMoney(0);
            utility.chargeRent(payer);
            assertTrue(payer.getMoney() >= 9520 && payer.getMoney() <= 9920);
            assertEquals(10000, payer.getMoney() + owner.getMoney());
        }
        owner.setMoney(1500);
        Utility anotherProperty = (Utility) board.getSquares().get(28);
        anotherProperty.buyProperty(owner);
        for(int i = 0; i < 10000; i++){
            payer.setMoney(10000);
            owner.setMoney(0);
            utility.chargeRent(payer);
            assertTrue(payer.getMoney() >= 8800 && payer.getMoney() <= 9800);
            assertEquals(10000, payer.getMoney() + owner.getMoney());
        }
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