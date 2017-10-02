package utilties.models;

import java.awt.*;

/**
 * Model for Game.  Execute nextTurn to progress through game.
 */
public class Game {

    private EntityMap _entityMap;
    private int _numberOfTurnsCompleted = 0;

    /**
     * Gets EntityMap object
     * @return EntityMap
     */
    public EntityMap getEntityMap() {
        return _entityMap;
    }

    /**
     * Computes all game events for next turn.  Returns updated entityMap.
     * @return Updated EntityMap
     */
    public EntityMap nextTurn(){
        int playerX = this._entityMap.getPlayerTile().getLocation().x;
        int playerY = this._entityMap.getPlayerTile().getLocation().y;

        this._entityMap.setLocation(_entityMap.getPlayerTile(), new Point(playerX + 1, playerY));

        return this._entityMap;
    }

    /**
     *
     * @param entityMap
     */
    public Game(EntityMap entityMap){
        this._entityMap = entityMap;
    }

    /**
     * Gets count of number of turns that game has gone through
     * @return
     */
    public int getNumberOfTurnsCompleted() {
        return _numberOfTurnsCompleted;
    }

}

