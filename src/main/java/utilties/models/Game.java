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
import java.util.ArrayList;

/**
 * Model for Game.  Execute nextTurn to progress through game.
 */
public class Game {

    private EntityMap _entityMap;
    private int _numberOfTurnsCompleted = 0;
    private ArrayList<Point> _previousLocations = new ArrayList<>();
    private ArrayList<Integer> _numberOfTimesAtCurrentLocation = new ArrayList<>();
    private boolean _isStalemate = false;
    private boolean _isGameOver = false;

    /**
     * @param entityMap
     */
    public Game(EntityMap entityMap) {
        this._entityMap = entityMap;
        move(0, 10,5);
        move(1,-10,-5);
        _previousLocations.add(getPlayer(0).getLocation());
        _previousLocations.add(getPlayer(1).getLocation());
        _numberOfTimesAtCurrentLocation.add(1);
        _numberOfTimesAtCurrentLocation.add(1);
    }

    /**
     * Gets EntityMap object
     *
     * @return EntityMap
     */
    public EntityMap getEntityMap() {
        return _entityMap;
    }


    /**
     * Returns updated entityMap.
     *
     * @return Updated EntityMap
     */
    public Map nextTurn() {
        if(isStalemateTurnForPlayer(0) && isStalemateTurnForPlayer(1)){
            _isStalemate = true;
        }
        this._numberOfTurnsCompleted++;
        return this._entityMap.getMap();
    }

    /**
     * Moves to location x and y away from players current location.
     *
     * @param playerId Player
     * @param x        Units away from players current location
     * @param y        Units away from players current location
     * @return If the player was able to move
     */
    public boolean move(int playerId, int x, int y) {
        getPlayer(playerId).setShielding(false);
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
        player.setShielding(false);

        if (entity.getEntityType() == Entity.EntityType.EMPTY) {
            return false;
        } else {
            Player playerToAttack = (Player) entity;
            int damageToBeDone = player.getDamage();

            if(playerToAttack.isShielding()){
                damageToBeDone = player.getDamage() - playerToAttack.getShieldStrength();
            }

            playerToAttack.setHealth(playerToAttack.getHealth() - damageToBeDone);
            if (playerToAttack.getHealth() <= 0) {
                this._isGameOver = false;
            }

            return true;
        }
    }

    /**
     * Sets the players state to defending.  When another command is used this is set to false
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
     * Heals the players to a maximum of 100.  Adds specified health to the player
     * @param playerId
     * @param health
     */
    public void heal(int playerId, int health) {
        this.getPlayer(playerId).setShielding(false);
        Player player = this.getPlayer(playerId);
        if (player.getHealth() + health > 100) {
            player.setHealth(100);
        } else {
            player.setHealth(player.getHealth() + health);
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
     * Returns a double of the distance between the players specified.  This includes diagonal distance
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

    /**
     * Number of tiles to get to player using only horizontal and vertical movement
     * @param playerId
     * @param opponentId
     * @return
     */
    public int pathDistanceToPlayer(int playerId, int opponentId){
        Point locationOfPlayer = getPlayer(playerId).getLocation();
        Point locationOfOpponent = getPlayer(opponentId).getLocation();

        int distanceX = locationOfOpponent.x - locationOfPlayer.x;
        int distanceY = locationOfOpponent.y - locationOfPlayer.y;

        return Math.abs(distanceX) + Math.abs(distanceY);
    }

    /**
     * Moves one tile closer to player.
     * @param playerId
     * @param opponentId
     * @return
     */
    public boolean moveCloserTo(int playerId, int opponentId){
        getPlayer(playerId).setShielding(false);
        Point locationOfPlayer = getPlayer(playerId).getLocation();
        Point locationOfOpponent = getPlayer(opponentId).getLocation();

        int distanceX = locationOfOpponent.x - locationOfPlayer.x;
        int distanceY = locationOfOpponent.y - locationOfPlayer.y;

        if(distanceY == 0){
            if(distanceX<0){
                return move(playerId, -1,0);
            }
            else{
                return move(playerId, 1,0);
            }
        }
        else if(distanceX == 0) {
            if(distanceY<0){
                return move(playerId, 0,-1);
            }
            else{
                return move(playerId, 0,1);
            }
        }

        double slope = (double) distanceX / distanceY;


        if(Math.abs(slope) < .5){
            if(distanceY<0){
                return move(playerId, 0,-1);
            }
            else{
                return move(playerId, 0,1);
            }
        }
        else {
            if(distanceX<0){
                return move(playerId, -1,0);
            }
            else{
                return move(playerId, 1,0);
            }
        }
    }

    /**
     * Is the player dead
     * @param playerId
     * @return
     */
    public boolean isDead(int playerId){
        if(getPlayer(playerId).getHealth() <=0){
            return true;
        }
        return false;
    }

    /**
     * Sets defending state.  When another command is called defending state is set to false
     * @param playerId
     * @param shieldStrength
     */
    public void defend(int playerId, int shieldStrength){
        getPlayer(playerId).setShielding(true);
        getPlayer(playerId).setShieldStrength(shieldStrength);
    }

    /**
     * Command to do nothing
     */
    public void doNothing(int playerId){
        getPlayer(playerId).setShielding(false);
        return;
    }

    /**
     * Is the game a stalemate
     * @return
     */
    public boolean isStalemate(){
        return this._isStalemate;
    }

    /**
     * Checks to see whether or not the current player has been in their location for 10 turns
     * @param playerId
     * @return
     */
    private boolean isStalemateTurnForPlayer(int playerId){
            if(_previousLocations.get(playerId) == getPlayer(playerId).getLocation()){
                _numberOfTimesAtCurrentLocation.set(playerId, _numberOfTimesAtCurrentLocation.get(playerId) + 1);

                if(_numberOfTimesAtCurrentLocation.get(playerId) > 10){
                    return true;
                }
            }
            else {
                _previousLocations.set(playerId, getPlayer(playerId).getLocation());
                _numberOfTimesAtCurrentLocation.set(playerId, 1);
            }
            return false;
    }

    /**
     * Is the game over
     * @return
     */
    public boolean isGameOver(){
        return this._isGameOver;
    }
}
