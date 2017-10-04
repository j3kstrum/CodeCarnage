/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package utilties.entities;

import java.awt.*;

public class Empty extends Entity {


    /**
     * Empty Entity.  Placed in a tile without any Objects
     */
    public Empty(Point location) {
        this.location = location;
        this.entityType = EntityType.EMPTY;
    }

}
