package gui.scripting;

import com.jfoenix.controls.JFXButton;
import javafx.css.PseudoClass;

public class ChoiceButton extends JFXButton {

    /**
     * PseudoClass that refers to the choiceButton css class
     */
    private static final PseudoClass
            CHOICE_BUTTON_PSEUDO_CLASS = PseudoClass.getPseudoClass("choiceButton");

    /**
     * JFXButton with a predefined css class for style and type inference
     */
    public ChoiceButton() {
        super();
        this.pseudoClassStateChanged(CHOICE_BUTTON_PSEUDO_CLASS, true);
    }

    /**
     * JFXButton with a predefined css class for style and type inference
     *
     * @param text String that will appear on the button object
     */
    public ChoiceButton(String text) {
        super(text);
        this.pseudoClassStateChanged(CHOICE_BUTTON_PSEUDO_CLASS, true);
    }


}
