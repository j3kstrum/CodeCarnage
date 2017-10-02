package gui.scripting;

import com.jfoenix.controls.JFXButton;
import gui.game.GameGUI;
import interpreter.Behavior;
import interpreter.BehaviorList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ScriptingController {

    @FXML
    private BehaviorList behaviorList;
    EventHandler choiceButtonClick = new EventHandler() {
        @Override
        public void handle(Event event) {
            try {
                Behavior behavior = (Behavior) behaviorList.getToggleGroup().getSelectedToggle();
                if (behavior.isSelected()) {
                    ChoiceButton currentButton = (ChoiceButton) event.getTarget();

                    ScriptButton buttonToAdd = new ScriptButton(currentButton.getText());

                    buttonToAdd.setStyle(currentButton.getStyle());

                    behavior.getChildren().add(buttonToAdd);
                    // get last button, check type etc.

                    // Last Child.
                    //                ScriptButton previousInstruction = (ScriptButton) behavior.getChildren().get(behavior.getChildren().size()-1);
                    //                String instructionText = previousInstruction.getText();

                } else {
                    showNoneSelected();
                }
            } catch (Exception ex) {
                showNoneSelected();
            }
        }
    };
    @FXML
    private AnchorPane choicesPane;
    @FXML
    private JFXButton add, subtract, submit;

    public ScriptingController() {

    }

    public BehaviorList getBehaviorList() {
        return behaviorList;
    }

    @FXML
    private void initialize() {

        for (Node component : choicesPane.getChildren()) {
            if (component instanceof VBox) {
                for (Node subComponent : ((VBox) component).getChildren()) {
                    if (subComponent instanceof ChoiceButton) {
                        ((ChoiceButton) subComponent).setOnAction(choiceButtonClick);
                    }
                }
            }
        }

        add.setOnAction((ActionEvent event) -> {
            Behavior behavior = new Behavior();
            behavior.getStyleClass().add("behavior");
            behavior.setToggleGroup(behaviorList.getToggleGroup());
            behavior.setOnMouseClicked((mouseEvent) -> {
                if (behavior.isSelected()) {
                    System.out.println("Unselected!");
                    behavior.setSelected(false);
                } else {
                    System.out.println("Selected!");
                    behavior.setSelected(true);
                }
            });

            behaviorList.getChildren().add(behavior);
        });

        subtract.setOnAction((ActionEvent event) -> {
            try {
                Behavior behavior = (Behavior) behaviorList.getToggleGroup().getSelectedToggle();
                if (behavior.isSelected()) {
                    behaviorList.getChildren().remove(behavior);
                }
            } catch (Exception ex) {
                //  Do Nothing
            }
        });

        submit.setOnAction((ActionEvent event) -> {
            System.out.println("You clicked Submit!");

            try {
                new GameGUI();
            } catch (Exception e) {
                e.printStackTrace();
            }

            submit.getScene().getWindow().hide();
        });
    }

    private void showNoneSelected() {
        // Nothing is selected, show prompt
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("No Behavior Selected");
        alert.setContentText("Please select a behavior from your list of behaviors prior to selecting a word " +
                "from the word bank. If you have not yet created a behavior, select the green 'plus' button " +
                "to do so.");
        alert.showAndWait();
    }

}
