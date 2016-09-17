package com.thien.monopolyserver;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BoardTest {
    @Test
    public void tryCreatingSquares(){
        Board board = new Board();
        board.createSquares();
        assertEquals(board.getSquares().get(0).getClass().getCanonicalName(), "com.thien.monopolyserver.Go");
        assertEquals(board.getSquares().get(10).getClass().getCanonicalName(), "com.thien.monopolyserver.Jail");
        assertEquals(board.getSquares().get(20).getClass().getCanonicalName(), "com.thien.monopolyserver.FreeParking");
        assertEquals(board.getSquares().get(30).getClass().getCanonicalName(), "com.thien.monopolyserver.GoToJail");
        assertEquals(board.getSquares().get(7).getClass().getCanonicalName(), "com.thien.monopolyserver.Chance");
        assertEquals(board.getSquares().get(17).getClass().getCanonicalName(), "com.thien.monopolyserver.CommunityChest");
        assertEquals(board.getSquares().get(21).getClass().getCanonicalName(), "com.thien.monopolyserver.Property");
        assertEquals(board.getSquares().get(12).getClass().getCanonicalName(), "com.thien.monopolyserver.Utility");
        assertEquals(board.getSquares().get(35).getClass().getCanonicalName(), "com.thien.monopolyserver.Railroad");
        assertEquals(board.getSquares().get(38).getClass().getCanonicalName(), "com.thien.monopolyserver.Tax");
    }
}