/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package gui.access;

/**
 * MenuGUI Access class. Allows for the engine to interface with the MenuGUI.
 * This acts like a RECEIVER socket for the MenuGUI. Updates are passed to
 * this class in a format that it understands.
 *
 * @author jacob.ekstrum
 */
public class GUIAccess {

    /**
     * Updates the MenuGUI. This is a placeholder class for now. In the future, other methods will exist
     * that will update aspects of the MenuGUI, or this method will contain a "GUIUpdate" object as a parameter.
     *
     * @return true if successful, false otherwise.
     */
    public static boolean update() {
        // Default functionality.
        return true;
    }

}
