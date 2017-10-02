package gui.scripting;

import com.jfoenix.controls.JFXButton;
import interpreter.BehaviorList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;

public class ScriptButton extends JFXButton {
    private static final PseudoClass
            SCRIPT_BUTTON_PSEUDO_CLASS = PseudoClass.getPseudoClass("scriptButton");
    @FXML
    BehaviorList behaviorList;

    public ScriptButton() {
        super();
        this.pseudoClassStateChanged(SCRIPT_BUTTON_PSEUDO_CLASS, true);
    }

    public ScriptButton(String text) {
        super(text);
        this.pseudoClassStateChanged(SCRIPT_BUTTON_PSEUDO_CLASS, true);

        this.setOnMouseClicked(event -> {
            // remove the button from the script and leave a blank button
        });
    }

}
