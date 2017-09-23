package gui.menu;

import com.jfoenix.controls.JFXButton;
import common.BaseLogger;
import engine.core.Engine;
import gui.game.GameGUI;
import javafx.application.Application;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MenuGUI extends Application {

    private Engine _engine = null;
    private static final BaseLogger LOGGER = new BaseLogger("MenuGUI");

    public MenuGUI() {
        super();
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

        loader.setController(controller);

        startButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent m) {
                        System.out.println("You clicked Start!");

                        try {
                            new GameGUI();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        primaryStage.getScene().getWindow().hide();
                    }
                });

        primaryStage.setTitle("Code Carnage");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Sets the MenuGUI's engine, which it will be observing.
     * @throws IllegalStateException If the engine already exists and is trying to be re-assigned.
     * @param engine The engine which should be used as the target of the observer.
     */
    public void observe(Engine engine) {
//        if (this._engine != null) {
//            throw new IllegalStateException("Engine already exists for MenuGUI, but attempted to re-specify.");
//        }
//        LOGGER.debug("Assigning Engine to MenuGUI.");
//        this._engine = engine;
        // TODO: Remove the below! We aren't in the game just yet!!!
//        LOGGER.info("Beginning core game battle...");
//        this._engine.startGame();
    }

}
