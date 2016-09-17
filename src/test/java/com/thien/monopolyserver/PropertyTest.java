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
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;

public class PropertyTest {
    @Test
    public void setNameAndPosition() {
        Property property = new Property(15, "Test Property", new Board(), 0);
        assertEquals(15, property.getPosition());
        assertEquals("Test Property", property.getName());
    }

    @Test
    public void priceAndRentMathCheck() {
        for (int i = 0; i < 100; i++) {
            Property property = new Property(1, "Cheap Stuff", new Board(), 0);
            assertTrue(500 < property.getSellPrice() && property.getSellPrice() < 750);
            assertTrue(100 < property.getRent()[0] && property.getRent()[0] < 150);
            assertTrue(4.98 <= ((double) property.getRent()[1]) / property.getRent()[0] && ((double) property.getRent()[1]) / property.getRent()[0] <= 5.51);
            assertTrue(2.97 <= ((double) property.getRent()[2]) / property.getRent()[1] && ((double) property.getRent()[2]) / property.getRent()[1] <= 3.22);
            assertTrue(2.28 <= ((double) property.getRent()[3]) / property.getRent()[2] && ((double) property.getRent()[3]) / property.getRent()[2] <= 2.51);
            assertTrue(1.18 <= ((double) property.getRent()[4]) / property.getRent()[3] && ((double) property.getRent()[4]) / property.getRent()[3] <= 1.255);
            assertTrue(1.15 <= ((double) property.getRent()[5]) / property.getRent()[4] && ((double) property.getRent()[5]) / property.getRent()[4] <= 1.235);
            assertEquals(500, property.getHousePrice());
        }
        for (int i = 0; i < 100; i++) {
            Property property = new Property(39, "Super Expensive", new Board(), 0);
            assertTrue(4000 < property.getSellPrice() && property.getSellPrice() < 6500);
            assertTrue(600 < property.getRent()[0] && property.getRent()[0] < 1200);
            assertTrue(4.98 <= ((double) property.getRent()[1]) / property.getRent()[0] && ((double) property.getRent()[1]) / property.getRent()[0] <= 5.55);
            assertTrue(2.98 <= ((double) property.getRent()[2]) / property.getRent()[1] && ((double) property.getRent()[2]) / property.getRent()[1] <= 3.25);
            assertTrue(2.29 <= ((double) property.getRent()[3]) / property.getRent()[2] && ((double) property.getRent()[3]) / property.getRent()[2] <= 2.55);
            assertTrue(1.18 <= ((double) property.getRent()[4]) / property.getRent()[3] && ((double) property.getRent()[4]) / property.getRent()[3] <= 1.3);
            assertTrue(1.15 <= ((double) property.getRent()[5]) / property.getRent()[4] && ((double) property.getRent()[5]) / property.getRent()[4] <= 1.26);
            assertEquals(2000, property.getHousePrice());
        }
    }

    @Test
    public void trySettingOwner() {
        Property property = new Property(1, "Test", new Board(), 0);
        property.setOwner(new Player("Test", new Board(), new ClientInfo(Mockito.mock(ServerDispatcher.class))));
        assertEquals(property.getOwner().getName(), "Test");
    }

    @Test
    public void tryChargingRent() throws InterruptedException {
        Board board = new Board();
        Player owner = Mockito.mock(Player.class);

        Mockito.when(owner.checkCompletePropertySet(0)).thenReturn(true);
        Mockito.when(owner.getLeastNumberOfHousesInSet(0)).thenReturn(10);

        Mockito.when(owner.getName()).thenReturn("Owner");
        Mockito.when(owner.getMoney()).thenReturn(1000000);

        ClientInfo mockedClientInfo = Mockito.mock(ClientInfo.class);
        Mockito.doNothing().when(mockedClientInfo).sendCommandToClient(anyString(), anyInt());
        Mockito.when(owner.getClientInfo()).thenReturn(mockedClientInfo);

        Player payer = new Player("Payer", board, mockedClientInfo);
        Property property = new Property(19, "A Property", new Board(), 0);
        property.setOwner(owner);
        int[] rent = property.getRent();
        payer.addMoney(1000000, false);
        for (int i = 0; i < 6; i++) {
            int payerBefore = payer.getMoney();
            property.chargeRent(payer);
            assertEquals(payerBefore - rent[i], payer.getMoney());
            property.addAHouse();
        }
        assertEquals(property.getHouses(), 5);
    }

    @Test
    public void tryBuying() throws IOException, InterruptedException {
        Player player = makePlayers(1, new Board()).get(0);
        Property property = new Property(10, "Property", new Board(), 0);
        property.buyProperty(player);
        assertEquals(player.getName(), property.getOwner().getName());
        assertEquals(1, player.getOwnedProperties().size());
        assertEquals("Property", player.getOwnedProperties().get(0).getName());
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