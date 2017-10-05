/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package engine.core;

import static org.junit.Assert.assertTrue;

public class EngineTest {

    private int two;
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

    @org.junit.Test
    public void onePlusOne() throws Exception {
        two = 1 + 1;
        assertTrue(two == 2);
    }
}
