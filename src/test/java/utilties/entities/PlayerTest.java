package utilties.entities;

import org.junit.Before;
import org.junit.Test;
import utilties.models.Game;

import java.awt.*;

import static org.junit.Assert.*;

public class PlayerTest {

    private Player p;

   @Before
    public void setup() throws Exception {
        p = new Player(5, new Point(2, 7));
    }

    @Test
    public void badValues() throws Exception {
        p = new Player(-275, new Point(-72, -17835907));
    }

    @Test
    public void getId() throws Exception {
        assertTrue(p.getId() == 5);
    }

    @Test
    public void getHealth() throws Exception {
        assertTrue(p.getHealth() == Game.HEALTH_MAX);
    }

    @Test
    public void setHealth() throws Exception {
        p.setHealth(64);
        assertTrue(p.getHealth() == 64);
    }

    @Test
    public void setLocation() throws Exception {
        p.setLocation(new Point(31, 0));
        assertTrue(p.getLocation().x == 31);
        assertTrue(p.getLocation().y == 0);
    }

    @Test
    public void getLocation() throws Exception {
        assertTrue(p.getLocation().x == 2);
        assertTrue(p.getLocation().y == 7);
    }

    @Test
    public void getEntityType() throws Exception {
        assertTrue(p.getEntityType() == Entity.EntityType.PLAYER);
    }

    private void injureNTimesByK(int n, int k) {
        for (int i = 0; i < n; i++) {
            p.setHealth(p.getHealth() - k);
        }
    }

    @Test
    public void goodInjuryTracking() throws Exception {
        int n = 12;
        int k = 3;
        injureNTimesByK(n, k);
        assertTrue(p.getHealth() == Game.HEALTH_MAX - (n * k));
    }


    @Test
    public void goodEntityTypeAfterInjury() throws Exception {
        int n = 5;
        int k = 2;
        injureNTimesByK(n, k);
        assertTrue(p.getEntityType() == Entity.EntityType.PLAYER);
    }

    @Test
    public void goodInjuryTrackingWithInterruption() throws Exception {
        int n = 5;
        int k = 3;
        injureNTimesByK(n, k);
        p.setHealth(p.getHealth() + 5);
        injureNTimesByK(n, k);
        assertTrue(p.getHealth() == Game.HEALTH_MAX - (n * k * 2) + 5);
    }

}