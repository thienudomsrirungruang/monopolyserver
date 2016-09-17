package com.thien.monopolyserver;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class GoToJailTest {
    @Test
    public void checkVariables() {
        GoToJail goToJail = new GoToJail(new Board());
        assertEquals("Go To Jail", goToJail.getName());
        assertEquals(30, goToJail.getPosition());
    }
    @Test
    public void goToJail() throws InterruptedException {
        Board board = new Board();
        ClientSender cs = Mockito.mock(ClientSender.class);
        Mockito.doNothing().when(cs).sendMessage("Command~Print~You go to jail!\r\n");
        ClientInfo ci = new ClientInfo(Mockito.mock(ServerDispatcher.class));
        ci.setClientSender(cs);
        Player player = new Player("Player", board, ci);
        player.goTo(30, true);
        board.getSquares().get(30).action(player);
        assertEquals(true, player.getJailed());
        assertEquals(10, player.getPosition());
    }
}