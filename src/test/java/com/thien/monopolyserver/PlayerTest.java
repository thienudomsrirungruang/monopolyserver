package com.thien.monopolyserver;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;

public class PlayerTest {
    Player player;
    Board board;
    @Before
    public void setPlayer(){
        ClientSender cs = Mockito.mock(ClientSender.class);
        Mockito.doNothing().when(cs).sendMessage("Command~Print~You buy a house in Old Kent Road for 500.\r\n");
        Mockito.doNothing().when(cs).sendMessage("Command~Print~You buy a house in Whitechapel Road for 500.\r\n");
        ClientInfo ci = new ClientInfo(Mockito.mock(ServerDispatcher.class));
        ci.setClientSender(cs);
        board = new Board();
        player = new Player("Test", board, ci);
    }
    @Test
    public void makeName(){
        assertEquals(player.getName(), "Test");
    }
    @Test
    public void tryMoving() throws InterruptedException {
        player.move(12);
        assertEquals(player.getPosition(), 12);
    }
    @Test
    public void tryWarping() throws InterruptedException {
        player.goTo(12, true);
        assertEquals(12, player.getPosition());
        player.goTo(3, true);
        assertEquals(3, player.getPosition());
        assertEquals(2000, player.getMoney());
    }
    @Test
    public void tryAddingMoney() throws InterruptedException {
        player.addMoney(123, true);
        assertEquals(123, player.getMoney());
    }
    @Test
    public void tryRollingDice() throws InterruptedException {
        for(int i = 0; i < 10000; i++){
            ClientSender cs = Mockito.mock(ClientSender.class);
            Mockito.doNothing().when(cs).sendMessage((String) any());
            ClientInfo cInfo = new ClientInfo(Mockito.mock(ServerDispatcher.class));
            cInfo.setClientSender(cs);
            player = new Player("Test", new Board(), cInfo);
            player.diceRoll(new Random(), true);
            assertTrue(player.getPosition() >= 2 && player.getPosition() <= 12);
        }
    }
    @Test
    public void rollDoubles() throws InterruptedException {
        Random notSoRandom = Mockito.mock(Random.class);
        Mockito.when(notSoRandom.nextInt(6)).thenReturn(0);
        assertTrue(player.diceRoll(notSoRandom, false));
        Mockito.when(notSoRandom.nextInt(6)).thenReturn(2);
        assertTrue(player.diceRoll(notSoRandom, false));
        Mockito.when(notSoRandom.nextInt(6)).thenReturn(5);
        assertTrue(player.diceRoll(notSoRandom, false));
        Mockito.when(notSoRandom.nextInt(6)).thenReturn(5, 3);
        assertFalse(player.diceRoll(notSoRandom, false));
        Mockito.when(notSoRandom.nextInt(6)).thenReturn(0, 1);
        assertFalse(player.diceRoll(notSoRandom, false));
    }
    @Test
    public void tryMakingPropertySet() throws InterruptedException {
        player.setMoney(100000);
        assertFalse(player.checkCompletePropertySet(0));
        Property p1 = (Property) board.getSquares().get(1);
        p1.buyProperty(player);
        Property p2 = (Property) board.getSquares().get(3);
        p2.buyProperty(player);
        assertTrue(player.checkCompletePropertySet(0));
        assertEquals(0, player.getLeastNumberOfHousesInSet(0));
        p1.addAHouse();
        p2.addAHouse();
        p1.addAHouse();
        p2.addAHouse();
        p1.addAHouse();
        assertEquals(2, player.getLeastNumberOfHousesInSet(0));
    }
    @Test(expected = IllegalStateException.class)
    public void tryGettingException(){
        player.getLeastNumberOfHousesInSet(4);
    }
    @Ignore
    @Test
    public void doTurn() throws IOException, InterruptedException {
        Random diceMock = Mockito.mock(Random.class);
        Mockito.when(diceMock.nextInt(6)).thenReturn(2, 3);
        ArrayList<Square> squares = Mockito.mock(ArrayList.class);
        Square square = Mockito.mock(Square.class);
        Mockito.when(squares.get(7)).thenReturn(square);
        Mockito.doNothing().when(square).action(player);
        player.doTurn(diceMock);
    }
}