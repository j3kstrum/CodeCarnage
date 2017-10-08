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
import gui.menu.MenuGUI;
import interpreter.ScriptCommand;
import javafx.application.Application;
import javafx.collections.ObservableMap;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.mapeditor.core.Map;
import org.mapeditor.core.MapLayer;
import org.mapeditor.core.Tile;
import org.mapeditor.core.TileLayer;
import utilties.models.Game;

import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.List;

public class GameGUI extends Application {

    private static final BaseLogger LOGGER = new BaseLogger("MenuGUI");
    private Engine _engine = null;

    private ProgressBar userHealth, opponentHealth;

    // layerIndex + (layers * (x + (width * y))) -> tile at x, y, currLayer
    private HashMap<Integer, Integer> mapCache = new HashMap<>();
    private HashMap<Integer, Image> tileCache = new HashMap<>();
    private ArrayList<Point> redrawCoords = new ArrayList<>();

    private boolean hasDisplayedResultScreen = false;
    private ArrayList<ScriptCommand> commandObjects;

    private Stage _stage = null;

    public Map _map;
    private Pane _imagePane;

    public GameGUI(ArrayList<ScriptCommand> commandObjects) throws Exception {
        //Create Engine
        this.commandObjects = commandObjects;
        _engine = new Engine(this);
        LOGGER.info("Beginning game gui and engine...");
        this._engine.startGame();

        start(new Stage());
    }

    public List<ScriptCommand> getCommandObjects() {
        return commandObjects;
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

        this._stage = primaryStage;

        primaryStage.setTitle("Code Carnage");
        primaryStage.setScene(new Scene(root, 800, 480));
        primaryStage.setResizable(false);
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
            return;
        }

        updateGameGUI();
        ts.restart();
    }

    /**
     * Identifies map "Deltas."
     *
     * A Delta is defined as a map tile whose state differs from the previous tick.
     * These are identified to prevent useless computation associated with redrawing the entire map each tick.
     *
     * Some code derived from http://discourse.mapeditor.org/t/loading-tmx-map-and-displaying-with-javafx/1189
     * @param layerList The list of map layers to be used for cache comparisons.
     */
    private void identifyDeltas(ArrayList<MapLayer> layerList) {

        final int layers = layerList.size();
        // Initialize first layer
        int layerIndex = -1;

        // Search for points that must be redrawn
        for (MapLayer layer : layerList) {
            layerIndex++;

            TileLayer tileLayer = (TileLayer) layer;

            if (tileLayer == null) {
                LOGGER.fatal("Cannot retrieve map layer.");
                System.exit(-1);
            }

            int width = tileLayer.getBounds().width;
            int height = tileLayer.getBounds().height;

            Tile tile;
            int tileID;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // If we're already redrawing, don't waste computation.
                    if (redrawCoords.contains(new Point(x, y))) {
                        continue;
                    }
                    // Get the cache key for this specific point.
                    int cacheVal = layerIndex + (layers * (x + (width * y)));
                    tile = tileLayer.getTileAt(x, y);
                    // Use -1 for no tile ID instead of null to create discrepancy between "never drawn"
                    // and "drawn but a blank tile".
                    tileID = tile == null ? -1 : tile.getId();
                    // If it's null, this is our first time rendering - so we must draw.
                    // If not and there is a discrepancy with the cache, we need to update the map and cache.
                    if (mapCache.get(cacheVal) == null || !mapCache.get(cacheVal).equals(tileID)) {
                        // Need to redraw this value.
                        redrawCoords.add(new Point(x, y));
                    }
                }
            }
        }

    }

    /**
     * Updates, on the GUI, the deltas that were identified for the current tick.
     * Some code derived from http://discourse.mapeditor.org/t/loading-tmx-map-and-displaying-with-javafx/1189
     *
     * @param layerList The list containing each of the layers of the map.
     *
     */
    private void updateDeltas(ArrayList<MapLayer> layerList) {
        // Reset layer and reverse iteration again.
        final int layers = layerList.size();
        // LayerIndex will keep track of which layer we're on. This is used to accurately access the cache.
        int layerIndex = -1;

        // For each layer, redraw points that must be redrawn.
        for (MapLayer layer : layerList) {

            // Perform variable initializations.
            layerIndex++;
            TileLayer tileLayer = (TileLayer) layer;
            if (tileLayer == null) {
                LOGGER.fatal("Cannot retrieve map layer.");
                System.exit(-1);
            }
            int width = tileLayer.getBounds().width;
            Image tileImage = null;
            Tile tile;
            int tileID;

            // For each point (which, hopefully, has been identified previously)
            for (Point p : redrawCoords) {
                // Get the cache value that we will deposit into.
                int cacheVal = layerIndex + (layers * (p.x + (width * p.y)));
                // Get the tile that needs to be redrawn on this layer.
                tile = tileLayer.getTileAt(p.x, p.y);
                // If it's null, don't bother redrawing.
                // Instead, store 'empty tile' in the cache.
                if (tile == null) {
                    mapCache.put(cacheVal, -1);
                    continue;
                }
                // Get the unique identifier for the tile image.
                tileID = tile.getId();
                if (tileCache.containsKey(tileID)) {
                    // Grab the image from the image cache.
                    tileImage = tileCache.get(tileID);
                } else {
                    // Convert the tile's image to a swing image, and then cache it.
                    try {
                        tileImage = SwingFXUtils.toFXImage(tile.getImage(), null);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    tileCache.put(tileID, tileImage);
                }

                // Create the actual image to be redrawn.
                ImageView i = new ImageView(tileImage);
                i.setTranslateX(p.x * 32);
                i.setTranslateY(p.y * 32);

                // Update the map cache and then redraw the tile.
                mapCache.put(cacheVal, tileID);
                _imagePane.getChildren().add(i);
            }
        }
    }

    /**
     * Displays the result screen found at the specified path.
     * @param imagePath The path containing the image file to be used.
     */
    private void displayResultScreen(URL imagePath) {
        ImageView i = new ImageView(new Image(imagePath.toString()));

        // Ensure that, even if we ever resize the game map, we scale the results screen to encompass everything.
        i.setFitWidth(_imagePane.getWidth());
        i.setFitHeight(_imagePane.getHeight());

        _imagePane.getChildren().add(i);
        hasDisplayedResultScreen = true;
    }

    /**
     * Updates the GUI based on data read from Map
     * Some child code derived from http://discourse.mapeditor.org/t/loading-tmx-map-and-displaying-with-javafx/1189
     */
    private void updateGameGUI() {
        // If null return
        if (_map == null) {
            LOGGER.fatal("Could not update GameGUI: Map == null.");
            return;
        }

        // If the game is over, display the appropriate screen (if not yet done) and then return.
        // We won't be updating tiles, since the game is DEFINITIVELY over.
        String overlay = null;
        switch (_engine.getGameState()) {
            case INACTIVE:
                return;
            case WON:
                overlay = "/winner_overlay.png";
                break;
            case LOST:
                overlay = "/defeated_overlay.png";
                break;
            case STALEMATE:
                overlay = "/stalemate_overlay.png";
                break;
        }
        if (overlay != null) {
            if (!hasDisplayedResultScreen) {
                URL img = getClass().getResource(overlay);
                displayResultScreen(img);
            }

            // HERE
            Alert endGame = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to play again?",
                    ButtonType.YES, ButtonType.NO);
            endGame.setHeaderText(null);
            endGame.setTitle("Game Over.");

            Optional<ButtonType> result = endGame.showAndWait();

            if (result.isPresent()) {
                _engine.shutdown(true);

                if (result.get().equals(ButtonType.YES)) {
                    // start new game
                    try {
                        MenuGUI m = new MenuGUI();
                        _stage.getScene().getWindow().hide();
                        return;

                    } catch (Exception ex) {
                        System.exit(1);
                    }

                } else {
                    System.exit(0);
                }
            }
            return;
        }

        // Get map data
        ArrayList<MapLayer> layerList = new ArrayList<>(this._map.getLayers());
        redrawCoords = new ArrayList<>();

        // Reverse layers for top-down identification
        Collections.reverse(layerList);

        identifyDeltas(layerList);

        // Reverse the layers again so we can draw back-to-front.
        Collections.reverse(layerList);

        updateDeltas(layerList);

        if (this.userHealth == null) {
            this.userHealth = new ProgressBar();
            this.opponentHealth = new ProgressBar();
            this.opponentHealth.setTranslateX(700);
        } else {
            _imagePane.getChildren().remove(this.userHealth);
            _imagePane.getChildren().remove(this.opponentHealth);
        }

        double userHealth = _engine.game.getPlayer(Game.PLAYER_ID).getHealth();
        double opponentHealth = _engine.game.getPlayer(Game.OPPONENT_ID).getHealth();
        double max = Game.HEALTH_MAX;

        double userRatio = userHealth / max;

        this.userHealth.setProgress(userRatio);

        double opponentRatio = opponentHealth / max;

        System.out.println("OPPONENT HEALTH: " + opponentHealth);
        System.out.println("RATIO: " + Double.toString(opponentRatio));

        this.opponentHealth.setProgress(opponentHealth / max);

        _imagePane.getChildren().add(this.opponentHealth);
        _imagePane.getChildren().add(this.userHealth);

    }
}
