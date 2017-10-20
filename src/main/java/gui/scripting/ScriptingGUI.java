/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package gui.scripting;

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
        ObservableMap nodesMap = loader.getNamespace();

        //Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();


        Parent root = loader.load();

        primaryStage.setTitle("Code Carnage");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        //primaryStage.setWidth(visualBounds.getWidth());
        //primaryStage.setHeight(visualBounds.getHeight());
        //setDimensions(nodesMap, visualBounds);
        primaryStage.show();

        // Assign the stylesheet to the Scene
        root.getStylesheets().add(
                getClass().getResource("/styles/script.css").toExternalForm()
        );
    }

    private void setDimensions(ObservableMap map, Rectangle2D visualBounds) {
        ScrollPane choicesScroll = (ScrollPane) map.get("choicesScroll");
        ScrollPane behaviorsScroll = (ScrollPane) map.get("behaviorsScroll");
        AnchorPane behaviorPane = (AnchorPane) map.get("behaviorPane");
        BehaviorList behaviorList = (BehaviorList) map.get("behaviorList");
        JFXButton add = (JFXButton) map.get("add");
        JFXButton submit = (JFXButton) map.get("submit");
        JFXButton subtract = (JFXButton) map.get("subtract");
        JFXButton deleteWord = (JFXButton) map.get("deleteWord");
        Pane logoPane = (Pane) map.get("logoPane");


        behaviorsScroll.setPrefWidth(.75 * visualBounds.getWidth());
        behaviorsScroll.setPrefHeight(.5 * visualBounds.getHeight());
        behaviorPane.setPrefWidth(.75 * visualBounds.getWidth());
        behaviorPane.setPrefHeight(.75 * visualBounds.getHeight());

        behaviorsScroll.setFitToWidth(true);
        behaviorsScroll.setFitToHeight(true);
        behaviorList.setPrefWidth(.73 * visualBounds.getWidth());

        choicesScroll.setPrefHeight(.9 * visualBounds.getHeight());


    }
}
