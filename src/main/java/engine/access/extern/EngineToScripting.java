package main.java.engine.access.extern;

import main.java.common.BaseLogger;
import main.java.common.data.GameMap;
import main.java.scripting.access.ScriptingAccess;

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
     * @param currentTurn The GameMap representing the current state of the game at this turn.
     * @return The GameMap object resembling the next turn.
     */
    public static GameMap nextTurn(GameMap currentTurn) {
        LOGGER.info("Acquiring next game turn...");
        return ScriptingAccess.nextTurn(currentTurn);
    }

}
