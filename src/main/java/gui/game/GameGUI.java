package gui.game;

import common.BaseLogger;
import engine.core.Engine;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.mapeditor.core.Map;
import org.mapeditor.core.MapLayer;
import org.mapeditor.core.Tile;
import org.mapeditor.core.TileLayer;
import org.mapeditor.io.TMXMapReader;

import java.util.ArrayList;
import java.util.HashMap;


public class GameGUI extends Application {

    private static final BaseLogger LOGGER = new BaseLogger("MenuGUI");
    private Engine _engine = null;

    public Map map;
    Pane _imagePane;

    public GameGUI() throws Exception {
        new Thread().start();

        //Create Engine
        _engine = new Engine(this);
        LOGGER.info("Beginning core game battle...");
        this._engine.startGame();

        start(new Stage());
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

        ObservableMap namespace = loader.getNamespace();

        StackPane pane = (StackPane) namespace.get("pane");
        Group group = new Group();

        _imagePane = new Pane();
        _imagePane.setPrefSize(800, 480);

        pane.getChildren().add(group);

        group.getChildren().add(_imagePane);

        primaryStage.setTitle("Code Carnage");
        primaryStage.setScene(new Scene(root, 800, 480));
        primaryStage.show();

        startUIUpdateThread();

    }

    private void startUIUpdateThread(){
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {

                while (true)
                {
                    Platform.runLater ( () -> updateGameGUI());
                    Thread.sleep (250);
                }
            }
        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    public void updateGameGUI(){

        if(map==null){
            return;
        }
        ArrayList<MapLayer> layerList = new ArrayList<>(this.map.getLayers());

        for (MapLayer layer : layerList) {

            TileLayer tileLayer = (TileLayer) layer;

            if (tileLayer == null) {
                System.out.println("can't get map layer");
                System.exit(-1);
            }

            int width = tileLayer.getBounds().width;
            int height = tileLayer.getBounds().height;

            Tile tile;
            int tileID;

            HashMap<Integer, Image> tileHash = new HashMap<>();
            Image tileImage = null;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    tile = tileLayer.getTileAt(x, y);
                    if (tile == null) {
                        continue;
                    }
                    tileID = tile.getId();
                    if (tileHash.containsKey(tileID)) {
                        tileImage = tileHash.get(tileID);
                    } else {
                        try {
                            tileImage = SwingFXUtils.toFXImage(tile.getImage(), null);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        tileHash.put(tileID, tileImage);
                    }

                    ImageView i = new ImageView(tileImage);
                    i.setTranslateX(x * 32);
                    i.setTranslateY(y * 32);

                    _imagePane.getChildren().add(i);
                }
            }
        }
    }
}
