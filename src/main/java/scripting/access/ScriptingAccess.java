package main.java.scripting.access;

import main.java.common.data.GameMap;

/**
 * Class designed to be a listener for other modules requiring scripting data.
 * This class will return the data asked.
 *
 * @author: jacob.ekstrum
 */
public class ScriptingAccess {

    /**
     * Performs the required calculations to transform the current turn into the next turn.
     * @param currentTurn The GameMap representing the current turn.
     * @return The GameMap representing the next turn.
     */
    public static GameMap nextTurn(GameMap currentTurn) {
        // TODO: Perform scripting parsing and algorithms and return the updated map.
        return null;
    }

}
