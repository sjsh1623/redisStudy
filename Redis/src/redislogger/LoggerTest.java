package redislogger;

import helper.JedisHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertTrue;

public class LoggerTest {
    static JedisHelper helper;
    private static final int WAITING_TERM = 5000;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        helper = JedisHelper.getInstance();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        helper.destroyPool();
    }

    @Test
    public void testWrite() {
        Random random = new Random(System.currentTimeMillis());
        LogWriterV2 logWriter = new LogWriterV2(helper);

        for (int i = 0; i < 100; i++) {
            assertTrue(logWriter.log(i + ", This is new test log message") > 0);

            try {
                Thread.sleep(random.nextInt(50));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testReceiver() {
        LogReceiverV2 logReceiver = new LogReceiverV2();

        for (int i = 0; i < 5; i++) {
            logReceiver.start();
            try {
                Thread.sleep(WAITING_TERM);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
