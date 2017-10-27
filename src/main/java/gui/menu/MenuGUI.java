/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package gui.menu;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import common.BaseLogger;
import gui.scripting.ScriptingGUI;
import interpreter.enumerations.Difficulty;
import javafx.application.Application;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MenuGUI extends Application {

    private static final BaseLogger LOGGER = new BaseLogger("MenuGUI");

    private static boolean firstTime = true;

    public MenuGUI() throws Exception {
        if (!firstTime) {
            start(new Stage());
        }
        firstTime = false;
    }

    public MenuGUI(String[] args) {
        new Thread(
                () -> launch(args)
        ).start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Parent root = loader.load();
        MenuController controller = new MenuController();

        ObservableMap map = loader.getNamespace();

        JFXButton startButton = (JFXButton) map.get("buttonStart");
        // find the UI element
        JFXComboBox<String> difficultyComboBox = (JFXComboBox<String>) map.get("comboBoxSelectDiff");
        // Set items and prompt message
        difficultyComboBox.setPromptText("Please select difficulty");
        difficultyComboBox.getItems().addAll("Easy", "Medium", "Hard");

        loader.setController(controller);

        startButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
            (MouseEvent m) -> {
                LOGGER.debug("You clicked Start!");
                // get the selected difficulty value
                String difficulty = difficultyComboBox.getValue();
                // if the player does not select a difficulty, it is Medium by default
                if(difficulty == null){
                    difficulty = "Medium";
                }
                LOGGER.debug("Difficulty set to: " + difficulty);
                try {
                    new ScriptingGUI(difficulty);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                primaryStage.getScene().getWindow().hide();
            }
        );

        primaryStage.setTitle("Code Carnage");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

}
