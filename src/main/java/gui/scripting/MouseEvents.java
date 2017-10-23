package gui.scripting;

import interpreter.Check;
import interpreter.ScriptCommand;
import interpreter.enumerations.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import javax.management.InstanceNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

final class MouseEvents {
    private BehaviorList behaviorList;
    private List<Node> allButtons;
    /**
     * Handles click events for choice buttons.  Attempts to add the selected item to the selected behavior
     * in the list.
     */
    EventHandler<ActionEvent> choiceButtonClick = new EventHandler<ActionEvent>() {
        /**
         * @param event Click event to be handled
         */
        @Override
        public void handle(ActionEvent event) {
            try {
                // Get the last selected behavior from the list
                Behavior behavior = (Behavior) behaviorList.getToggleGroup().getSelectedToggle();

                if (behavior.isSelected()) {
                    ChoiceButton currentButton = (ChoiceButton) event.getTarget();

                    ScriptButton buttonToAdd = new ScriptButton();

                    if (currentButton.getText().equals(Data.USER_INPUT.text())) {
                        // create alert to enter an integer
                        TextInputDialog dialog = new TextInputDialog("0");
                        dialog.setHeaderText("Please enter a number between 0 and 2,000,000,000");
                        dialog.setContentText("User Input: ");

                        boolean isInt = false;

                        while (!isInt) {
                            Optional<String> result = dialog.showAndWait();
                            if (result.isPresent()) {
                                try {
                                    int input = Integer.parseInt(result.get().trim());
                                    if (input < 0 || input > 2000000000) {
                                        continue;
                                    }
                                    isInt = true;
                                    buttonToAdd = new ScriptButton(result.get().trim());
                                } catch (Exception ex) {
                                    // continue
                                }
                            } else {
                                return;
                            }
                        }
                    } else {
                        buttonToAdd = new ScriptButton(currentButton.getText());
                    }

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
     * Handles click events for the deleteWord button.  Removes the last word in the selected behavior
     */
    EventHandler<ActionEvent> removeLastWordClick = new EventHandler<ActionEvent>() {
        /**
         * @param event Click event to be handled
         */
        @Override
        public void handle(ActionEvent event) {
            // Remove the last word in the selected behavior
            try {
                Behavior behavior = (Behavior) behaviorList.getToggleGroup().getSelectedToggle();

                if (behavior.isSelected() && behaviorList.getChildren().contains(behavior)) {
                    ScriptButton lastButton = (ScriptButton) behavior.getChildren().get(behavior.getChildren().size() - 1);

                    behavior.getChildren().remove(lastButton);

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

    MouseEvents(BehaviorList behaviorList, List<Node> allButtons) {
        this.behaviorList = behaviorList;
        this.allButtons = allButtons;
    }

    //region Static Methods

    /**
     * Get all button text that is valid at current state
     *
     * @param currentScript Script buttons that are active in the current Behavior
     * @return Returns a list of all Strings that are valid adds to the current Behavior
     */
    static List<String> getAllowableText(List<ScriptButton> currentScript) {
        if (currentScript.isEmpty()) {
            // enforce 'if'
            return (Collections.singletonList("If"));
        }

        ScriptButton lastButton = currentScript.get(currentScript.size() - 1);
        String lastButtonText = lastButton.getText().trim();

        boolean isInteger;

        try {
            int value = Integer.parseInt(lastButtonText.trim());
            isInteger = true;
        } catch (Exception ex) {
            isInteger = false;
        }

        // enforce data
        if (ScriptingTypes.OPERATOR.list().contains(lastButtonText) || lastButtonText.equals(Conditional.IF.text()) || lastButtonText.equals(Conditional.AND.text())) {
            return ScriptingTypes.DATA.list();
        }
        // enforce operator
        else if ((ScriptingTypes.DATA.list().contains(lastButtonText) || (isInteger)) && (currentScript.size() % 4) - 2 == 0) {
            return ScriptingTypes.OPERATOR.list();
        }
        // enforce and or then
        else if (ScriptingTypes.DATA.list().contains(lastButtonText) || (isInteger)) {
            return Arrays.asList(Conditional.AND.text(), Conditional.THEN.text());
        }
        // enforce command
        else if (lastButtonText.equals(Conditional.THEN.text())) {
            return ScriptingTypes.COMMAND.list();
        }
        // disable all
        else {
            return Collections.emptyList();
        }
    }

    /**
     * Builds the set of Check objects from the present enumerators
     *
     * @param scriptingObjects List of Scripting Enumerators to be parsed
     * @return Returns all check objects extracted from scriptingObjects
     */
    private static ArrayList<Check> constructChecks(ArrayList<String> scriptingObjects) {
        ArrayList<Check> checks = new ArrayList<>();
        for (int i = 0; i < scriptingObjects.size(); i++) {
            String object = scriptingObjects.get(i);

            if (ScriptingTypes.DATA.list().contains(object) || object.matches("\\d+")) {
                Operator op;

                try {
                    op = getOperator(scriptingObjects.get(i + 1));
                } catch (InstanceNotFoundException e) {
                    e.printStackTrace();
                    // Should never happen
                    return null;
                }

                Check check = new Check(scriptingObjects.get(i),
                        scriptingObjects.get(i + 2),
                        op);
                checks.add(check);

                // increment to end of the check that was located
                i = i + 2;
            }
        }
        return checks;
    }

    private static Operator getOperator(String text) throws InstanceNotFoundException {
        for (Operator operator : Operator.values()) {
            if (text.trim().equals(operator.text())) {
                return operator;
            }
        }
        throw new InstanceNotFoundException();
    }

    List<ScriptCommand> getCommands() {
        ArrayList<ScriptCommand> commands = new ArrayList<>();

        // get all behaviors in the list as Behavior Nodes
        List<Behavior> behaviors = behaviorList.getChildren().stream()
                .map(b -> (Behavior) b)
                .collect(Collectors.toList());

        // Iterate behaviors on submitted script.
        // Each valid behavior is converted to a ScriptCommand object
        for (Behavior behavior : behaviors) {
            if (isCompleteBehavior(behavior)) {

                // Get the enumerated values of the current behavior
                ArrayList<String> scriptingObjects = getScriptingEnums(behavior);

                ArrayList<Check> checks = constructChecks(scriptingObjects);

                // Get Command enum for the command button
                Command command = getCommand(scriptingObjects.get(scriptingObjects.size() - 1));

                commands.add(new ScriptCommand(checks, command));
            }
        }

        return commands;
    }

    private Command getCommand(String text) {
        for (Command command : Command.values()) {
            if (text.trim().equals(command.text())) {
                return command;
            }
        }
        return null;
    }

    /**
     * Returns whether or not all behaviors in the list are complete
     *
     * @param allBehaviors List of all Behavior nodes to be checked
     * @return Returns true if all behaviors are complete. False otherwise
     */
    boolean canAddBehaviors(List<Node> allBehaviors) {
        for (Node node : allBehaviors) {
            Behavior asBehavior = (Behavior) node;
            if (!isCompleteBehavior(asBehavior)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Shows an informational alert stating that the selected action could not be completed since no behavior has been
     * selected
     */
    void alertNoneSelected() {
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
        if (behavior.getChildren().size() == 0) {
            return false;
        }
        ScriptButton lastButton = (ScriptButton) behavior.getChildren().get(behavior.getChildren().size() - 1);

        // We can do this since validity is enforced along the way
        return ScriptingTypes.COMMAND.list().contains(lastButton.getText());
    }

    /**
     * Enable all buttons in validButtons, disable the rest that appear in the choicesPane
     *
     * @param validText List of valid Strings at current point
     */
    void enableButtons(List<String> validText) {
        allButtons.forEach(b -> {
            if (b instanceof ChoiceButton) {
                if (validText.contains(((ChoiceButton) b).getText().trim())) {
                    b.setDisable(false);
                } else {
                    b.setDisable(true);
                }
            }
        });
    }

    /**
     * Gets the proper enum values that correspond with each button in the provided Behavior Node
     *
     * @param behavior Behavior node to be parsed for enumerators
     * @return Returns a list of enums parsed from behavior
     */
    private ArrayList<String> getScriptingEnums(Behavior behavior) {
        ArrayList<String> objects = new ArrayList<>();
        for (Node button : behavior.getChildren()) {
            String buttonText = ((ScriptButton) button).getText();

            if (ScriptingTypes.COMMAND.list().contains(buttonText)) {
                for (Command command : Command.values()) {
                    if (command.text().equals(buttonText)) {
                        objects.add(command.text());
                        break;
                    }
                }
            } else if (ScriptingTypes.OPERATOR.list().contains(buttonText)) {
                for (Operator operator : Operator.values()) {
                    if (operator.text().equals(buttonText)) {
                        objects.add(operator.text());
                        break;
                    }
                }
            } else if (ScriptingTypes.DATA.list().contains(buttonText)) {
                for (Data data : Data.values()) {
                    if (data.text().equals(buttonText)) {
                        objects.add(data.text());
                        break;
                    }
                }
            } else if (ScriptingTypes.CONDITIONAL.list().contains(buttonText)) {
                for (Conditional conditional : Conditional.values()) {
                    if (conditional.text().equals(buttonText)) {
                        objects.add(conditional.text());
                        break;
                    }
                }
            } else {
                // itz a number dood
                objects.add(buttonText.trim());
            }

        }
        return objects;
    }
    //endregion
}
