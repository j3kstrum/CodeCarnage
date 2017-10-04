package common;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the Main class and the main method.
 *
 * NOTE - We cannot spawn multiple JavaFX launch threads.
 * Therefore, we call this test once to ensure that calling main doesn't break everything.
 * To test all possible string argument lists, we'll have to do that manually
 * (or, adopt a different testing framework).
 *
 * @author jacob.ekstrum
 */
public class MainTest {

    /**
     * Test main with some default arguments that should definitely not break anything (but may or may not be valid).
     */
    @Test
    public void mainTestSomeArgs() throws Exception {
        common.Main.main(new String[] {"These", "Should", "Be", "Valid", "Args!"});
    }

}