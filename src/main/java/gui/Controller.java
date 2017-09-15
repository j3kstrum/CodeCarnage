package main.java.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class Controller {
    @FXML
    ToggleGroup toggleGroup;

    /*
    Click event handler for buttonGo on menu
     */
    public void click(ActionEvent action) {
        RadioButton checked = (RadioButton) toggleGroup.getSelectedToggle();

        if (checked.getText().equals("Play")) {
            //System.out.println("You selected: Play");
        } else if (checked.getText().equals("Settings")) {
            //System.out.println("You selected: Settings");
        }
    }
}
