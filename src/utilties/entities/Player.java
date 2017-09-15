package utilties.entities;

import utilties.models.Location;

public class Player implements IEntity{

    private int id;
    private int health;
    private Location location;
    private EntityType entityType = EntityType.PLAYER;

    public Player(int id, Location location){
        this.id = id;
        this.health = 100;
        this.location = location;
    }

    public int getId(){
        return this.id;
    }

    public int getHealth(){
        return this.health;
    }

    public void setHealth(int health){
        this.health = health;
    }

    @Override
    public void setLocation(Location location){
        this.location = location;
    }

    @Override
    public Location getLocation(){
        return this.location;
    }

    @Override
    public EntityType getEntityType() {
        return this.entityType;
    }
}
