/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package utilties.entities;

import java.awt.*;

/* Entity Abstract Class
 *
 * Entities have a location and a type.  More functionality to be added later once game is fleshed out
 */
public abstract class Entity {

    protected EntityType entityType = null;

    protected Point location;

    /*
    Entity Type enum
     */
    public enum EntityType {
        EMPTY, PLAYER
    }


    public void setLocation(Point location) {
        this.location = location;
    }

    public Point getLocation() {
        return this.location;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }


}
