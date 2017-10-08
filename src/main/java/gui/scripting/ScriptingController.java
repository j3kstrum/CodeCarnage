/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package gui.scripting;

import com.jfoenix.controls.JFXButton;
import gui.game.GameGUI;
import interpreter.ScriptCommand;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScriptingController {

    //region FXML Nodes
    @FXML
    private BehaviorList behaviorList;

    @FXML
    private AnchorPane choicesPane;
    @FXML
    private JFXButton add, subtract, submit, deleteWord;

    @FXML
    private VBox conditionals, data, operators, commands;
    //endregion

    private MouseEvents mouseEventHandler;

    /**
     * Get all of the choice button Nodes
     */
    private List<Node> getAllButtons() {
        return Stream.of(conditionals.getChildren(),
                commands.getChildren(),
                operators.getChildren(),
                data.getChildren())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public ScriptingController() {
    }

    /**
     * Set all event handlers upon initializing ScriptingGUI
     */
    @FXML
    private void initialize() {
        this.mouseEventHandler = new MouseEvents(behaviorList, getAllButtons());

        // Assign each ChoiceButton in choicesPane the choiceButtonClick event handler
        for (Node component : choicesPane.getChildren()) {
            if (component instanceof VBox) {
                for (Node subComponent : ((VBox) component).getChildren()) {
                    if (subComponent instanceof ChoiceButton) {
                        ((ChoiceButton) subComponent).setOnAction(this.mouseEventHandler.choiceButtonClick);
                    }
                }
            }
        }

        // Assign add button an action to create a new behavior in the list when clicked
        add.setOnAction((ActionEvent event) -> {
            List<Node> allBehaviors = behaviorList.getChildren().filtered(n -> n instanceof Behavior);

            // Check if all behaviors in the list are complete
            if (this.mouseEventHandler.canAddBehaviors(allBehaviors)) {

                Behavior behavior = new Behavior();
                behavior.getStyleClass().add("behavior");
                behavior.setToggleGroup(behaviorList.getToggleGroup());
                behavior.setOnMouseClicked((mouseEvent) -> {
                    behavior.setSelected(behavior.isSelected());

                    List<ScriptButton> currentScript =
                            behavior.getChildren().stream()
                                    .filter(b -> b instanceof ScriptButton)
                                    .map(b -> (ScriptButton) b)
                                    .collect(Collectors.toList());

                    List<String> allowables = MouseEvents.getAllowableText(currentScript);

                    mouseEventHandler.enableButtons(allowables);


                });

                behaviorList.getChildren().add(behavior);
            } else {
                // Incomplete behaviors exist, show prompt
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Unfinished Behavior!");
                alert.setHeaderText("You have incomplete scripts.");
                alert.setContentText("You must complete all scripts in your behavior list prior to adding another!");
                alert.showAndWait();
            }
        });

        // Assign subtract button an action to remove the selected behavior when clicked
        subtract.setOnAction((ActionEvent event) -> {
            try {
                Behavior behavior = (Behavior) behaviorList.getToggleGroup().getSelectedToggle();
                if (behavior.isSelected()) {
                    behaviorList.getChildren().remove(behavior);
                } else {
                    this.mouseEventHandler.alertNoneSelected();
                }
            } catch (Exception ex) {
                //  Do Nothing
                this.mouseEventHandler.alertNoneSelected();
            }
        });

        // Add Event listener to deleteWord button to delete last word in the selected behavior
        deleteWord.setOnAction(this.mouseEventHandler.removeLastWordClick);

        // Assign submit button an action to instantiate the GameGUI, as well as to pass all necessary scripting objects
        submit.setOnAction((ActionEvent event) -> {
//            System.out.println("You clicked Submit!");

            try {
                ArrayList<ScriptCommand> commandObjects = new ArrayList<>(mouseEventHandler.getCommands());
                // commandObjects should now be sent to the interpreter for processing...

                new GameGUI(commandObjects);
            } catch (Exception e) {
                e.printStackTrace();
            }

            submit.getScene().getWindow().hide();
        });
    }

}
