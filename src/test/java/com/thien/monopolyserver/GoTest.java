package com.thien.monopolyserver;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GoTest{
    @Test
    public void checkVariables(){
        Go go = new Go(new Board());
        assertEquals(go.getPosition(), 0);
        assertEquals(go.getName(), "Go");
    }
}