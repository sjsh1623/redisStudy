package redislogger;

import helper.JedisHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertTrue;

public class LogWriterTest {
    static JedisHelper helper;
    static LogWriter logger;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        helper = JedisHelper.getInstance();
        logger = new LogWriter(helper);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
//        helper.destroyPool();
    }

    @Test
    public void testLogger() {
        Random random = new Random(System.currentTimeMillis());

        for (int i = 0; i < 100; i++) {
            assertTrue(logger.log("This is test log message " + i) > 0);

            try {
                Thread.sleep(random.nextInt(50));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
