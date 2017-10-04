package gui.scripting;

import com.jfoenix.controls.JFXButton;
import javafx.css.PseudoClass;

public class ScriptButton extends JFXButton {

    /**
     * PseudoClass that refers to the scriptButton css class
     */
    private static final PseudoClass
            SCRIPT_BUTTON_PSEUDO_CLASS = PseudoClass.getPseudoClass("scriptButton");

    /**
     * JFXButton with a predefined css class for style and type inference
     */
    public ScriptButton() {
        super();
        this.pseudoClassStateChanged(SCRIPT_BUTTON_PSEUDO_CLASS, true);
    }

    /**
     * JFXButton with a predefined css class for style and type inference
     *
     * @param text String that will appear on the button object
     */
    public ScriptButton(String text) {
        super(text);
        this.pseudoClassStateChanged(SCRIPT_BUTTON_PSEUDO_CLASS, true);

        this.setOnMouseClicked(event -> {
            // remove the button from the script and leave a blank button
        });
    }

}
