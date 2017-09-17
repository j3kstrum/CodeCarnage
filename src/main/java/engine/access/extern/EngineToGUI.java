package main.java.engine.access.extern;

import main.java.common.BaseLogger;
import main.java.gui.access.GUIAccess;


/**
 * Class that, from the Engine's side, updates the GUI.
 * It will be used, not instantiated, to send results to the GUI through
 * it's GUIAccess "Socket."
 *
 * @author jacob.ekstrum
 */
public class EngineToGUI {

    private static BaseLogger LOGGER = new BaseLogger("EngineToGUI");
    private static final int DEFAULT_RETRIES = 2;

    /**
     * Updates the GUI by connecting to the socket and requesting it to update.
     *
     * Ideally, if possible, this will force the GUI to read all information that it needs
     * from the Engine and its EngineData without requiring any explicit parameters.
     */
    public static void update() {
        boolean success = GUIAccess.update();
        // Iterates while we haven't done too many retries (haven't given up yet)
        // and we are still not successful in updating.
        for (int num_retries = DEFAULT_RETRIES; !success && num_retries > 0; num_retries--) {
            LOGGER.critical("Engine could not successfully update GUI.");
            success = GUIAccess.update();
        }
    }

}
