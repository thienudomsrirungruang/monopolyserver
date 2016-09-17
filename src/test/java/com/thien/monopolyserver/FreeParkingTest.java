package com.thien.monopolyserver;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FreeParkingTest {
    @Test
    public void checkVariables(){
        FreeParking fp = new FreeParking(new Board());
        assertEquals("Free Parking", fp.getName());
        assertEquals(20, fp.getPosition());
    }
}