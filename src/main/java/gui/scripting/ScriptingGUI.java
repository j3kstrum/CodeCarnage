/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package gui.scripting;

import common.BaseLogger;
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

    private static final BaseLogger LOGGER = new BaseLogger("ScriptingGUI");
    private String difficulty;

    // Modified the original constructor, not sure if it is acceptable?
    public ScriptingGUI(String difficulty) throws Exception {
        this.difficulty = difficulty;
        LOGGER.debug("default constructor with difficulty: " + this.difficulty);
        start(new Stage());
    }

    public ScriptingGUI(String[] args) {
        LOGGER.debug("args constructor");
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

        // Get instance of controller in order to pass the difficulty
        ScriptingController controller = loader.getController();

        controller.createContext(this.difficulty);

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
