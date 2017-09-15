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

    public int getNumberOfColumns(){
        return numberOfColumns;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setLocation(IEntity iEntity, Location location){
        Location previousLocation = iEntity.getLocation();
        Tile previousTile = map[previousLocation.getX()][previousLocation.getY()];
        iEntity.setLocation(location);
        map[location.getX()][location.getY()] = previousTile;
        map[previousLocation.getX()][previousLocation.getY()] = createBlankTile(location);
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

    private Tile createBlankTile(Location location){
        return new Tile(location, new Empty(location));
    }
}
