package com.thien.monopolyserver;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

public class SquareTest {
    @Test
    public void tryFindingPeople() throws InterruptedException {
        Board board = new Board();
        ClientInfo ci = new ClientInfo(Mockito.mock(ServerDispatcher.class));
        ClientSender cs = Mockito.mock(ClientSender.class);
        Mockito.doNothing().when(cs).sendMessage((String) any());
        ci.setClientSender(cs);
        Player player = new Player("1", board, ci);
        Player otherPlayer = new Player("2", board, ci);
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(player);
        players.add(otherPlayer);
        board.setPlayers(players);
        Square square = board.getSquares().get(1);
        assertEquals("None", square.getEveryoneOnSquare());
        player.goTo(1, false);
        otherPlayer.goTo(1, false);
        assertEquals("1, 2", square.getEveryoneOnSquare());
    }
}