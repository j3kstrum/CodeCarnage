package utilties.models;

import utilties.entities.Empty;
import utilties.entities.IEntity;

public class Map {

    private int numberOfColumns;
    private int numberOfRows;

    private Tile[][] map;

    public Map(int numberOfColumns, int numberOfRows){
        this.numberOfColumns = numberOfColumns;
        this.numberOfRows = numberOfRows;
        initializeMap();
    }


    public boolean isOccupied(Location location){
        if(getTile(location).getEntityType() == IEntity.EntityType.EMPTY){
            return  true;
        }
        return false;
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

    private void initializeMap() {
        map = new Tile[numberOfColumns][numberOfRows];

        for (int x = 0; x < numberOfColumns; x++) {
            for (int y = 0; y < numberOfRows; y++) {
                Location location = new Location(x, y);
                map[x][y] = createBlankTile(location);
            }
        }
    }

    private void setTile(Location location, Tile tile){
        map[location.getX()][location.getY()] = tile;
    }

    private Tile getTile(Location location){
        return map[location.getX()][location.getY()];
    }

    private Tile createBlankTile(Location location){
        return new Tile(location, new Empty(location));
    }

    public Tile[][] getMap() {
        return this.map;
    }
}
