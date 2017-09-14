package utilties.models.entities;

import utilties.models.Location;

public interface IEntity {

    enum EntityType {EMPTY, PLAYER}

    void setLocation(Location location);

    Location getLocation();

    EntityType getEntityType();

}
