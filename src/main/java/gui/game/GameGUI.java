package gui.game;

import common.BaseLogger;
import engine.core.Engine;
import javafx.application.Application;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.mapeditor.core.Map;
import org.mapeditor.core.MapLayer;
import org.mapeditor.core.Tile;
import org.mapeditor.core.TileLayer;
import org.mapeditor.io.TMXMapReader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class GameGUI extends Application {

    private static final BaseLogger LOGGER = new BaseLogger("MenuGUI");
    private Engine _engine = null;

    TMXMapReader mapReader = new TMXMapReader();
    Map gameMap = null;

    /**
     * Creates the game Engine and game GUI. Each on a separate thread.
     *
     * @throws Exception in start if failure to load game map file
     */
    public GameGUI() throws Exception {
        new Thread().start();

        //Create Engine
        _engine = new Engine();
        LOGGER.info("Beginning core game battle...");
        this._engine.startGame();

        start(new Stage());
    }

    /**
     * Creates the game window
     * @param primaryStage JavaFX stage object to be rendered upon loading.
     * @throws Exception on failure to load game Map
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // load ui element objects from fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        Parent root = loader.load();

        ObservableMap namespace = loader.getNamespace();

        // Access imagePane FXML object
        Pane imagePane = (Pane) namespace.get("imagePane");

        primaryStage.setTitle("Code Carnage");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

        try {
            gameMap = mapReader.readMap("../resources/main/game-map.tmx");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Render the map, 1 layer at a time
        renderMap(imagePane);

    }

    /**
     * Creates a JavaFX Image based on the tile ID hash
     * "borrowed" fragment from part of
     * https://community.oracle.com/message/9655930#9655930
     *
     * @param image awt image of the current tile
     * @return JavaFX Image for the particular tile in the map render.
     * @throws IOException on failure to write to Image object
     */
    private Image createImage(BufferedImage image) throws IOException {
        if (!(image instanceof RenderedImage)) {
            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
                    image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

            Graphics g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
            image = bufferedImage;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write((RenderedImage) image, "png", out);
        out.flush();
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        return new javafx.scene.image.Image(in);
    }

    /**
     * Draw the map on the UI, layer by layer
     *
     * @param imagePane Container for all of the tiles to be rendered
     */
    private void renderMap(Pane imagePane) {

        ArrayList<MapLayer> layerList = new ArrayList<MapLayer>(gameMap.getLayers());

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

            // Create a Tile Map which stores Tile ID and Tile Image data for the layer
            HashMap<Integer, Image> tileHash = new HashMap<Integer, Image>();
            Image tileImage = null;

            // Create the Image for each tile in the layer, add it as an ImageView in the UI
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
                            tileImage = createImage(tile.getImage());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        tileHash.put(tileID, tileImage);
                    }

                    ImageView i = new ImageView(tileImage);

                    i.setTranslateX(x * 32);
                    i.setTranslateY(y * 32);

                    imagePane.getChildren().add(i);

                }
            }

            tileHash = null;
            gameMap = null;
        }
    }

}
