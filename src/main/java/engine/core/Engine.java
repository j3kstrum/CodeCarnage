/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package engine.core;

import common.BaseLogger;
import common.data.GameMap;
import engine.data.EngineData;
import gui.game.GameGUI;
import org.mapeditor.core.Map;
import org.mapeditor.io.TMXMapReader;
import utilties.models.EntityMap;
import utilties.models.Game;

import java.net.URL;

/**
 * The main class for the main.java.engine.
 * This class will be initialized and will be responsible for game timing, acting as a game pipeline, etc.
 * <p>
 * Created by jacob.ekstrum on 9/11/17.
 */
public class Engine {

    // Hard shutdown boolean. Causes main loop to terminate with no chance for recovery.
    private boolean _shutdown = false;

    // The Engine's Database.
    private final EngineData DATA;

    // The amount of time for each game tick.
    public static final long TICK_TIME = 250;
    private boolean _inCoreGame = false;

    private static final BaseLogger ENGINE_LOGGER = new BaseLogger("Engine");

    public Map map;
    public Game game;
    public GameGUI gameGUI;

    /**
     * Initializes the Engine and performs the main ticking loop.
     */
    public Engine(GameGUI gameGUI) {
        this.gameGUI = gameGUI;
        DATA = new EngineData();
        try {
            this.DATA.setMap(loadGameMap());
        } catch (NullPointerException ne) {
            ENGINE_LOGGER.fatal("COULD NOT LOAD GAME MAP.");
        }
        ENGINE_LOGGER.info("Engine initialized. Beginning tick loop...");
    }

    /**
     * Loads the Tiled static EntityMap from disk.
     *
     * @return The initial GameMap, initialized to hold the static Tiled EntityMap.
     */
    private GameMap loadGameMap() {
        TMXMapReader tmr = new TMXMapReader();
        GameMap mp = null;
        try {
            TMXMapReader mapReader = new TMXMapReader();
            URL mapPath = getClass().getResource("/game-map.tmx");
            try {
                this.map = mapReader.readMap(mapPath.toString());
            } catch (Exception ex) {
                ENGINE_LOGGER.warning("Could not load game map. Attempting to use *nix filepaths.");
                mapPath = getClass().getResource("/nix/game-map.tmx");
                try {
                    this.map = mapReader.readMap(mapPath.toString());
                } catch (Exception ex2) {
                    ENGINE_LOGGER.fatal(ex2.getMessage());
                    System.exit(2);
                }
            }

            EntityMap entityMap = new EntityMap(map, 25, 15);
            game = new Game(entityMap);
            this.gameGUI._map = map;

        } catch (Exception e) {
            ENGINE_LOGGER.fatal("Could not load game map.");
            // TODO: Don't just kill the program here. Download the game map that we know works from
            // GitHub if we can get a connection, and then use that map instead.
            ENGINE_LOGGER.fatal(e.getMessage());
            System.exit(1);
        }

        return mp;
    }

    /**
     *
     * @return True if the engine is running, False otherwise.
     */
    public boolean isRunning() {
        return !this._shutdown;
    }

    /**
     * BORROWED FROM Tiled MapEditor test code. Loads a filename from the project's resources.
     *
     * @param filename The relative path to be loaded for resources.
     * @return The URL representing the full filepath to the desired resource.
     */
    private URL getUrlFromResources(String filename) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        return classLoader.getResource(filename);
    }

    /**
     * Wrapper for shutdown(boolean now) that uses default argument now=false.
     *
     * @return The value returned by shutting down with now as false.
     */
    public boolean shutdown() {
        return this.shutdown(false);
    }

    /**
     * Safe shutdown handler for the engine that can be called from external functions.
     *
     * @param now Whether to immediately terminate the program at the end of the tick or to wait.
     * @return true if successful, false if shutdown failed.
     */
    public boolean shutdown(boolean now) {
        System.out.println("Shutting down...");
        if (now) {
            this._shutdown = true;
        } else {
            ENGINE_LOGGER.warning("Currently can not shutdown engine with delay. " +
                    "Terminating immediately following current tick.");
            this._shutdown = true;
        }
        return true;
    }

    /**
     * Ensures that the proper amount of time since the last tick has elapsed.
     * If not, sleeps until this condition is met.
     *
     * @param lastTick The system time polled at the last tick.
     * @return The system time at the end of the method, which is the start of the next tick.
     */
    public long performWait(long lastTick) {
        long delta = System.currentTimeMillis() - lastTick;
        if (delta > Engine.TICK_TIME) {
            ENGINE_LOGGER.critical("System too slow for tick. Elapsed delta: " + String.valueOf(delta) + ".");
        } else {
            try {
                Thread.sleep(Engine.TICK_TIME - delta);
            } catch (InterruptedException ie) {
                ENGINE_LOGGER.critical("Tick sleep interrupted!");
            }
        }
        return System.currentTimeMillis();
    }

    /**
     * Performs all of the necessary game logic for one tick, or turn, if currently in-game.
     */
    public Map tick() {

        if (game == null) {
            ENGINE_LOGGER.critical("Game was null. Returning null.");
            return null;
        }
        if (game.getNumberOfTurnsCompleted() > 30) {
            this.shutdown();
        }
        game.moveCloserTo(0,1);
        game.moveCloserTo(1, 0);
        return game.nextTurn();
    }

    /**
     * Performs code cleanup.
     *
     * @return true if successful, false otherwise.
     */
    public boolean cleanup() {
        // TODO: Populate
        ENGINE_LOGGER.warning("Currently have no engine cleanup code.");
        return true;
    }

    /**
     * Starts the game from the engine's perspective.
     * NOTE: This is a *hard* game start; that is, the battle is occurring here.
     */
    public void startGame() {
        this._inCoreGame = true;
    }

    /**
     * Stops the core battle from the engine's perspective.
     */
    public void stopGame() {
        this._inCoreGame = false;
    }

}
