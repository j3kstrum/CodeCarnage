package common;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * When testing the logger, we really don't care about testing for time functionality.
 * If you're using the logger, you can see time functionality, and if you can't, something
 * is broken or you've formatted improperly.
 *
 * However, that being said, these tests will account for ensuring the proper name
 * and proper stream are receiving output to prevent other confusions in logging.
 *
 * @author jacob.ekstrum
 */
public class BaseLoggerTest {

    // Will be used to capture system out and system error messages.
    // *** SEE https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println
    private ByteArrayOutputStream sysOutWrapper;
    private ByteArrayOutputStream sysErrWrapper;
    private BaseLogger LOGGER;
    private PrintStream sysout;
    private PrintStream syserr;

    /**
     * SEE https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println
     */
    @Before
    public void setUpStreams() {
        sysout = System.out;
        syserr = System.err;
        sysOutWrapper = new ByteArrayOutputStream();
        sysErrWrapper = new ByteArrayOutputStream();
        System.setOut(new PrintStream(sysOutWrapper));
        System.setErr(new PrintStream(sysErrWrapper));
        /*
         * Initialize test logger to default
         */
        LOGGER = new BaseLogger("BaseLoggerTest");
    }

    /**
     * SEE https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println
     */
    @After
    public void cleanUpStreams() {
        System.setOut(sysout);
        System.setErr(syserr);
    }

    /**
     * Test that the logger can send output to standard output.
     */
    @Test
    public void testLogSysOut() throws Exception {
        LOGGER = new BaseLogger("BaseLoggerTest", System.out);
        String message = "This message should have been received by system out.";
        LOGGER.info(message);
        assertEquals("", sysErrWrapper.toString());
        assertTrue(sysOutWrapper.toString().contains(message));
    }

    /**
     * Test that the logger can send output to standard error.
     */
    @Test
    public void testLogSysErr() throws Exception {
        LOGGER = new BaseLogger("BaseLoggerTest", System.err);
        String message = "This message should have been received by system error.";
        LOGGER.info(message);
        assertEquals("", sysOutWrapper.toString());
        assertTrue(sysErrWrapper.toString().contains(message));
    }

    /**
     * Test that the user can specify a custom logging format.
     */
//    @Test
//    public void testCustomFormat() throws Exception {
//        String name = "BaseLoggerTest";
//        LOGGER = new BaseLogger(name, System.out, "[%(name)]: %(message)");
//        String message = "hi";
//        LOGGER.fatal(message);
//        assertEquals("[" + name + "]: " + message + "\n", sysOutWrapper.toString());
//    }

    /**
     * Test that the user can use the debug level.
     */
    @Test
    public void debug() throws Exception {
        LOGGER.debug("msg");
        assertTrue(sysErrWrapper.toString().contains("DEBUG"));
    }

    /**
     * Test that the user can use the debug level.
     */
    @Test
    public void info() throws Exception {
        LOGGER.info("msg");
        assertTrue(sysErrWrapper.toString(), sysErrWrapper.toString().contains("INFO"));
    }

    /**
     * Test that the user can use the debug level.
     */
    @Test
    public void warning() throws Exception {
        LOGGER.warning("msg");
        assertTrue(sysErrWrapper.toString().contains("WARNING"));
    }

    /**
     * Test that the user can use the debug level.
     */
    @Test
    public void critical() throws Exception {
        LOGGER.critical("msg");
        assertTrue(sysErrWrapper.toString().contains("CRITICAL"));
    }

    /**
     * Test that the user can use the debug level.
     */
    @Test
    public void fatal() throws Exception {
        LOGGER.fatal("msg");
        assertTrue(sysErrWrapper.toString().contains("FATAL"));
    }

}