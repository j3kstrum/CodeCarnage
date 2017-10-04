package utilties.entities;

import utilties.models.Location;

/**
 * Player Entity
 */

public class Player extends Entity {

    private int id;
    private int health;
    private boolean is_dead;
    public static final int HEALTH_MAX = 100;

    /**
     *
     * @param id Player ID
     * @param location Location of where Player will be added
     */
    public Player(int id, Location location){
        this.id = id;
        this.health = HEALTH_MAX; // TODO: If needed, scale by some handicap factor.
        this.location = location;
        this.is_dead = false;
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
        // Set to health if in range 0 to HEALTH_MAX, otherwise set to bound.
        this.health = health < 0 ? 0 : health > HEALTH_MAX ? HEALTH_MAX : health;
        this.is_dead = this.health == 0;
    }

    /**
     * Determine if player is dead.
     * @return True if the player is dead. False otherwise.
     */
    public boolean isDead() {
        return this.is_dead;
    }

    /**
     * Inflict x damage to player's health.
     * @param x Amount of damage to do to player.
     */
    public void inflictDamage(int x) {
        // Set to maximum of zero or health difference.
        this.setHealth(this.getHealth() > x ? this.getHealth() - x : 0);
    }
}
