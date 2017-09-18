package utilties.entities;

import utilties.models.Location;

public class Empty extends Entity {


    /**
    Empty Entity.  Placed in a tile without any Objects
     */
    public Empty(Location location){
        this.location = location;
        this.entityType = EntityType.EMPTY;
    }

}
