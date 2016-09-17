package com.thien.monopolyserver;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MonopolyServerTest {
    MonopolyServer instance;
    @Before
    public void startup(){
        instance = new MonopolyServer();
    }
    @Test
    public void checkPort(){
        assertEquals(41875, instance.getPort());
    }
}
