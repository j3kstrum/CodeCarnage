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
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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

    //Constants
    //Directions
    public static final int DIRECTION_RIGHT = 1;
    public static final int DIRECTION_LEFT = -1;
    public static final int DIRECTION_UP = 1;
    public static final int DIRECTION_DOWN = -1;
    public static final int DIRECTION_CONSTANT = 0;

    //List of random directions we can move to.  Will stay constant 50%, and move randomly one direction 50% of time
    public static final ArrayList<Integer> DIRECTIONS_RANDOM_MOVEMENT = new ArrayList() {{
        add(DIRECTION_UP);
        add(DIRECTION_CONSTANT);
        add(DIRECTION_CONSTANT);
        add(DIRECTION_CONSTANT);
        add(DIRECTION_DOWN);
    }};

    //Players
    public static final int HEALTH_DEAD = 0;
    public static final int HEALTH_MAX = 100;
    public static final int PLAYER_ID = 0;
    public static final int OPPONENT_ID = 1;

    //Constant for number of turns to calculate a stalemate
    private static final int NUMBER_OF_TURNS_TO_STALEMATE = 10;


    /**
     * @param entityMap
     */
    public Game(EntityMap entityMap) {
        this._entityMap = entityMap;
        //this.getPlayer(0).setLocation(new Point(1, 1));
        _previousLocations.add(getPlayer(PLAYER_ID).getLocation());
        _previousLocations.add(getPlayer(OPPONENT_ID).getLocation());
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
        //TODO Will need to update this if we want more players
        if(isStalemateTurnForPlayer(PLAYER_ID) && isStalemateTurnForPlayer(OPPONENT_ID)){
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
            if (playerToAttack.getHealth() <= HEALTH_DEAD) {
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
     * Sets defending state.  When another command is called defending state is set to false
     * @param playerId
     * @param shieldStrength
     */
    public void defend(int playerId, int shieldStrength){
        getPlayer(playerId).setShielding(true);
        getPlayer(playerId).setShieldStrength(shieldStrength);
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
        if (player.getHealth() + health > HEALTH_MAX) {
            player.setHealth(HEALTH_MAX);
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
        //Calculate distances in X and Y directions
        Point distances = getDeltaDistances(playerId, opponentId);
        //Subtract one because you can never get to players actual location, only the closest tile surrounding it
        return (Math.abs(distances.x) + Math.abs(distances.y)) - 1;
    }

    /**
     * Moves one tile closer to player.
     * @param playerId
     * @param opponentId
     * @return
     */
    public boolean approach(int playerId, int opponentId){
        getPlayer(playerId).setShielding(false);

        //Calculate distances in X and Y directions
        Point distances = getDeltaDistances(playerId, opponentId);

        //If Y distance is 0, then we only need to move in X directions
        if(distances.y == 0){
            //If negative, move left
            if(distances.x<0){
                return move(playerId, DIRECTION_LEFT, DIRECTION_CONSTANT);
            }
            //If positive, move right
            else{
                return move(playerId, DIRECTION_RIGHT,DIRECTION_CONSTANT);
            }
        }
        //Repeat steps when X distance = 0
        else if(distances.x == 0) {
            if(distances.y<0){
                return move(playerId, DIRECTION_CONSTANT ,DIRECTION_DOWN);
            }
            else{
                return move(playerId, DIRECTION_CONSTANT ,DIRECTION_UP);
            }
        }

        //Calculate slope
        double slope = (double) distances.x / distances.y;

        //If slope is < .5, then we need to move in Y direction
        //If slope is > .5 then we need to move in X direction
        if(Math.abs(slope) < .5){
            if(distances.y<0){
                return move(playerId, DIRECTION_CONSTANT ,DIRECTION_DOWN);
            }
            else{
                return move(playerId, DIRECTION_CONSTANT ,DIRECTION_UP);
            }
        }
        else {
            if(distances.x<0){
                return move(playerId, DIRECTION_LEFT, DIRECTION_CONSTANT);
            }
            else{
                return move(playerId, DIRECTION_RIGHT,DIRECTION_CONSTANT);
            }
        }
    }

    /**
     * Returns a point(should be a vector) of distances in x and y coordinates between player and opponent
     * @param playerId
     * @param opponentId
     * @return
     */
    private Point getDeltaDistances(int playerId, int opponentId){
        //Get locations of players
        Point locationOfPlayer = getPlayer(playerId).getLocation();
        Point locationOfOpponent = getPlayer(opponentId).getLocation();

        //Calculate distances in X and Y directions
        int distanceX = locationOfOpponent.x - locationOfPlayer.x;
        int distanceY = locationOfOpponent.y - locationOfPlayer.y;

        return new Point(distanceX, distanceY);

    }

    /**
     * Is the player dead
     * @param playerId
     * @return
     */
    public boolean isDead(int playerId){
        if(getPlayer(playerId).getHealth() <= HEALTH_DEAD){
            return true;
        }
        return false;
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
     * Checks to see whether or not the current player has been in their location for NUMBER_OF_TURNS_TO_STALEMATE
     * @param playerId
     * @return
     */
    private boolean isStalemateTurnForPlayer(int playerId){
            if(_previousLocations.get(playerId) == getPlayer(playerId).getLocation()){
                _numberOfTimesAtCurrentLocation.set(playerId, _numberOfTimesAtCurrentLocation.get(playerId) + 1);

                if(_numberOfTimesAtCurrentLocation.get(playerId) > NUMBER_OF_TURNS_TO_STALEMATE){
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

    public boolean retreat(int playerId, int opponentId){
        getPlayer(playerId).setShielding(false);

        //Calculate distances in X and Y directions
        Point distances = getDeltaDistances(playerId, opponentId);

        //If Y distance is 0, then we only need to move in X directions

        //Should we have one direction stay the same?  Introduces randomness

        int randomMovement = generateRandomMovement();

        if(getRandomBoolean()){
            return move(playerId, randomMovement, DIRECTION_CONSTANT);
        }
        else{
            return move(playerId, DIRECTION_CONSTANT, randomMovement);
        }
    }

    /**
     *
     * @return Random Movement Value
     */
    public int generateRandomMovement(){
        int seed =  ThreadLocalRandom.current().nextInt(0, 4 + 1);
        //System.out.println(seed);
        int direction = DIRECTIONS_RANDOM_MOVEMENT.get(seed);
        return direction;
    }

    public boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }

}
