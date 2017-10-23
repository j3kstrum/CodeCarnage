package utilties.models;

import common.exceptions.LoadMapFailedException;
import org.junit.Before;
import org.junit.Test;
import org.mapeditor.io.MapReader;
import utilties.entities.Entity;
import utilties.entities.Player;

import java.awt.*;
import java.net.URL;

import static org.junit.Assert.assertTrue;

public class EntityMapTest{

    private EntityMap entityMap;

    @Before
    public void setup() throws Exception {
        try {
            URL mapPath = new URL("https://www.cse.buffalo.edu/~jacobeks/codecarnage/me/r/game-map.tmx");
            MapReader mr = new MapReader();
            entityMap = new EntityMap(mr.readMap(mapPath), 25, 15);
        } catch (Exception ex) {
            throw new LoadMapFailedException(ex.getMessage());
        }
    }
    @Test
    public void cannotMoveToLocation() throws Exception {
        assertTrue(entityMap.canMoveToLocation(new Point(-5, 5)) == false);
    }
    @Test
    public void canMoveToLocation() throws Exception {
        assertTrue(entityMap.canMoveToLocation(entityMap.getPlayers().get(Game.OPPONENT_ID).getLocation()) == false);
    }
    @Test
    public void foundPlayerAndOpponent() throws Exception {
        assertTrue(entityMap.getPlayers().size() == 2);
    }

    @Test
    public void isNotInsideMap() throws Exception {
        assertTrue(entityMap.isInsideMap(new Point(-5, 5)) == false);
    }

    @Test
    public void isInsideMap() throws Exception {
        assertTrue(entityMap.isInsideMap(new Point(5,5)));
    }

    @Test
    public void setLocationPlayerData() throws Exception {
        EntityTile player = entityMap.getPlayers().get(Game.PLAYER_ID);
        int oldX = player.getLocation().x;
        int oldY = player.getLocation().y;

        entityMap.setLocation(player, new Point(player.getLocation().x + 1, player.getLocation().y));
        assertTrue(player.getLocation().x == oldX + 1 && player.getLocation().y == oldY);
    }

    @Test
    public void setLocationTileData() throws Exception {
        EntityTile player = entityMap.getPlayers().get(Game.PLAYER_ID);
        int oldX = player.getLocation().x;
        int oldY = player.getLocation().y;

        entityMap.setLocation(player, new Point(player.getLocation().x + 1, player.getLocation().y));
        assertTrue(entityMap.getEntityTiles()[oldX][oldY] != entityMap.getEntityTiles()[player.getLocation().x][player.getLocation().y]);
    }

    @Test
    public void isPlayerStoredOnMap() throws Exception {
        EntityTile player = entityMap.getPlayers().get(Game.PLAYER_ID);
        entityMap.setLocation(player, new Point(player.getLocation().x + 1, player.getLocation().y));
        assertTrue(entityMap.getEntityTiles()[player.getLocation().x][player.getLocation().y] == player);
    }

}
