package gui.scripting;

import com.jfoenix.controls.JFXButton;
import interpreter.BehaviorList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;

public class ChoiceButton extends JFXButton {
    private static final PseudoClass
            CHOICE_BUTTON_PSEUDO_CLASS = PseudoClass.getPseudoClass("choiceButton");

    @FXML
    private BehaviorList behaviorList;

    public ChoiceButton() {
        super();
        this.pseudoClassStateChanged(CHOICE_BUTTON_PSEUDO_CLASS, true);
    }

    public ChoiceButton(String text) {
        super(text);
        this.pseudoClassStateChanged(CHOICE_BUTTON_PSEUDO_CLASS, true);
    }


}
