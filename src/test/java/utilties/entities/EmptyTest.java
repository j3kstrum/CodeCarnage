package utilties.entities;

import org.junit.Test;

import static org.junit.Assert.*;
import java.awt.Point;

public class EmptyTest {
    @Test
    public void setLocation() throws Exception {
        Empty e = new Empty(new Point(0, 0));
        Point loc = new Point(16, 31);
        e.setLocation(loc);
        assertTrue(e.getLocation().equals(loc));
    }

    @Test
    public void getLocation() throws Exception {
        Point loc = new Point(71, -17);
        Empty e = new Empty(loc);
        assertTrue(e.getLocation().equals(loc));
    }

    @Test
    public void getEntityType() throws Exception {
        Empty e = new Empty(new Point(0, 0));
        assertTrue(e.getEntityType() == Entity.EntityType.EMPTY);
    }

    @Test
    public void locationRaw() throws Exception {
        Point loc = new Point(1723, 782);
        Empty e = new Empty(loc);
        assertTrue(e.location.equals(loc));
    }

    @Test
    public void entityTypeRaw() throws Exception {
        Empty e = new Empty(new Point(71, 3));
        assertTrue(e.entityType == Entity.EntityType.EMPTY);
    }

}