package gui.game;

import common.BaseLogger;
import engine.core.Engine;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class GameGUI extends Application {

    private static final BaseLogger LOGGER = new BaseLogger("MenuGUI");
    private Engine _engine = null;

    public GameGUI() throws Exception {
        new Thread().start();

        start(new Stage());

        //Create Engine
        _engine = new Engine();
        LOGGER.info("Beginning core game battle...");
        this._engine.startGame();
    }

    public GameGUI(String[] args) {
        new Thread(
                () -> launch(args)
        ).start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Code Carnage");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

    }

}
