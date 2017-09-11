package common;
import engine.core.Engine;

/**
 * Class responsible for initializing all core modules of the project.
 * Each module will then be provided references to the other modules, which can be accessed as needed
 * to acquire data.
 *
 * Created by jacob.ekstrum on 9/11/17.
 */
public class Main {

    private static Engine _ENGINE;

    /**
     * Main method for the project. Responsible for initializing all core modules.
     * @param args The system's command line arguments. This should be empty.
     */
    public static void main(int[] args) {
        _ENGINE = new Engine();
    }

}
