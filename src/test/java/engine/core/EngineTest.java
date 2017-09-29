package engine.core;

import gui.game.GameGUI;

import static org.junit.Assert.*;

public class EngineTest {

    private Engine e;

    @org.junit.Before
    public void setUp() throws Exception {
        e = new Engine(null);
    }

    @org.junit.Test
    public void shutdown() throws Exception {
        e.startGame();
        e.shutdown();
        assertTrue(true);
    }

    @org.junit.Test
    public void shutdown1() throws Exception {
        e.startGame();
        e.shutdown(true);
        assertTrue(true);
    }

    @org.junit.Test
    public void shutdown2() throws Exception {
        e.startGame();
        e.shutdown(false);
        assertTrue(true);
    }

    @org.junit.Test
    public void startGame() throws Exception {
        e.startGame();
        assertTrue(true);
    }

    @org.junit.Test
    public void stopGame() throws Exception {
        e.startGame();
        e.stopGame();
        assertTrue(true);
    }

}