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
import utilties.entities.Player;
import utilties.models.EntityMap;
import utilties.models.EntityTile;
import utilties.models.Game;
import utilties.models.Location;

import java.util.ArrayList;
import java.util.HashMap;


public class GameGUI extends Application {

    private static final BaseLogger LOGGER = new BaseLogger("MenuGUI");
    private Engine _engine = null;

    private static final int PLAYER_LAYER = 2;


    private Game game;

    TMXMapReader mapReader = new TMXMapReader();
    public Map gameMap = null;
    Pane _imagePane = null;

    public GameGUI() throws Exception {
        new Thread().start();

        //Create Engine
        _engine = new Engine();
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
        _imagePane.setPrefSize(1600, 900);

        pane.getChildren().add(group);

        group.getChildren().add(_imagePane);

        primaryStage.setTitle("Code Carnage");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        try {
            gameMap = mapReader.readMap("./src/main/resources/game-map.tmx");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        EntityMap entityMap = new EntityMap(gameMap, getPlayerTiles(gameMap));
        game = new Game(entityMap);

        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {

        while (true)
        {
            Platform.runLater ( () -> updateGameGUI(gameMap));
            Thread.sleep (1000);
            game.nextTurn();
            //nextTurn(gameMap);
        }
            }
        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    public void updateGameGUI(Map gameMap){
        ArrayList<MapLayer> layerList = new ArrayList<>(gameMap.getLayers());

        for (MapLayer layer : layerList) {

            TileLayer tileLayer = (TileLayer) layer;

            if (tileLayer == null) {
                System.out.println("can't get map layer");
                System.exit(-1);
            }

            int width = tileLayer.getBounds().width;
            int height = tileLayer.getBounds().height;

            Tile tile = null;
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


    private ArrayList<EntityTile> getPlayerTiles(Map map){

        ArrayList<Tile> playerTiles = new ArrayList<>();
        TileLayer playerLayer = (TileLayer) map.getLayer(PLAYER_LAYER);
        int height = playerLayer.getBounds().height;
        int width = playerLayer.getBounds().width;

        Location playerLocation  = null;
        Location opponentLocation = null;

        Tile tile;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tile = playerLayer.getTileAt(x, y);
                if (tile == null) {

                    continue;
                }
                else{
                    if(playerTiles.size() == 0){
                        playerLocation = new Location(x, y);
                    }
                    else{
                        opponentLocation = new Location(x, y);
                    }
                    playerTiles.add(tile);
                }

            }
        }

        Tile playerTile = playerTiles.get(0);
        Tile opponentTile = playerTiles.get(1);


        EntityTile playerEntityTile = new EntityTile(playerLocation, new Player(0, playerLocation), playerTile);
        EntityTile opponentEntityTile = new EntityTile(opponentLocation, new Player(1, opponentLocation), opponentTile);

        ArrayList<EntityTile> playerEntityTiles = new ArrayList<>();
        playerEntityTiles.add(playerEntityTile);
        playerEntityTiles.add(opponentEntityTile);

        return playerEntityTiles;

    }


    public Map nextTurn(Map gameMap){
        ArrayList<MapLayer> layerList = new ArrayList<>(gameMap.getLayers());
        TileLayer tileLayer = (TileLayer) layerList.get(2);
        Tile tile = tileLayer.getTileAt(1,7);
        tileLayer.removeTile(tile);
        tileLayer.setTileAt(4,13, tile);

        Tile tileOpponent = tileLayer.getTileAt(23,7);
        tileLayer.removeTile(tileOpponent);
        tileLayer.setTileAt(22,7, tileOpponent);

        return gameMap;
    }

}
