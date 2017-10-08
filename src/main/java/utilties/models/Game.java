/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package utilties.models;

import common.BaseLogger;
import common.constants.GameStatus;
import org.mapeditor.core.Map;
import utilties.entities.Entity;
import utilties.entities.Player;

import java.awt.*;
import java.util.ArrayList;
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

    //Vectors for Moving
    private static final Point MOVE_LEFT = new Point(DIRECTION_LEFT, DIRECTION_CONSTANT);
    private static final Point MOVE_RIGHT = new Point(DIRECTION_RIGHT, DIRECTION_CONSTANT);
    private static final Point MOVE_DOWN = new Point(DIRECTION_CONSTANT, DIRECTION_DOWN);
    private static final Point MOVE_UP = new Point(DIRECTION_CONSTANT, DIRECTION_UP);


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

    public static final int MAX_TURN_LIMIT = 250;

    //Constant for number of turns to calculate a stalemate
    private static final int NUMBER_OF_TURNS_TO_STALEMATE = 50;

    private static final BaseLogger LOGGER = new BaseLogger("Game");

    /**
     * @param entityMap
     */
    public Game(EntityMap entityMap) {
        this._entityMap = entityMap;
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
        //If the game is not over, check to see if there is a stalemate condition
        if(!isGameOver()) {
            //TODO Will need to update this if we want more players
            boolean isStalemateForPlayer = isStalemateTurnForPlayer(PLAYER_ID);
            boolean isStalemateForOpponent = isStalemateTurnForPlayer(OPPONENT_ID);

            if (isStalemateForPlayer && isStalemateForOpponent) {
                _isStalemate = true;
                this._isGameOver = true;
            } else if (this._numberOfTurnsCompleted > MAX_TURN_LIMIT) {
                _isStalemate = true;
                this._isGameOver = true;
            }

            this._numberOfTurnsCompleted++;
        }
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
     * If opponent is within one tile horizontally or vertically, player will attack that tile.  If not, returns false
     *
     * @param playerId Player
     * @return If player was able to attack
     */
    public boolean attack(int playerId) {
        stopDefending(playerId);
        System.out.println("Attacking location for player ID: " + playerId);
        if (attackLocation(playerId, 0, 1)) {
            return true;
        } else if (attackLocation(playerId, 0, -1)) {
            return true;
        } else if (attackLocation(playerId, 1, 0)) {
            return true;
        } else if (attackLocation(playerId, -1, 0)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Moves one tile closer to opponent.
     *
     * @param playerId
     * @param opponentId
     * @return
     */
    public boolean approach(int playerId, int opponentId) {
        stopDefending(playerId);

        //Calculate distances in X and Y directions
        Point distances = getDeltaDistances(playerId, opponentId);

        //Check if Y is 0 to prevent divide by 0 error.
        if (distances.y == 0) {
            //If negative, move left
            if (distances.x < 0) {
                return move(playerId, DIRECTION_LEFT, DIRECTION_CONSTANT);
            }
            //If positive, move right
            else {
                return move(playerId, DIRECTION_RIGHT, DIRECTION_CONSTANT);
            }
        }

        //Calculate slope
        double slope = (double) distances.x / distances.y;

        //If slope is < .5, then we need to move in Y direction
        //If slope is > .5 then we need to move in X direction
        if (Math.abs(slope) < .5) {
            if (distances.y < 0) {
                return move(playerId, DIRECTION_CONSTANT, DIRECTION_DOWN);
            } else {
                return move(playerId, DIRECTION_CONSTANT, DIRECTION_UP);
            }
        } else {
            if (distances.x < 0) {
                return move(playerId, DIRECTION_LEFT, DIRECTION_CONSTANT);
            } else {
                return move(playerId, DIRECTION_RIGHT, DIRECTION_CONSTANT);
            }
        }
    }

    /**
     * Evades from opposing player.  Will move in direction that is one space away from player.  Will only evade 50% of time
     *
     * @param playerId
     * @param opponentId
     * @return
     */
    public boolean evade(int playerId, int opponentId) {
        stopDefending(playerId);

        if (getRandomBoolean()) {
            return false;
        }

        Player player = getPlayer(playerId);
        Player opponent = getPlayer(opponentId);
        //Calculate distances in X and Y directions

        int currentDistance = pathDistanceToLocation(player.getLocation(), opponent.getLocation());

        //Get a list of potential move locations
        ArrayList<Point> potentialMoveLocations = getAllPossibleMoves(getPlayer(playerId).getLocation());

        Point playerCurrentLocation = getPlayer(playerId).getLocation();

        double longestDistance = -1;
        int longestMoveIndex = -1;
        //Pick the distance that moves farthest from opponent
        for (int i = 0; i < potentialMoveLocations.size(); i++) {

            Point moveLocation = getNewLocation(playerCurrentLocation, potentialMoveLocations.get(i));
            int distanceCandidate = pathDistanceToLocation(moveLocation, opponent.getLocation());
            if (distanceCandidate > longestDistance) {
                longestDistance = distanceCandidate;
                longestMoveIndex = i;
            }
            //Introduce randomness.  If moves have equal distance, then choose randomly so we don't evade to the same location every turn
            if(distanceCandidate == longestDistance){
                if(getRandomBoolean()){
                    longestDistance = distanceCandidate;
                    longestMoveIndex = i;
                }
            }
        }

        //If the new location is farther than the old location, we move
        if (longestDistance > currentDistance) {
            return move(playerId, potentialMoveLocations.get(longestMoveIndex).x, potentialMoveLocations.get(longestMoveIndex).y);
        }

        return false;
    }

    /** Dodges from opposing player.  Currently not implemented
     *
     * @param playerId
     * @param opponentId
     * @return
     */

    public boolean dodge(int playerId, int opponentId){
        stopDefending(playerId);
        return false;
    }

    /**
     * Heals the players to a maximum of 100.  Adds specified health to the player
     * @param playerId
     * @param health
     */
    public void heal(int playerId, int health) {
        stopDefending(playerId);
        Player player = this.getPlayer(playerId);
        if (player.getHealth() + health > HEALTH_MAX) {
            player.setHealth(HEALTH_MAX);
        } else {
            player.setHealth(player.getHealth() + health);
        }
    }

    /**
     * Gets all possible moves at a given location
     * @param location
     * @return
     */
    private ArrayList<Point> getAllPossibleMoves(Point location){
        ArrayList<Point> potentialMoveDirections = new ArrayList<>();

        if(_entityMap.canMoveToLocation(getNewLocation(location, MOVE_UP))){
            potentialMoveDirections.add(MOVE_UP);
        }
        if(_entityMap.canMoveToLocation(getNewLocation(location, MOVE_DOWN))){
            potentialMoveDirections.add(MOVE_DOWN);
        }
        if(_entityMap.canMoveToLocation(getNewLocation(location, MOVE_RIGHT))){
            potentialMoveDirections.add(MOVE_RIGHT);
        }
        if(_entityMap.canMoveToLocation(getNewLocation(location, MOVE_LEFT))){
            potentialMoveDirections.add(MOVE_LEFT);
        }
        return potentialMoveDirections;
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
     * Sets the players state to defending and sets shield strength to specified value. When another command is used this is set to false
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
     * Sets the players state to stop defending
     *
     * @param playerId
     */
    private void stopDefending(int playerId) {
        this.getPlayer(playerId).setShielding(false);
    }

    /**
     * Attacks location x and y away from players current location
     *
     * @param playerId Player
     * @param x        Units away from players current location
     * @param y        Units away from players current location
     * @return If player was able to attack
     */
    private boolean attackLocation(int playerId, int x, int y) {
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

            if (playerToAttack.isShielding()) {
                damageToBeDone = player.getDamage() - playerToAttack.getShieldStrength();
            }

            playerToAttack.setHealth(playerToAttack.getHealth() - damageToBeDone);
            if (playerToAttack.getHealth() <= HEALTH_DEAD) {
                playerToAttack.setHealth(0);
                this._isGameOver = true;
            }

            return true;
        }
    }


    private Point getNewLocation(Point currentLocation, Point vector){
        return new Point(currentLocation.x + vector.x, currentLocation.y + vector.y);
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
        return distance(playerLocation, opponentLocation);
    }


    public double distance(Point location1, Point location2){
       return Math.hypot(location1.x - location2.x, location1.y - location2.y);
    }

    /**
     * Number of tiles to get to opponent using only horizontal and vertical movement
     * @param playerId
     * @param opponentId
     * @return
     */
    public int pathDistanceToPlayer(int playerId, int opponentId){
        //Calculate distances in X and Y directions
        Point distances = getDeltaDistances(playerId, opponentId);
        //Subtract one because you can never get to players actual location, only the closest tile surrounding it
        return Math.abs(distances.x) + Math.abs(distances.y);
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

    public int pathDistanceToLocation(Point location1, Point location2){
        //Calculate distances in X and Y directions
        int distanceX = location1.x - location2.x;
        int distanceY = location1.y - location2.y;
        return (Math.abs(distanceX) + Math.abs(distanceY)) - 1;
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
     * Is the game a stalemate
     * @return
     */
    public boolean isStalemate(){
        return this._isStalemate;
    }

    /**
     * Used by external classes to acquire game state.
     * @return The GameStatus pertaining to the current state of the game.
     */
    public GameStatus getState() {
        if (!isGameOver()) {
            //LOGGER.critical("No way to distinguish between RUNNING and INACTIVE game states yet (TODO SEAN).");
            return GameStatus.RUNNING;
        }

        if (isStalemate()) {
            return GameStatus.STALEMATE;
        }

        LOGGER.warning("Cannot determine which player has won easily from the getState() method. Needs helper methods.");
        if (isDead(0)) {
            return GameStatus.LOST;
        }
        else if (isDead(1)) {
            System.out.println(getPlayer(1).getHealth());
            return GameStatus.WON;
        }
        throw new RuntimeException("CANNOT DETERMINE GAME STATE.");
    }

    /**
     * Checks to see whether or not the current player has been in their location for NUMBER_OF_TURNS_TO_STALEMATE
     * @param playerId
     * @return If the users has not moved in NUMBER_OF_TURNS_TO_STALEMATE
     */
    private boolean isStalemateTurnForPlayer(int playerId){

        if(_previousLocations.get(playerId).x == getPlayer(playerId).getLocation().x && _previousLocations.get(playerId).y == getPlayer(playerId).getLocation().y){
                _numberOfTimesAtCurrentLocation.set(playerId, _numberOfTimesAtCurrentLocation.get(playerId) + 1);

                if(_numberOfTimesAtCurrentLocation.get(playerId) > NUMBER_OF_TURNS_TO_STALEMATE){
                    return true;
                }
                else{
                    _previousLocations.set(playerId, new Point(getPlayer(playerId).getLocation()));
                }
            }
            else {
                _previousLocations.set(playerId, new Point(getPlayer(playerId).getLocation()));
                _numberOfTimesAtCurrentLocation.set(playerId, 1);
            }
            return false;
    }

    //TODO Utilize Seed generated from engine
    /**
     * Is the game over
     * @return
     */
    public boolean isGameOver(){
        return this._isGameOver;
    }

    //TODO Utilize Seed generated from engine
    /**
     *
     * @return Random Movement Value
     */
    public int pickRandomElement(int listSize){
        int seed = ThreadLocalRandom.current().nextInt(0, listSize);
        System.out.println(seed);
        System.out.println(listSize);
        return seed;
    }

    //TODO Utilize Seed generated from engine
    public boolean getRandomBoolean() {
        return ThreadLocalRandom.current().nextInt(0, 2) == 1;
    }

}
