package utilties.models;

/**
 * Model class for Location.  Contains X and Y coordinates
 */
public class Location {

    private int x;
    private int y;

    /**
     * Creates location based on X and Y coordinates
     * @param x X coordinate
     * @param y Y coordinate
     */
    public Location(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for X
     * @return X
     */
    public int getX(){
        return this.x;
    }

    /**
     * Getter for Y
     * @return Y
     */
    public int getY(){
        return this.y;
    }
}
