package com.thien.monopolyserver;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

public class PlayerFinderTest {
    @Ignore
    @Test
    public void tryAcceptingPlayers() throws IOException, InterruptedException{
        ServerSocket ss = Mockito.mock(ServerSocket.class);
        Socket socket = Mockito.mock(Socket.class);
        Mockito.when(socket.getInetAddress()).thenReturn(null);
        Mockito.when(ss.accept()).thenReturn(socket);
        GameStarter gs = Mockito.mock(GameStarter.class);
        ServerDispatcher sd = new ServerDispatcher(gs, ss);
        Mockito.doNothing().when(gs).startGame(sd);
        PlayerFinder pf = new PlayerFinder(sd, ss);
        pf.start();
        Thread.sleep(1000);
        assertEquals(2, sd.getPlayerList().size());
    }
}