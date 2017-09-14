package utilties.models;

import utilties.models.entities.Empty;

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

    private void initializeMap() {
        map = new Tile[numberOfColumns][numberOfRows];

        for (int x = 0; x < numberOfColumns; x++) {
            for (int y = 0; y < numberOfRows; y++) {
                Location location = new Location(x, y);
                map[x][y] = new Tile(location, new Empty(location));
            }
        }
    }
}
