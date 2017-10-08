/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package engine.core;

import gui.game.GameGUI;
import utilties.models.Game;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EngineTest {

    private Engine e;

    @org.junit.BeforeClass
    public static void setTest() throws Exception {
        Engine.TEST_FLAG = true;
    }

    @org.junit.Before
    public void setUp() throws Exception {
        e = new Engine(null);
    }

    @org.junit.Test
    public void shutdown() throws Exception {
        e.startGame();
        e.shutdown();
    }

    @org.junit.Test
    public void shutdown1() throws Exception {
        e.startGame();
        e.shutdown(true);
    }

    @org.junit.Test
    public void shutdown2() throws Exception {
        e.startGame();
        e.shutdown(false);
    }

    @org.junit.Test
    public void startGame() throws Exception {
        e.startGame();
    }

    @org.junit.Test
    public void stopGame() throws Exception {
        e.startGame();
        e.stopGame();
    }

    @org.junit.Test
    public void cleanup() throws Exception {
        e.startGame();
        e.stopGame();
        e.cleanup();
    }

    @org.junit.Test
    public void gameRunning() throws Exception {
        e.startGame();
        assertTrue(e.isRunning());
    }

    @org.junit.Test
    public void gameStoppedNotRunning() throws Exception {
        e.startGame();
        e.stopGame();
        assertFalse(e.isRunning());
    }

    @org.junit.Test
    public void testValidWait() throws Exception {
        e.performWait(System.currentTimeMillis() - Engine.TICK_TIME / 10);
    }

    @org.junit.Test
    public void testBarelyValidWait() throws Exception {
        e.performWait(System.currentTimeMillis() - Engine.TICK_TIME * 2 / 3);
    }

    @org.junit.Test
    public void testInvalidWait() throws Exception {
        e.performWait(System.currentTimeMillis() - Engine.TICK_TIME * 2);
    }

    @org.junit.Test
    public void interruptEngineWait() throws Exception {
        Thread t = new Thread(() -> e.performWait(System.currentTimeMillis() + 10000));
        t.start();
        Thread.sleep(100);
        t.interrupt();
    }

    @org.junit.Test
    public void properSleepDuration() throws Exception {
        long curtime = System.currentTimeMillis();
        long endtime = e.performWait(curtime);
        assertTrue(endtime - curtime > Engine.TICK_TIME - 10);
        assertTrue(endtime - curtime < Engine.TICK_TIME + 10);
    }

    @org.junit.Test
    public void tickNullGame() throws Exception {
        e.game = null;
        assertTrue(e.tick() == null);
    }

    // TODO: Eventually be able to load up a valid game...
    @org.junit.Test(expected = NullPointerException.class)
    public void tickGame() throws Exception {
        e.game = new Game(null);
        assertTrue(e.tick() != null);
    }
}
