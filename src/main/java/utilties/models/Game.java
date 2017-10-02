package utilties.models;

import utilties.entities.Entity;
import utilties.entities.Player;

import java.awt.*;

/**
 * Model for Game.  Execute nextTurn to progress through game.
 */
public class Game {

    private EntityMap _entityMap;
    private int _numberOfTurnsCompleted = 0;

    /**
     *
     * @param entityMap
     */
    public Game(EntityMap entityMap){
        this._entityMap = entityMap;
    }

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

        this._numberOfTurnsCompleted += 1;
        return this._entityMap;
    }

    /**
     * Moves to location x and y away from players current location
     * @param playerId Player
     * @param x Units away from players current location
     * @param y Units away from players current location
     * @return If the player was able to move
     */
    public boolean move(int playerId, int x, int y){
        EntityTile player = this._entityMap.getPlayers().get(playerId);
        Point location = player.getLocation();
        return this._entityMap.setLocation(player, new Point(location.x + x, location.y + y));
    }

    /**
     * Attacks location x and y away from players current location
     * @param playerId Player
     * @param x Units away from players current location
     * @param y Units away from players current location
     * @return If player was able to attack
     */
    public boolean attack(int playerId, int x, int y){
        EntityTile playerTile = this._entityMap.getPlayers().get(playerId);
        Point location = playerTile.getLocation();

        //If point to attack is outside map, return false
        if(!_entityMap.isInsideMap(new Point(location.x + x, location.y + y))){
            return false;
        }
        Entity entity = getEntityAtLocation(new Point (location.x + x, location.y + y));

        Player player = (Player) playerTile.getEntity();

        if(entity.getEntityType() == Entity.EntityType.EMPTY){
            return false;
        }
        else{
            Player playerToAttack = (Player) entity;
            playerToAttack.setHealth(playerToAttack.getHealth() - player.getDamage());
            if(playerToAttack.getHealth() <=0){
                //TODO Victory Screen of some sort
                System.out.println("PLAYER " + playerId + " has won");
            }
            return true;
        }
    }

    public Entity getEntityAtLocation(Point location){
        return this._entityMap.getEntityTiles()[location.x][location.y].getEntity();
    }

    /**
     * Gets count of number of turns that game has gone through
     * @return
     */
    public int getNumberOfTurnsCompleted() {
        return _numberOfTurnsCompleted;
    }

    /**
     * Gets player based on id
     * @param id
     * @return
     */
    public Player getPlayer(int id){
        return (Player) this._entityMap.getPlayers().get(id).getEntity();
    }

    /**
     * Returns a double of the distance between the players specified
     * @param playerId
     * @param opponentId
     * @return
     */
    public double distanceToOpponent(int playerId, int opponentId){
        Point playerLocation = getPlayer(playerId).getLocation();
        Point opponentLocation = getPlayer(opponentId).getLocation();
        return Math.hypot(playerLocation.x - opponentLocation.x, playerLocation.y - opponentLocation.y);
    }
}

