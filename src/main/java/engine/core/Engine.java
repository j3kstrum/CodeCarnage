package main.java.engine.core;

import main.java.common.BaseLogger;

/**
 * The main class for the main.java.engine.
 * This class will be initialized and will be responsible for game timing, acting as a game pipeline, etc.
 *
 * Created by jacob.ekstrum on 9/11/17.
 */
public class Engine {

    // Hard shutdown boolean. Causes main loop to terminate with no chance for recovery.
    private static boolean _SHUTDOWN = false;

    private static final BaseLogger _ENGINE_LOGGER = new BaseLogger("Engine");

    public Engine() {
        _ENGINE_LOGGER.info("Engine logger created.");
        while (!_SHUTDOWN) {
            break;
        }
        if (!this.cleanup()) {
            _ENGINE_LOGGER.critical("Engine cleanup failed.");
        }
    }

    /**
     * Performs code cleanup.
     * @return true if successful, false otherwise.
     */
    private boolean cleanup(){
        // TODO: Populate
        return true;
    }

    // TODO: Populate
}
