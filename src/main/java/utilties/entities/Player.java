package utilties.entities;

import java.awt.*;

/**
 * Player Entity
 */

public class Player extends Entity {

    private int id;
    private int health;
    private int damage;
    private boolean isShielding;
    private int shieldStrength;

    /**
     *
     * @param id Player ID
     * @param location Location of where Player will be added
     */
    public Player(int id, Point location){
        this.id = id;
        this.health = 100;
        this.damage = 10;
        this.location = location;
        entityType = EntityType.PLAYER;
        this.isShielding = false;
        this.shieldStrength = 5;
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


    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }


    public boolean isShielding() {
        return isShielding;
    }

    public void setShielding(boolean shielding) {
        isShielding = shielding;
    }

    public int getShieldStrength() {
        return shieldStrength;
    }

    public void setShieldStrength(int shieldStrength) {
        this.shieldStrength = shieldStrength;
    }

}
