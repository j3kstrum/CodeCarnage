package main.java.utilties.models;

import main.java.utilties.entities.Entity;

/**
 * Model class for Tile.  Placed on Map a coordinate on a Map.
 */
public class Tile {

    private Location location;

    private Entity entity;

    /**
     * Creates Tile object with passed in location and entity on tile
     * @param location Location to place Tile
     * @param entity Entity on Tile
     */
    public Tile(Location location, Entity entity){
        this.location = location;
        this.entity = entity;
    }

    /**
     * Getter for location of Tile
     * @return Location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Getter for Entity
     * @return Entity
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Getter for Entity Type of Tile
     * @return EntityType
     */
    public Entity.EntityType getEntityType(){
        return entity.getEntityType();
    }
}
