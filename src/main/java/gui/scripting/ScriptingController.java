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
import javafx.scene.control.Labeled;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScriptingController {

    @FXML
    private BehaviorList behaviorList;

    @FXML
    private AnchorPane choicesPane;
    @FXML
    private JFXButton add, subtract, submit;

    @FXML
    private VBox conditionals, data, operators, commands;

    private ArrayList<ChoiceButton> conditionalList, dataList, operatorList, commandList;

    /**
     * Handles click events for choice buttons.  Attempts to add the selected item to the selected behavior
     * in the list.
     */
    private EventHandler choiceButtonClick = new EventHandler() {
        /**
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

                    List<ScriptButton> currentScript =
                            behavior.getChildren().stream()
                                    .filter(b -> b instanceof ScriptButton)
                                    .map(b -> (ScriptButton) b)
                                    .collect(Collectors.toList());

                    List<String> allowables = getAllowableText(currentScript);

                    enableButtons(allowables);
                } else {
                    alertNoneSelected();
                }
            } catch (Exception ex) {
                alertNoneSelected();
            }
        }
    };

    /**
     * Get all button text that is valid at current state
     *
     * @param currentScript Script buttons that are active in the current Behavior
     * @return Returns a list of all Strings that are valid adds to the current Behavior
     */
    private List<String> getAllowableText(List<ScriptButton> currentScript) {
        if (currentScript.isEmpty()) {
            // enforce 'if'
            return (Collections.singletonList("If"));
        }

        ScriptButton lastButton = currentScript.get(currentScript.size() - 1);
        String lastButtonText = lastButton.getText().trim();

        // enforce data
        if (lastButtonText.equals("If") || lastButtonText.equals("And") ||
                operatorList.stream().map(Labeled::getText).collect(Collectors.toList()).contains(lastButtonText)) {
            return dataList.stream().map(Labeled::getText).collect(Collectors.toList());
        }
        // enforce operator
        else if (dataList.stream().map(Labeled::getText).collect(Collectors.toList()).contains(lastButtonText) &&
                (currentScript.size() % 4) - 2 == 0) {
            return operatorList.stream().map(Labeled::getText).collect(Collectors.toList());
        }
        // enforce and or then
        else if (dataList.stream().map(Labeled::getText).collect(Collectors.toList()).contains(lastButtonText)) {
            return Arrays.asList("And", "Then");
        }
        // enforce command
        else if (lastButtonText.equals("Then")) {
            return commandList.stream().map(Labeled::getText).collect(Collectors.toList());
        }
        // disable all
        else {
            return Collections.emptyList();
        }
    }

    public List<ChoiceButton> getConditionalList() {
        return conditionals.getChildren().stream()
                .filter(node -> node instanceof ChoiceButton)
                .map(node -> (ChoiceButton) node)
                .collect(Collectors.toList());
    }

    public List<ChoiceButton> getDataList() {
        return data.getChildren().stream()
                .filter(node -> node instanceof ChoiceButton)
                .map(node -> (ChoiceButton) node)
                .collect(Collectors.toList());
    }

    public List<ChoiceButton> getOperatorList() {
        return operators.getChildren().stream()
                .filter(node -> node instanceof ChoiceButton)
                .map(node -> (ChoiceButton) node)
                .collect(Collectors.toList());
    }

    public List<ChoiceButton> getCommandList() {
        return commands.getChildren().stream()
                .filter(node -> node instanceof ChoiceButton)
                .map(node -> (ChoiceButton) node)
                .collect(Collectors.toList());
    }

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
        this.conditionalList = new ArrayList<>(getConditionalList());
        this.commandList = new ArrayList<>(getCommandList());
        this.dataList = new ArrayList<>(getDataList());
        this.operatorList = new ArrayList<>(getOperatorList());

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

                List<ScriptButton> currentScript =
                        behavior.getChildren().stream()
                                .filter(b -> b instanceof ScriptButton)
                                .map(b -> (ScriptButton) b)
                                .collect(Collectors.toList());

                List<String> allowables = getAllowableText(currentScript);

                enableButtons(allowables);

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
                    alertNoneSelected();
                }
            } catch (Exception ex) {
                //  Do Nothing
                alertNoneSelected();
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
    private void alertNoneSelected() {
        // Nothing is selected, show prompt
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("No Behavior Selected");
        alert.setHeaderText("No Behavior Selected");
        alert.setContentText("Please select a behavior from your list of behaviors prior to performing an action." +
                " If you have not yet created a behavior, select the green 'plus' button " +
                "to do so.");
        alert.showAndWait();
    }

    /**
     * @param behavior Behavior to be checked for completeness
     * @return Returns true if behavior is well formed
     */
    private boolean isCompleteBehavior(Behavior behavior) {
        ScriptButton lastButton = (ScriptButton) behavior.getChildren().get(behavior.getChildren().size() - 1);

        // We can do this since validity is enforced along the way
        return commandList.stream().map(Labeled::getText).collect(Collectors.toList()).contains(lastButton.getText());
    }

    /**
     * Enable all buttons in validButtons, disable the rest that appear in the choicesPane
     *
     * @param validText List of valid Strings at current point
     */
    private void enableButtons(List<String> validText) {

        List<ChoiceButton> allButtons = Stream.of(conditionalList, commandList, operatorList, dataList)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        allButtons.forEach(b -> {
            if (validText.contains(b.getText().trim())) {
                b.setDisable(false);
            } else {
                b.setDisable(true);
            }
        });
    }

}
