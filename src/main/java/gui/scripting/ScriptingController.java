/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package gui.scripting;

import com.jfoenix.controls.JFXButton;
import gui.game.GameGUI;
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

    @FXML
    private AnchorPane choicesPane;
    @FXML
    private JFXButton add, subtract, submit;

    private EventHandler choiceButtonClick = new EventHandler() {
        /**
         * Handles click events for choice buttons.  Attempts to add the selected item to the selected behavior
         * in the list.
         * @param event Click event to be handled
         */
        @Override
        public void handle(Event event) {
            try {
                // Get the last selected behavior from the list
                Behavior behavior = (Behavior) behaviorList.getToggleGroup().getSelectedToggle();
                if (behavior.isSelected()) {
                    ChoiceButton currentButton = (ChoiceButton) event.getTarget();

                    ScriptButton buttonToAdd = new ScriptButton(currentButton.getText());

                    // Copy the style of the clicked button to the newly generated ScriptButton
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

    public BehaviorList getBehaviorList() {
        return behaviorList;
    }

    public ScriptingController() {
    }

    /**
     * Set all event handlers upon initializing ScriptingGUI
     */
    @FXML
    private void initialize() {

        // Assign each ChoiceButton in choicesPane the choiceButtonClick event handler
        for (Node component : choicesPane.getChildren()) {
            if (component instanceof VBox) {
                for (Node subComponent : ((VBox) component).getChildren()) {
                    if (subComponent instanceof ChoiceButton) {
                        ((ChoiceButton) subComponent).setOnAction(choiceButtonClick);
                    }
                }
            }
        }

        // Assign add button an action to create a new behavior in the list when clicked
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

        // Assign subtract button an action to remove the selected behavior when clicked
        subtract.setOnAction((ActionEvent event) -> {
            try {
                Behavior behavior = (Behavior) behaviorList.getToggleGroup().getSelectedToggle();
                if (behavior.isSelected()) {
                    behaviorList.getChildren().remove(behavior);
                } else {
                    showNoneSelected();
                }
            } catch (Exception ex) {
                //  Do Nothing
                showNoneSelected();
            }
        });

        // Assign submit button an action to instantiate the GameGUI, as well as to pass all necessary scripting objects
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

    /**
     * Shows an informational alert stating that the selected action could not be completed since no behavior has been
     * selected
     */
    private void showNoneSelected() {
        // Nothing is selected, show prompt
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("No Behavior Selected");
        alert.setHeaderText("No Behavior Selected");
        alert.setContentText("Please select a behavior from your list of behaviors prior to performing an action." +
                " If you have not yet created a behavior, select the green 'plus' button " +
                "to do so.");
        alert.showAndWait();
    }

}
