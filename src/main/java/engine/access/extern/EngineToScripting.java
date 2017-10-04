/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package engine.access.extern;

import common.BaseLogger;
import common.data.GameMap;
import scripting.access.ScriptingAccess;

/**
 * Class that interfaces between the Engine and the Scripting framework. In particular, this
 * class is used by the Engine to ask the Scripting framework for information.
 *
 * @author jacob.ekstrum
 */
public class EngineToScripting {

    private static final BaseLogger LOGGER = new BaseLogger("EngineToScripting");

    /**
     * Asks the scripting framework for the next turn's game map.
     * This will be loaded into the engine and then used by the GUI.
     *
     * @param currentTurn The GameMap representing the current state of the game at this turn.
     * @return The GameMap object resembling the next turn.
     */
    public static GameMap nextTurn(GameMap currentTurn) {
        LOGGER.info("Acquiring next game turn...");
        return ScriptingAccess.nextTurn(currentTurn);
    }

}
