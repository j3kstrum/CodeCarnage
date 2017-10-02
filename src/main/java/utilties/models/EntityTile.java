package utilties.models;

import org.mapeditor.core.Tile;
import utilties.entities.Entity;

import java.awt.*;

/**
 * Model class for EntityTile.  Holds location of tile and corresponding tile object
 */
public class EntityTile {

    private Point _location;

    private Entity _entity;

    private Tile _tile;

    /**
     * Creates EntityTile object with passed in _location, _entity and corresponding _tile
     * Currently only used to store _location of the _entity
     * @param location Location to place EntityTile
     * @param entity Entity on EntityTile
     */
    public EntityTile(Point location, Entity entity, Tile tile){
        this._location = location;
        this._entity = entity;
        this._tile = tile;
    }

    /**
     * Getter for _location of EntityTile
     * @return Location
     */
    public Point getLocation() {
        return this._location;
    }

    /**
     * Getter for Entity
     * @return Entity
     */
    public Entity getEntity() {
        return this._entity;
    }

    /**
     * Getter for Entity Type of EntityTile
     * @return EntityType
     */
    public Entity.EntityType getEntityType(){
        return this._entity.getEntityType();
    }

    /**
     * Gets Tile for Entity
     * @return _tile
     */
    public Tile getTile() {
        return _tile;
    }

    /**
     * Sets Tile for Entity
     * @param tile Tile to set
     */
    public void setTile(Tile tile){
        this._tile = tile;
    }

}
