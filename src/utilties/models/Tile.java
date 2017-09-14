package utilties.models;

import utilties.models.entities.IEntity;

public class Tile {

    private Location location;

    private IEntity entity;

    public Tile(Location location, IEntity entity){
        this.location = location;
        this.entity = entity;
    }

    public Location getLocation() {
        return location;
    }

    public IEntity getEntity() {
        return entity;
    }

    public IEntity.EntityType getEntityType(){
        return this.entity.getEntityType();
    }
}
