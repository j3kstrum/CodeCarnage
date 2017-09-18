package gui.access;

/**
 * GUI Access class. Allows for the engine to interface with the GUI.
 * This acts like a RECEIVER socket for the GUI. Updates are passed to
 * this class in a format that it understands.
 *
 * @author jacob.ekstrum
 */
public class GUIAccess {

    /**
     * Updates the GUI. This is a placeholder class for now. In the future, other methods will exist
     * that will update aspects of the GUI, or this method will contain a "GUIUpdate" object as a parameter.
     * @return true if successful, false otherwise.
     */
    public static boolean update() {
        // Default functionality.
        return true;
    }

}
