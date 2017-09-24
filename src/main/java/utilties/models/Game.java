package utilties.models;

/**
 * Model for Game.  Execute nextTurn to progress through game.
 */
public class Game {

    private EntityMap entityMap;

    public Game(EntityMap entityMap){
        this.entityMap = entityMap;
    }

    /**
     * Gets EntityMap object
     * @return EntityMap
     */
    public EntityMap getEntityMap() {
        return entityMap;
    }

    /**
     * Computes all game events for next turn.  Returns updated entityMap.
     * @return Updated EntityMap
     */
    public EntityMap nextTurn(){
        int playerX = this.entityMap.getPlayerTile().getLocation().getX();
        int playerY = this.entityMap.getPlayerTile().getLocation().getY();
        this.entityMap.setLocation(0, new Location(playerX + 1, playerY));

        if(playerX > 15) {
            int opponentX = this.entityMap.getOpponentTile().getLocation().getX();
            int opponentY = this.entityMap.getOpponentTile().getLocation().getY();
            this.entityMap.setLocation(1, new Location(opponentX, opponentY + 1));

        }

        return entityMap;
    }
}

