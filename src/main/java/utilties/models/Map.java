package utilties.models;

import utilties.entities.Empty;
import utilties.entities.Entity;

/**
 * Map in which Game is Played on.  Represented as a two dimensional array of tiles.
 */
public class Map {

    private int numberOfColumns;
    private int numberOfRows;

    private Tile[][] map;

    /**
     * Creates Map with specifed amount of columns and rows.  Initializes tiles as Empty.
     * @param numberOfColumns Number of columns for map
     * @param numberOfRows Number of rows for map
     */
    public Map(int numberOfColumns, int numberOfRows){
        this.numberOfColumns = numberOfColumns;
        this.numberOfRows = numberOfRows;
        initializeMap();
    }

    /**
     * Initializes map.  Adds an Empty Entity for each tile of map
     */
    private void initializeMap() {
        map = new Tile[numberOfColumns][numberOfRows];

        for (int x = 0; x < numberOfColumns; x++) {
            for (int y = 0; y < numberOfRows; y++) {
                Location location = new Location(x, y);
                map[x][y] = createBlankTile(location);
            }
        }
    }

    /**
     * Getter for number of columns in Map
     * @return NumberOfColumns
     */
    public int getNumberOfColumns(){
        return numberOfColumns;
    }

    /**
     * Getter for number of rows in Map
     * @return NumberOfRows
     */
    public int getNumberOfRows() {
        return numberOfRows;
    }

    /**
     * Takes in entity and places in on new location on map.  Previous location is replaced with Empty Entity.
     * @param entity Entity to be moved
     * @param location Location to move entity
     */
    public void setLocation(Entity entity, Location location){

        //Grab the previous location.  Move entity and tile to new location.  Fill spot with new Empty Entity.
        Location previousLocation = entity.getLocation();
        Tile previousTile = getTile(location);
        entity.setLocation(location);
        setTile(location, previousTile);
        setTile(previousLocation, createBlankTile(location));
    }

    /**
     * Set the tile at a specific location
     * @param location Location of tile
     * @param tile Tile Object to Add
     */
    public void setTile(Location location, Tile tile){
        map[location.getX()][location.getY()] = tile;
    }

    /**
     * Get Tile as specified location
     * @param location Location of Tile
     * @return Tile
     */
    public Tile getTile(Location location){
        return map[location.getX()][location.getY()];
    }

    /**
     * Is the location occupied
     * @param location
     * @return True if tile at location is not an Empty Entity.  False otherwise
     */
    public boolean isLocationOccupied(Location location){
        if(getTile(location).getEntityType() == Entity.EntityType.EMPTY){
            return  true;
        }
        return false;
    }

    /**
     * Creates a blank tile at specified location
     * @param location Location of Tile
     * @return Blank tile that was created
     */
    private Tile createBlankTile(Location location){
        return new Tile(location, new Empty(location));
    }


    /**
     * Get Map for Game
     * @return Map
     */
    public Tile[][] getMap() {
        return this.map;
    }
}
