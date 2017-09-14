package main.java.common;

import main.java.engine.core.Engine;
import main.java.gui.GUI;

/**
 * Class responsible for initializing all core modules of the project.
 * Each module will then be provided references to the other modules, which can be accessed as needed
 * to acquire data.
 *
 * Created by jacob.ekstrum on 9/11/17.
 */
public class Main {

    private static Engine _ENGINE;
    private static GUI _GUI;

    /**
     * GUI method for the project. Responsible for initializing all core modules.
     * @param args The system's command line arguments. This should be empty.
     */
    public static void main(String[] args) {
        _ENGINE = new Engine();
        _GUI = new GUI(args);
    }

}
