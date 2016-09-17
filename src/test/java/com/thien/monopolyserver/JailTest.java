package com.thien.monopolyserver;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JailTest {
    @Test
    public void checkVariables(){
        Jail jail = new Jail(new Board());
        assertEquals(jail.getPosition(), 10);
        assertEquals(jail.getName(), "Jail");
    }
}