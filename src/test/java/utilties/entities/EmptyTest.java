package utilties.entities;

import org.junit.Test;
import utilties.models.Location;

import static org.junit.Assert.*;

public class EmptyTest {
    @Test
    public void setLocation() throws Exception {
        Empty e = new Empty(new Location(0, 0));
        Location loc = new Location(16, 31);
        e.setLocation(loc);
        assertTrue(e.getLocation().equals(loc));
    }

    @Test
    public void getLocation() throws Exception {
        Location loc = new Location(71, -17);
        Empty e = new Empty(loc);
        assertTrue(e.getLocation().equals(loc));
    }

    @Test
    public void getEntityType() throws Exception {
        Empty e = new Empty(new Location(0, 0));
        assertTrue(e.getEntityType() == Entity.EntityType.EMPTY);
    }

    @Test
    public void locationRaw() throws Exception {
        Location loc = new Location(1723, 782);
        Empty e = new Empty(loc);
        assertTrue(e.location.equals(loc));
    }

    @Test
    public void EntityTypeRaw() throws Exception {
        Empty e = new Empty(new Location(71, 3));
        assertTrue(e.entityType == Entity.EntityType.EMPTY);
    }

}