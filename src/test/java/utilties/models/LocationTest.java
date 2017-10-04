package utilties.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LocationTest {

    private Location _loc;

    @Before
    public void initializeObject() throws Exception {
        _loc = new Location(64, 31);
    }

    @Test
    public void initializeTest() throws Exception {
        _loc = null;
        _loc = new Location(10, 15);
    }

    @Test
    public void getX() throws Exception {
        assertTrue(_loc.getX() == 64);
    }

    @Test
    public void getY() throws Exception {
        assertTrue(_loc.getY() == 31);
    }

    private boolean setX(int to_value) throws Exception {
        _loc.setX(to_value);
        return _loc.getX() == to_value;
    }

    @Test
    public void setX1() throws Exception {
        assertTrue(setX(-738));
    }

    @Test
    public void setX2() throws Exception {
        assertTrue(setX(0));
    }

    @Test
    public void setX3() throws Exception {
        assertTrue(setX(646464646));
    }

    private boolean setY(int to_value) throws Exception {
        _loc.setY(to_value);
        return _loc.getY() == to_value;
    }

    @Test
    public void setY1() throws Exception {
        assertTrue(setY(-738));
    }

    @Test
    public void setY2() throws Exception {
        assertTrue(setY(0));
    }

    @Test
    public void setY3() throws Exception {
        assertTrue(setY(646464646));
    }

}