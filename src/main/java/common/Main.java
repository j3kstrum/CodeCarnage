/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package common;

import engine.core.Engine;
import gui.menu.MenuGUI;

/**
 * Class responsible for initializing all core modules of the project.
 * Each module will then be provided references to the other modules, which can be accessed as needed
 * to acquire data.
 * <p>
 * Created by jacob.ekstrum on 9/11/17.
 */
public class Main {

    private static final BaseLogger LOGGER = new BaseLogger("Main");

    /**
     * MenuGUI method for the project. Responsible for initializing all core modules.
     *
     * @param args The system's command line arguments. This should be empty.
     */
    public static void main(String[] args) {
        new MenuGUI(args);
        // EGN-MARKER
        LOGGER.info("Main method complete.");
    }

}
