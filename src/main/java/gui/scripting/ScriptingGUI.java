package gui.scripting;

import javafx.application.Application;
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
