package utilties.entities;

import utilties.models.Location;

/**
 * Player Entity
 */

public class Player extends Entity {

    private int id;
    private int health;

    /**
     *
     * @param id Player ID
     * @param location Location of where Player will be added
     */
    public Player(int id, Location location){
        this.id = id;
        this.health = 100;
        this.location = location;
        entityType = EntityType.PLAYER;
    }

    /**
     * Get the ID of the player
     * @return ID of player
     */
    public int getId(){
        return this.id;
    }

    /**
     * Get health of Player
     * @return Health of Player
     */
    public int getHealth(){
        return this.health;
    }


    /**
     * Set health of player
     * @param health Health to change to
     */
    public void setHealth(int health){
        this.health = health;
    }
}
