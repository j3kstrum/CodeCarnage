/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package scripting.access;

import common.data.GameMap;

/**
 * Class designed to be a listener for other modules requiring scripting data.
 * This class will return the data asked.
 *
 * @author: jacob.ekstrum
 */
public class ScriptingAccess {

    /**
     * Performs the required calculations to transform the current turn into the next turn.
     *
     * @param currentTurn The GameMap representing the current turn.
     * @return The GameMap representing the next turn.
     */
    public static GameMap nextTurn(GameMap currentTurn) {
        // TODO: Perform scripting parsing and algorithms and return the updated map.
        return null;
    }

}
