package utilties.models;

import org.mapeditor.core.Map;
import org.mapeditor.core.MapLayer;
import org.mapeditor.core.Tile;
import org.mapeditor.core.TileLayer;

import java.util.ArrayList;

/**
 * Map in which Game is Played on.  Represented as a two dimensional array of tiles.
 */
public class EntityMap {

    private Map _gameMap;

    private EntityTile _playerTile;

    private EntityTile _opponentTile;

    private ArrayList<EntityTile> _players;

    /**
     * Creates Map with a Tiled Map object and list of players on map.  Currently only works with two players
     */
    public EntityMap(Map map, ArrayList<EntityTile> players){
        this._gameMap = map;
        this._playerTile = players.get(0);
        this._opponentTile = players.get(1);
        this._players = players;
    }

    /**
     * Takes in the id of the player and location and moves them to that location on the map
     * @param id ID of the player
     * @param location Location to move to
     */
    public void setLocation(int id, Location location){

        EntityTile playerTile = this._players.get(id);

        int playerX = playerTile.getLocation().getX();
        int playerY = playerTile.getLocation().getY();

        ArrayList<MapLayer> layerList = new ArrayList<>(this._gameMap.getLayers());
        TileLayer tileLayer = (TileLayer) layerList.get(2);
        Tile tile = tileLayer.getTileAt(playerX,playerY);
        tileLayer.removeTile(tile);
        tileLayer.setTileAt(location.getX(),location.getY(), tile);
        playerTile.getLocation().setX(location.getX());
        playerTile.getLocation().setY(location.getY());
    }

    /**
     * Get tile object for Player
     * @return
     */
    public EntityTile getPlayerTile() {
        return _playerTile;
    }

    /**
     * Get tile object for Opponent
     * @return
     */
    public EntityTile getOpponentTile() {
        return _opponentTile;
    }
}

