package utilties.models;

/**
 * Model for Game.  Execute nextTurn to progress through game.
 */
public class Game {

    private EntityMap _entityMap;
    private boolean _hasReachedEnd = true;
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
        int playerX = this._entityMap.getPlayerTile().getLocation().getX();
        int playerY = this._entityMap.getPlayerTile().getLocation().getY();

        if(this._hasReachedEnd){
            if(playerX == 1){
                this._hasReachedEnd = false;
            }
            this._entityMap.setLocation(0, new Location(playerX - 1, playerY));

        }
        else {
            if(playerX == 24){
                this._hasReachedEnd = true;
            }
            this._entityMap.setLocation(0, new Location(playerX + 1, playerY));
        }

        int opponentX = this._entityMap.getOpponentTile().getLocation().getX();
        int opponentY = this._entityMap.getOpponentTile().getLocation().getY();


        if(opponentX - playerX == 2){
            this._entityMap.setLocation(1, new Location(opponentX, opponentY + 1));
        }
        else if(opponentX - playerX == -1){
            this._entityMap.setLocation(1, new Location(opponentX, opponentY - 1));
        }
        _numberOfTurnsCompleted++;

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

