package com.thien.monopolyserver;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ServerDispatcherTest {
    @Test
    public void checkOpenState() throws IOException, InterruptedException{
        GameStarter gs = Mockito.mock(GameStarter.class);
        ServerDispatcher sd = new ServerDispatcher(gs, new ServerSocket());
        Mockito.doNothing().when(gs).startGame(sd);
        sd.start();
        assertTrue(sd.shouldBeOpen());
        sd.addClient(new ClientInfo(Mockito.mock(ServerDispatcher.class)));
        Thread.sleep(200);
        assertTrue(sd.shouldBeOpen());
        sd.addClient(new ClientInfo(Mockito.mock(ServerDispatcher.class)));
        Thread.sleep(200);
        assertFalse(sd.shouldBeOpen());
    }
}