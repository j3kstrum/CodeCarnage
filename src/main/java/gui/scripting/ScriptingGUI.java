/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package gui.scripting;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    public ScriptingGUI(String[] args) {
        new Thread(
                () -> launch(args)
        ).start();
    }

    /**
     * Starts the ScriptingGUI as determined by JavaFX.
     * @param primaryStage The stage onto which the GUI should be rendered.
     * @throws Exception If the underlying JavaFX components or other code throw Exceptions.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scripting.fxml"));

        Parent root = loader.load();

        primaryStage.setTitle("Code Carnage");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

        // Assign the stylesheet to the Scene
        root.getStylesheets().add(
                getClass().getResource("/styles/script.css").toExternalForm()
        );
    }
}
