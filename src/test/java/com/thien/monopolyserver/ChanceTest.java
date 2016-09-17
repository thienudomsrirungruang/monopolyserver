package com.thien.monopolyserver;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

public class ChanceTest {
    @Test
    public void checkVariables(){
        Chance chance = new Chance(10, new Random(), new Board());
        assertEquals(chance.getPosition(), 10);
        assertEquals(chance.getName(), "Chance");
    }
    @Test
    public void allChanceScenarios() throws InterruptedException {
        Board board = Mockito.mock(Board.class);
        ClientInfo cInfo = new ClientInfo(Mockito.mock(ServerDispatcher.class));
        ClientSender cs = Mockito.mock(ClientSender.class);
        Mockito.doNothing().when(cs).sendMessage((String) any());
        cInfo.setClientSender(cs);
        Player player = new Player("Test", board, cInfo);
        Player otherPlayer = new Player("Test2", board, cInfo);
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(player);
        players.add(otherPlayer);
        ArrayList<Square> squares = Mockito.mock(ArrayList.class);
        Mockito.when(board.getSquares()).thenReturn(squares);
        Mockito.when(board.getPlayers()).thenReturn(players);
        Square square = Mockito.mock(Square.class);
        for(int i = 0; i < 40; i++){
            Mockito.when(squares.get(i)).thenReturn(square);
        }
        Mockito.doNothing().when(square).action(player);
        Random randomMock = Mockito.mock(Random.class);
        Mockito.when(randomMock.nextInt(14)).thenReturn(0, 1, 2, 3, 3, 4, 4, 4, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        Chance chance = new Chance(100, randomMock, board);

        player.goTo(20, false);
        chance.action(player); // 0
        assertEquals(player.getPosition(), 0);

        chance.action(player); // 1
        assertEquals(player.getPosition(), 24);

        chance.action(player); // 2
        assertEquals(player.getPosition(), 11);

        chance.action(player); // 3
        assertEquals(player.getPosition(), 12);

        player.goTo(20, false);
        chance.action(player); // 3
        assertEquals(player.getPosition(), 28);

        player.goTo(0, false);
        chance.action(player); // 4
        assertEquals(player.getPosition(), 5);

        player.goTo(10, false);
        chance.action(player); // 4
        assertEquals(player.getPosition(), 15);

        player.goTo(20, false);
        chance.action(player); // 4
        assertEquals(player.getPosition(), 25);

        player.goTo(30, false);
        chance.action(player); // 4
        assertEquals(player.getPosition(), 35);

        player.setMoney(0);
        chance.action(player); // 5
        assertEquals(player.getMoney(), 500);

        player.goTo(12, false);
        chance.action(player); // 6
        assertEquals(player.getPosition(), 9);

        chance.action(player); // 7
        assertTrue(player.getJailed());
        assertEquals(player.getPosition(), 10);
        player.setJailed(false);

        player.setMoney(150);
        chance.action(player); // 8
        assertEquals(player.getMoney(), 0);

        chance.action(player); // 9
        assertEquals(player.getPosition(), 5);

        chance.action(player); // 10
        assertEquals(player.getPosition(), 39);

        player.setMoney(500);
        otherPlayer.setMoney(0);
        chance.action(player); // 11
        assertEquals(player.getMoney(), 0);
        assertEquals(otherPlayer.getMoney(), 500);

        chance.action(player); // 12
        assertEquals(player.getMoney(), 1500);

        chance.action(player); // 13
        assertEquals(player.getMoney(), 2500);
    }
}