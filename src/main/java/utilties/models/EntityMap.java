/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package utilties.models;

import org.mapeditor.core.Map;
import org.mapeditor.core.MapLayer;
import org.mapeditor.core.Tile;
import org.mapeditor.core.TileLayer;
import utilties.entities.Empty;
import utilties.entities.Entity;
import utilties.entities.Player;

import java.awt.*;
import java.util.ArrayList;

/**
 * Model class for GameMap.  Represented as a two dimensional array of tiles.
 */
public class EntityMap {

    private Map _gameMap;

    private EntityTile _playerTile;

    private EntityTile _opponentTile;

    private ArrayList<EntityTile> _players;

    private EntityTile[][] _entityTiles;

    private int _numberOfColumns;

    private int _numberOfRows;

    private static final int PLAYER_LAYER = 2;

    /**
     * Creates Map with a Tiled Map object and list of players on map.  Currently only works with two players
     */
    public EntityMap(Map map, int columns, int rows) {
        this._gameMap = map;
        //Get players and corresponding locations from map
        ArrayList<EntityTile> players = getPlayerTiles(this._gameMap);
        this._players = players;
        //Get player/opponent.  Currently index 0 corresponds to player/index 1 corresponds to the opponent
        this._playerTile = players.get(0);
        this._opponentTile = players.get(1);
        _entityTiles = new EntityTile[columns][rows];
        this._numberOfColumns = columns;
        this._numberOfRows = rows;
        initializeMap(columns, rows);
    }

    /**
     * Initializes a 2D representation of the game map
     *
     * @param columns Number of columns
     * @param rows    Number of rows
     */
    private void initializeMap(int columns, int rows) {
        //Iterate through columns/rows and set tiles for every point for 2D array.
        //Initializes all non player tiles to empty.
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {

                Point currentLocation = new Point(x, y);

                //If the current coordinates are the player's location on map
                if (_playerTile.getLocation().equals(currentLocation)) {
                    _entityTiles[x][y] = _playerTile;
                    //If the current coordinates are the opponent's location on map
                } else if (_opponentTile.getLocation().equals(currentLocation)) {
                    _entityTiles[x][y] = _opponentTile;
//                    System.out.println(_entityTiles[x][y].getEntityType() == Entity.EntityType.PLAYER);
                } else {
                    //Grab the empty tile reference at that location
                    TileLayer playerLayer = (TileLayer) this._gameMap.getLayer(PLAYER_LAYER);
                    //Create empty tile in location
                    _entityTiles[x][y] = new EntityTile(currentLocation, new Empty(currentLocation), playerLayer.getTileAt(currentLocation.x, currentLocation.y));
                }
            }
        }
    }

    /**
     * Gets player tiles from Player layer
     * Will need to refactor.  Basically first two objects found are added to list.
     * 0 being player, 1 being opponent
     *
     * @param map
     * @return
     */
    private ArrayList<EntityTile> getPlayerTiles(Map map) {

        //Grab reference to player layer
        TileLayer playerLayer = (TileLayer) map.getLayer(PLAYER_LAYER);

        int height;
        int width;
        try {
            height = playerLayer.getBounds().height;
            width = playerLayer.getBounds().width;
        } catch (NullPointerException npe) {
            playerLayer.setOffset(0, 0);
            height = playerLayer.getBounds().height;
            width = playerLayer.getBounds().width;
        }

        ArrayList<Tile> playerTiles = new ArrayList<>();
        Point playerLocation = null;
        Point opponentLocation = null;

        //Iterate through player layer to find player tiles
        Tile tile;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tile = playerLayer.getTileAt(x, y);
                if (tile == null) {
                    continue;
                } else {
                    //If we haven't found a player yet, then tile found is player
                    if (playerTiles.size() == 0) {
                        playerLocation = new Point(x, y);
                    } else {
                        opponentLocation = new Point(x, y);
                    }
                    playerTiles.add(tile);
                }
            }
        }

        //Grab references
        Tile playerTile = playerTiles.get(0);
        Tile opponentTile = playerTiles.get(1);

        //Add tiles to corresponding entity tile
        EntityTile playerEntityTile = new EntityTile(playerLocation, new Player(0, playerLocation), playerTile);
        EntityTile opponentEntityTile = new EntityTile(opponentLocation, new Player(1, opponentLocation), opponentTile);

        //Build list and return
        ArrayList<EntityTile> playerEntityTiles = new ArrayList<>();
        playerEntityTiles.add(playerEntityTile);
        playerEntityTiles.add(opponentEntityTile);

        return playerEntityTiles;

    }

    /**
     * Gets the player layer where the most of the game is rendered
     *
     * @return PlayerLayer
     */
    private TileLayer getPlayerLayer() {
        ArrayList<MapLayer> layerList = new ArrayList<>(this._gameMap.getLayers());
        return (TileLayer) layerList.get(2);
    }


    /**
     * Moves location of EntityTile to new location.  This updates both Tiled's TileMap and well as our custom game map.
     * If the location to move to is occupied, the EntityTile stays in its current location, or if location is outside of player bounds
     *
     * @param entity           Entity to move
     * @param locationToMoveTo Location to move to
     * @return If EntityTile can move to that location
     */
    public boolean setLocation(EntityTile entity, Point locationToMoveTo) {

        //If outside of player bounds return false
        if (!isInsideMap(locationToMoveTo)) {
            return false;
        }

        //All movement is done on the player layer.  We need to grab a couple of references before we can move location.
        TileLayer playerLayer = getPlayerLayer();
        //Get the current location of the entity
        Point entityCurrentLocation = new Point(entity.getLocation());
        //Reference to the tile the player will be moving to
        EntityTile tileToMoveTo = _entityTiles[locationToMoveTo.x][locationToMoveTo.y];


        //If the tile to move to is empty, then it is a valid move
        if (tileToMoveTo.getEntityType() == Entity.EntityType.EMPTY) {
            //Remove tile from previous location
            playerLayer.removeTile(entity.getTile());
            //Set tile at new location
            playerLayer.setTileAt(locationToMoveTo.x, locationToMoveTo.y, entity.getTile());
            //Now set the entity's stored location to the new location
            entity.getEntity().getLocation().setLocation(locationToMoveTo);

            //TODO Current system switches the tiles that moved with each other.  This is something we will need to change later on.
            //Now lets update the game map with the new location for the player
            _entityTiles[locationToMoveTo.x][locationToMoveTo.y] = entity;
            //Old player location is now switched with the empty tile
            _entityTiles[entityCurrentLocation.x][entityCurrentLocation.y] = tileToMoveTo;
            //Update tiled to store and display the empty tile at the old location.  I don't believe this step is needed, but I we may need it later on in development
            playerLayer.setTileAt(entityCurrentLocation.x, entityCurrentLocation.y, tileToMoveTo.getTile());
            return true;
        } else {
            //Cannot move to a tile that contains another tile
            return false;
        }
    }

    /**
     * Returns if the player can move to the specified location on the map
     * @param location
     * @return
     */
    public boolean canMoveToLocation(Point location){
        return isInsideMap(location) && _entityTiles[location.x][location.y].getEntityType() == Entity.EntityType.EMPTY;
    }

    /**
     * Determines whether a point is inside the bounds of the game map
     * @param point
     * @return
     */
    public boolean isInsideMap(Point point) {
        if (point.x >= this._numberOfColumns || point.x < 0
                || point.y >= this._numberOfRows || point.y < 0) {
            return false;
        }
        return true;
    }

    public ArrayList<EntityTile> getPlayers() {
        return _players;
    }

    public EntityTile[][] getEntityTiles() {
        return _entityTiles;
    }

    public Map getMap(){
        return this._gameMap;
    }

    /**
     * Removes tile at specified location from map
     * @param location
     */
    public void removeTile(Point location){
        EntityTile entityTile = this._entityTiles[location.x][location.y];
        getPlayerLayer().removeTile(entityTile.getTile());
        this._entityTiles[location.x][location.y] = new EntityTile(location, new Empty(location), getPlayerLayer().getTileAt(location.x, location.y));
    }
}

