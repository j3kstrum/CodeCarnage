package utilties.models;

import utilties.entities.Entity;
import utilties.entities.Player;
import utilties.scripting.PlayerFunctions;

/**
 * Model for Game.  Execute nextTurn to progress through game.
 */
public class Game {

    private Map map;

    private Player player;
    private Player opponent;

    private boolean isGameComplete;

    public Map getMap() {
        return map;
    }

    public Game(){
        initializeGame();
    }

    private void initializeGame(){

        isGameComplete = false;

        //Create Map
        map = new Map(20,20);

        //Set Locations For Players
        Location playerLocation = new Location(0,0);
        Location opponentLocation = new Location(19,19);


        //Create Tiles for Players
        Tile playerTile = new Tile(playerLocation, new Player(1, playerLocation));
        Tile opponentTile = new Tile(opponentLocation, new Player(2, opponentLocation));

        //Set tiles on Map object
        map.setTile(playerLocation, playerTile);
        map.setTile(opponentLocation, opponentTile);
    }

    /**
     * Computes all game events for next turn.  Returns updated map.
     * @return Updated Map
     */
    public Map nextTurn(){
        PlayerFunctions.Move(map, new Location(player.getLocation().getX() + 1, player.getLocation().getY()), player);
        PlayerFunctions.Move(map, new Location(opponent.getLocation().getX() - 1, opponent.getLocation().getY()), opponent);
        return map;
    }

    /**
     * Getter for Player
     * @return Player
     */
    public Entity getPlayer() {
        return player;
    }

    /**
     * Getter for Opponent
     * @return Opponent
     */
    public Entity getOpponent() {
        return opponent;
    }

    /**
     * Getter for GameCompletion
     * @return Is Game Complete
     */
    public boolean isGameComplete() {
        return isGameComplete;
    }

    /**
     * Setter for Game Completion
     * @param gameComplete Is game complete
     */
    public void setGameComplete(boolean gameComplete) {
        isGameComplete = gameComplete;
    }
}
