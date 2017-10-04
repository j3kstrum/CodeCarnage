/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package gui.menu;

import com.jfoenix.controls.JFXButton;
import common.BaseLogger;
import engine.core.Engine;
import gui.scripting.ScriptingGUI;
import javafx.application.Application;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MenuGUI extends Application {

    private static final BaseLogger LOGGER = new BaseLogger("MenuGUI");

    public MenuGUI() {
        super();
    }

    public MenuGUI(String[] args) {
        new Thread(
                () -> launch(args)
        ).start();
    }

    private static final BaseLogger LOGGER = new BaseLogger("MenuGUI");

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Parent root = loader.load();
        MenuController controller = new MenuController();

        ObservableMap map = loader.getNamespace();

        JFXButton startButton = (JFXButton) map.get("buttonStart");

        loader.setController(controller);

        startButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
            (MouseEvent m) -> {
                LOGGER.debug("You clicked Start!");

                try {
                    new ScriptingGUI();
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
