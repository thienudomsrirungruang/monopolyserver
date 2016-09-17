package com.thien.monopolyserver;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

public class TaxTest {
    @Test
    public void checkVariables(){
        Tax tax = new Tax(10, "Income Tax", new Board(), 0);
        assertEquals(tax.getPosition(), 10);
        assertEquals(tax.getName(), "Income Tax");
    }
    @Test
    public void taxCheck() throws InterruptedException {
        ClientInfo ci = new ClientInfo(Mockito.mock(ServerDispatcher.class));
        ClientSender cs = Mockito.mock(ClientSender.class);
        Mockito.doNothing().when(cs).sendMessage((String) any());
        ci.setClientSender(cs);
        Player player = new Player("Player", new Board(), ci);
        Tax tax = new Tax(20, "Tax", new Board(), 1000);
        player.setMoney(1500);
        tax.action(player);
        assertEquals(500, player.getMoney());
    }
}