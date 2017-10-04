/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package utilties.models;

import org.mapeditor.core.Map;
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
     * @param entityMap
     */
    public Game(EntityMap entityMap) {
        this._entityMap = entityMap;
    }

    /**
     * Gets EntityMap object
     *
     * @return EntityMap
     */
    public EntityMap getEntityMap() {
        return _entityMap;
    }


    //TODO How are we going to implement scripting?

    /**
     * Returns updated entityMap.
     *
     * @return Updated EntityMap
     */
    public Map nextTurn() {
        this._numberOfTurnsCompleted++;
        System.out.println("Returning the next map...");
        return null;
//        return this._entityMap;
    }

    /**
     * Moves to location x and y away from players current location
     *
     * @param playerId Player
     * @param x        Units away from players current location
     * @param y        Units away from players current location
     * @return If the player was able to move
     */
    public boolean move(int playerId, int x, int y) {
        EntityTile player = this._entityMap.getPlayers().get(playerId);
        Point location = player.getLocation();
        return this._entityMap.setLocation(player, new Point(location.x + x, location.y + y));
    }

    /**
     * Attacks location x and y away from players current location
     *
     * @param playerId Player
     * @param x        Units away from players current location
     * @param y        Units away from players current location
     * @return If player was able to attack
     */
    public boolean attack(int playerId, int x, int y) {
        EntityTile playerTile = this._entityMap.getPlayers().get(playerId);
        Point location = playerTile.getLocation();

        //If point to attack is outside map, return false
        if (!_entityMap.isInsideMap(new Point(location.x + x, location.y + y))) {
            return false;
        }
        Entity entity = getEntityAtLocation(new Point(location.x + x, location.y + y));

        Player player = (Player) playerTile.getEntity();

        if (entity.getEntityType() == Entity.EntityType.EMPTY) {
            return false;
        } else {
            Player playerToAttack = (Player) entity;
            playerToAttack.setHealth(playerToAttack.getHealth() - player.getDamage());
            if (playerToAttack.getHealth() <= 0) {
                //TODO Victory Screen of some sort
                System.out.println("PLAYER " + playerId + " has won");
            }
            return true;
        }
    }

    /**
     * Sets the players state to defending
     *
     * @param playerId
     */
    public void defend(int playerId) {
        this.getPlayer(playerId).setShielding(true);
    }

    /**
     * Sets the players state to stop defending
     *
     * @param playerId
     */
    public void stopDefending(int playerId) {
        this.getPlayer(playerId).setShielding(false);
    }

    /**
     * Heals the players to a maximum of 100.  Adds 3 health to the player
     *
     * @param playerId
     */
    public void heal(int playerId) {
        Player player = this.getPlayer(playerId);
        if (player.getHealth() + 3 > 100) {
            player.setHealth(100);
        } else {
            player.setHealth(player.getHealth() + 3);
        }
    }

    /**
     * Gets entity at specified location
     *
     * @param location
     * @return
     */
    public Entity getEntityAtLocation(Point location) {
        return this._entityMap.getEntityTiles()[location.x][location.y].getEntity();
    }

    /**
     * Gets count of number of turns that game has gone through
     *
     * @return
     */
    public int getNumberOfTurnsCompleted() {
        return _numberOfTurnsCompleted;
    }

    /**
     * Gets player based on id
     *
     * @param id
     * @return
     */
    public Player getPlayer(int id) {
        return (Player) this._entityMap.getPlayers().get(id).getEntity();
    }

    /**
     * Returns a double of the distance between the players specified
     *
     * @param playerId
     * @param opponentId
     * @return
     */
    public double distanceToOpponent(int playerId, int opponentId) {
        Point playerLocation = getPlayer(playerId).getLocation();
        Point opponentLocation = getPlayer(opponentId).getLocation();
        return Math.hypot(playerLocation.x - opponentLocation.x, playerLocation.y - opponentLocation.y);
    }

}

