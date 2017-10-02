package gui.scripting;

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScriptingGUI extends Application {


    public ScriptingGUI() throws Exception {
        new Thread().start();
        //Create Engine
        start(new Stage());
    }

    public ScriptingGUI(String[] args) {
        new Thread(
                () -> launch(args)
        ).start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scripting.fxml"));

        Parent root = loader.load();

        ObservableMap namespace = loader.getNamespace();

        JFXButton submit = (JFXButton) namespace.get("submit");

        primaryStage.setTitle("Code Carnage");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
        root.getStylesheets().add(
                getClass().getResource("/styles/script.css").toExternalForm()
        );

//        submit.addEventHandler(MouseEvent.MOUSE_CLICKED,
//                new EventHandler<MouseEvent>() {
//                    public void handle(MouseEvent m) {
//                        System.out.println("You clicked Submit!");
//
//                        try {
//                            new GameGUI();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        primaryStage.getScene().getWindow().hide();
//                    }
//                });
    }
}
