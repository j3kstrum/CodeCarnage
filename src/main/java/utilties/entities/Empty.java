package utilties.entities;

import java.awt.*;

public class Empty extends Entity {


    /**
    Empty Entity.  Placed in a tile without any Objects
     */
    public Empty(Point location){
        this.location = location;
        this.entityType = EntityType.EMPTY;
    }

}
