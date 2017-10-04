/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package gui.game;

import common.BaseLogger;
import engine.core.Engine;
import engine.core.TickingService;
import javafx.application.Application;
import javafx.collections.ObservableMap;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.mapeditor.core.Map;
import org.mapeditor.core.MapLayer;
import org.mapeditor.core.Tile;
import org.mapeditor.core.TileLayer;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Thread.sleep;


public class GameGUI extends Application {

    private static final BaseLogger LOGGER = new BaseLogger("MenuGUI");
    private Engine _engine = null;

    private ArrayList<ImageView> addChildBuffer;

    public Map _map;
    private Pane _imagePane;

    public GameGUI() throws Exception {
        //Create Engine
        _engine = new Engine(this);
        LOGGER.info("Beginning game gui and engine...");
        this._engine.startGame();

        start(new Stage());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Builds main panel to draw game on
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        Parent root = loader.load();

        ObservableMap namespace = loader.getNamespace();

        AnchorPane pane = (AnchorPane) namespace.get("pane");
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

    /**
     * Starts thread to update Game GUI
     */
    private void startUIUpdateThread() {

        TickingService ts = new TickingService(_engine);

        ts.setOnSucceeded(
                (event) -> {
                    _map = ts.getValue();
                    uiTick(ts);
                }
        );

        uiTick(ts);
    }

    /**
     * Performs all of the required actions for the UI to tick.
     * @param ts The ticking service to be used for engine tick calls.
     */
    private void uiTick(TickingService ts) {
        if (!_engine.isRunning()) {
            LOGGER.info("Engine not running.");

            if (!_engine.cleanup()) {
                LOGGER.critical("Engine cleanup failed.");
                System.exit(1);
            }
            System.exit(0);
        }

        addChildBuffer = new ArrayList<>();
        new Thread(this::updateGameGUI).start();
        ts.restart();
        for (ImageView child : addChildBuffer) {
            // This can no longer be done in updateGameGUI,
            // as it is called from a non-FX thread.
            _imagePane.getChildren().add(child);
        }
    }

    /**
     * Updates the GUI based on data read from Map
     * Renderer code derived from http://discourse.mapeditor.org/t/loading-tmx-map-and-displaying-with-javafx/1189
     */
    public void updateGameGUI() {

        if (_map == null) {
            return;
        }
        ArrayList<MapLayer> layerList = new ArrayList<>(this._map.getLayers());

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

                    addChildBuffer.add(i);
                }
            }
        }
    }
}
