package main.java.utilties.entities;

import main.java.utilties.models.Location;

/* Entity Abstract Class
 *
 * Entities have a location and a type.  More functionality to be added later once game is fleshed out
 */
public abstract class Entity {

    protected EntityType entityType = null;

    protected Location location;

    /*
    Entity Type enum
     */
    public enum EntityType {EMPTY, PLAYER}


    public void setLocation(Location location){
        this.location = location;
    }

    public Location getLocation(){
        return this.location;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

}
