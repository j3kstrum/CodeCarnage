package engine.core;

/**
 * The main class for the engine.
 * This class will be initialized and will be responsible for game timing, acting as a game pipeline, etc.
 *
 * Created by jacob.ekstrum on 9/11/17.
 */
public class Engine {

    // Hard shutdown boolean. Causes main loop to terminate with no chance for recovery.
    private static boolean _SHUTDOWN = false;

    public Engine() {
        while (!_SHUTDOWN) {
            System.out.println("Init");
            break;
        }
        if (!this.cleanup()) {
            System.err.println("SERVER ERROR: Cleanup failed!");
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
