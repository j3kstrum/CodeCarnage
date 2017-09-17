package main.java.engine.data;

import main.java.common.BaseLogger;
import main.java.common.data.GameMap;

/**
 * The class containing the Engine's Data. It handles all lower-level data objects pertaining to the engine.
 *
 * @author jacob.ekstrum
 */
public class EngineData {

    private GameMap _map;
    private static final BaseLogger LOGGER = new BaseLogger("EngineData");

    /**
     * Initializes the game's data, including the game map.
     */
    public EngineData() {
        this._map = new GameMap();
        LOGGER.info("Initialized EngineData.");
    }

    /**
     * Sets the game map to the specified map.
     * @param toMap The game map that should be used as the new game map.
     */
    public void setMap(GameMap toMap) {
        // TODO: Some error checking, possibly?
        this._map = toMap;
    }

    /**
     * Gets the current game map being used.
     * @return The game map that is currently in play.
     */
    public GameMap getMap() {
        // TODO: Some error checking, possibly?
        return this._map;
    }

}
