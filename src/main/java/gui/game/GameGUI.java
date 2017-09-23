package gui.game;

import common.BaseLogger;
import engine.core.Engine;
import javafx.application.Application;
import javafx.collections.ObservableMap;
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
import java.util.HashMap;


public class GameGUI extends Application {

    private static final BaseLogger LOGGER = new BaseLogger("MenuGUI");
    private Engine _engine = null;

    TMXMapReader mapReader = new TMXMapReader();
    Map gameMap = null;
    TileLayer groundLayer = null;

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

        Pane imagePane = new Pane();
        imagePane.setPrefSize(1600, 900);

        pane.getChildren().add(group);

        group.getChildren().add(imagePane);

        primaryStage.setTitle("Code Carnage");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        try {
            gameMap = mapReader.readMap("./src/main/resources/carnage.tmx");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (int layerIndex = 0; layerIndex < 4; layerIndex++) {

            groundLayer = (TileLayer) gameMap.getLayer(layerIndex);
            if (groundLayer == null) {
                System.out.println("can't get map layer");
                System.exit(-1);
            }

            int width = groundLayer.getBounds().width;
            int height = groundLayer.getBounds().height;

            Tile tile = null;
            int tileID;

            HashMap<Integer, Image> tileHash = new HashMap<Integer, Image>();
            Image tileImage = null;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    tile = groundLayer.getTileAt(x, y);
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

//                i.setFitHeight(64);
//                i.setFitWidth(64);
//                i.setPreserveRatio(true);

                    i.setTranslateX(x * 32);
                    i.setTranslateY(y * 32);

                    imagePane.getChildren().add(i);


                }
            }

            System.out.println("Tile image hash has " + tileHash.size() + " items");

            tileHash = null;
            gameMap = null;
            groundLayer = null;
        }


    }

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

}
