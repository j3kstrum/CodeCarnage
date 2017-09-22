package gui.menu;


import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class Controller {
    // To be populated once GUI is set up properly.

    public void click(ActionEvent event) {
        String id = ((Button) event.getSource()).getText();

        if (id.equals("Start Game")) {
            // close current window
            // open game window
        } else if (id.equals("Settings")) {
            // open game settings
        }
    }

}
