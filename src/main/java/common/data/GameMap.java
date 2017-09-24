package common.data;

/**
 * The class representing the Game EntityMap.
 * This will contain all pertinent data and could eventually be used as a template for a
 * "GameMapDelta" object for efficiency.
 *
 * @author jacob.ekstrum
 */
public class GameMap {
    // TODO: Populate. I think Sean started some work on this.

    private org.mapeditor.core.Map _staticMap;

    public GameMap(org.mapeditor.core.Map tiledMap) {
        this._staticMap = tiledMap;
    }
}
