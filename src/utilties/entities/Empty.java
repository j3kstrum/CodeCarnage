package utilties.entities;

import utilties.models.Location;

public class Empty implements IEntity {

    private EntityType entityType = EntityType.EMPTY;

    private Location location;

    public Empty(Location location){
        this.location = location;
    }

    @Override
    public void setLocation(Location location) {

    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public EntityType getEntityType() {
        return entityType;
    }
}
