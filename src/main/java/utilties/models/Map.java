package main.java.utilties.models;

import main.java.utilties.entities.Empty;
import main.java.utilties.entities.IEntity;

public class Map {

    private int numberOfColumns;
    private int numberOfRows;

    private Tile[][] map;

    public Map(int numberOfColumns, int numberOfRows){
        this.numberOfColumns = numberOfColumns;
        this.numberOfRows = numberOfRows;
        initializeMap();
    }

    private void initializeMap() {
        map = new Tile[numberOfColumns][numberOfRows];

        for (int x = 0; x < numberOfColumns; x++) {
            for (int y = 0; y < numberOfRows; y++) {
                Location location = new Location(x, y);
                map[x][y] = createBlankTile(location);
            }
        }
    }

    public int getNumberOfColumns(){
        return numberOfColumns;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setLocation(IEntity iEntity, Location location){
        Location previousLocation = iEntity.getLocation();
        Tile previousTile = getTile(location);
        iEntity.setLocation(location);
        setTile(location, previousTile);
        setTile(previousLocation, createBlankTile(location));
    }

    public void setTile(Location location, Tile tile){
        map[location.getX()][location.getY()] = tile;
    }

    public Tile getTile(Location location){
        return map[location.getX()][location.getY()];
    }

    public boolean isTileOccupied(Location location){
        if(getTile(location).getEntityType() == IEntity.EntityType.EMPTY){
            return  true;
        }
        return false;
    }

    private Tile createBlankTile(Location location){
        return new Tile(location, new Empty(location));
    }

    public Tile[][] getMap() {
        return this.map;
    }
}
