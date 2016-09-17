package com.thien.monopolyserver;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

public class CommunityChestTest {
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
        Mockito.when(randomMock.nextInt(15)).thenReturn(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14);
        CommunityChest communityChest = new CommunityChest(100, randomMock, board);

        player.goTo(20, false);
        communityChest.action(player); // 0
        assertEquals(player.getPosition(), 0);

        player.setMoney(0);
        communityChest.action(player); // 1
        assertEquals(player.getMoney(), 2000);

        communityChest.action(player); // 2
        assertEquals(player.getMoney(), 1500);

        communityChest.action(player); // 3
        assertEquals(player.getMoney(), 2000);

        communityChest.action(player); // 4
        assertTrue(player.getJailed());
        assertEquals(player.getPosition(), 10);

        otherPlayer.setMoney(500);
        communityChest.action(player); // 5
        assertEquals(player.getMoney(), 2500);
        assertEquals(otherPlayer.getMoney(), 0);

        communityChest.action(player); // 6
        assertEquals(player.getMoney(), 3500);

        communityChest.action(player); // 7
        assertEquals(player.getMoney(), 3700);

        otherPlayer.setMoney(100);
        communityChest.action(player); // 8
        assertEquals(otherPlayer.getMoney(), 0);
        assertEquals(player.getMoney(), 3800);

        communityChest.action(player); // 9
        assertEquals(player.getMoney(), 4800);

        communityChest.action(player); // 10
        assertEquals(player.getMoney(), 3800);

        communityChest.action(player); // 11
        assertEquals(player.getMoney(), 2300);

        communityChest.action(player); // 12
        assertEquals(player.getMoney(), 2550);

        communityChest.action(player); // 13
        assertEquals(player.getMoney(), 2650);

        communityChest.action(player); // 14
        assertEquals(player.getMoney(), 3650);
    }
}