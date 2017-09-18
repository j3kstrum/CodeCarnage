package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import common.BaseLogger;
import engine.core.Engine;

public class GUI extends Application {

    private Engine _engine = null;
    private static final BaseLogger LOGGER = new BaseLogger("GUI");

    public GUI() {
        super();
    }

    public GUI(String[] args) {
        new Thread(
                () -> launch(args)
        ).start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Code Carnage");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Sets the GUI's engine, which it will be observing.
     * @throws IllegalStateException If the engine already exists and is trying to be re-assigned.
     * @param engine The engine which should be used as the target of the observer.
     */
    public void observe(Engine engine) {
        if (this._engine != null) {
            throw new IllegalStateException("Engine already exists for GUI, but attempted to re-specify.");
        }
        LOGGER.debug("Assigning Engine to GUI.");
        this._engine = engine;
        // TODO: Remove the below! We aren't in the game just yet!!!
        LOGGER.info("Beginning core game battle...");
        this._engine.startGame();
    }

}
