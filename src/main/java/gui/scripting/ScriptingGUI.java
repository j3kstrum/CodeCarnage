/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package gui.scripting;

import com.jfoenix.controls.JFXButton;
import gui.game.GameGUI;
import javafx.application.Application;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * GUI for the Scripting menu.
 * This will allow the user to input their desired scripting functionalities
 * and to submit their scripts once completed.
 *
 * @author nick.martin
 */
public class ScriptingGUI extends Application {

    public ScriptingGUI() throws Exception {
        start(new Stage());
    }

    /**
     * Starts the ScriptingGUI as determined by JavaFX.
     * @param primaryStage The stage onto which the GUI should be rendered.
     * @throws Exception If the underlying JavaFX components or other code throw Exceptions.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the scripting graphics and scene.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scripting.fxml"));
        Parent root = loader.load();

        // Acquire the namespace and the submit button from the namespace.
        ObservableMap namespace = loader.getNamespace();
        JFXButton submit = (JFXButton) namespace.get("submit");

        // Setup the scene and its properties, and then show it.
        primaryStage.setTitle("Code Carnage");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

        // Add an event handler when the submit button is clicked.
        submit.addEventHandler(MouseEvent.MOUSE_CLICKED,
                (MouseEvent m) -> {
                    try {
                        // If GameGUI fails to render, don't hide the window.
                        // TODO(nick): evaluate if above is an appropriate way to handle things.

                        // Starts the GameGUI, and then hides this scripting GUI.
                        new GameGUI();
                        primaryStage.getScene().getWindow().hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
    }
}
