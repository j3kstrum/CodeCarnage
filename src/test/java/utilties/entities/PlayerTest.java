package utilties.entities;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {

    private Player p;

//    @Before
//    public void setup() throws Exception {
//        p = new Player(5, new Location(2, 7));
//    }
//
//    @Test
//    public void badValues() throws Exception {
//        p = new Player(-275, new Location(-72, -17835907));
//    }
//
//    @Test
//    public void getId() throws Exception {
//        assertTrue(p.getId() == 5);
//    }
//
//    @Test
//    public void getHealth() throws Exception {
//        assertTrue(p.getHealth() == Player.HEALTH_MAX);
//    }
//
//    @Test
//    public void setHealth() throws Exception {
//        p.setHealth(64);
//        assertTrue(p.getHealth() == 64);
//    }
//
//    @Test
//    public void setLocation() throws Exception {
//        p.setLocation(new Location(31, 0));
//        assertTrue(p.getLocation().getX() == 31);
//        assertTrue(p.getLocation().getY() == 0);
//    }
//
//    @Test
//    public void getLocation() throws Exception {
//        assertTrue(p.getLocation().getX() == 2);
//        assertTrue(p.getLocation().getY() == 7);
//    }
//
//    @Test
//    public void getEntityType() throws Exception {
//        assertTrue(p.getEntityType() == Entity.EntityType.PLAYER);
//    }
//
//    private void injureNTimesByK(int n, int k) {
//        for (int i = 0; i < n; i++) {
//            p.inflictDamage(k);
//        }
//    }
//
//    @Test
//    public void goodInjuryTracking() throws Exception {
//        int n = 12;
//        int k = 3;
//        injureNTimesByK(n, k);
//        assertTrue(p.getHealth() == Player.HEALTH_MAX - (n * k));
//    }
//
//    @Test
//    public void injuredAfterDeath() throws Exception {
//        int n = 50;
//        int k = Player.HEALTH_MAX;
//        injureNTimesByK(n, k);
//        assertTrue(p.getHealth() == 0);
//    }
//
//    @Test
//    public void testIsDead() throws Exception {
//        int n = 2;
//        int k = Player.HEALTH_MAX;
//        injureNTimesByK(n, k);
//        assertTrue(p.isDead());
//    }
//
//    @Test
//    public void testIsNotDead() throws Exception {
//        int n = 2;
//        int k = Player.HEALTH_MAX / 4;
//        injureNTimesByK(n, k);
//        assertFalse(p.isDead());
//    }
//
//    @Test
//    public void goodEntityTypeAfterInjury() throws Exception {
//        int n = 5;
//        int k = 2;
//        injureNTimesByK(n, k);
//        assertTrue(p.getEntityType() == Entity.EntityType.PLAYER);
//    }
//
//    @Test
//    public void goodInjuryTrackingWithInterruption() throws Exception {
//        int n = 5;
//        int k = 3;
//        injureNTimesByK(n, k);
//        p.setHealth(p.getHealth() + 5);
//        injureNTimesByK(n, k);
//        assertTrue(p.getHealth() == Player.HEALTH_MAX - (n * k * 2) + 5);
//    }
//
//    @Test
//    public void healedAboveFull() throws Exception {
//        int n = 5;
//        int k = 1;
//        injureNTimesByK(n, k);
//        p.setHealth(p.getHealth() + 38);
//        assertTrue(p.getHealth() == Player.HEALTH_MAX);
//    }
//
//    @Test
//    public void healedWhileFull() throws Exception {
//        p.setHealth(p.getHealth() + 1);
//        assertTrue(p.getHealth() == Player.HEALTH_MAX);
//    }

}