package uniquevisit;

import helper.JedisHelper;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UniqueVisitTest {
    static JedisHelper jedisHelper;
    private UniqueVisit uniqueVisit;
    private static final int VISIT_COUNT = 1000; // --- 1
    private static final int TOTAL_USER = 10000000; // --- 2
    private static final String TEST_DATE = "19500101"; // --- 3
    static Random random = new Random();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        jedisHelper = JedisHelper.getInstance();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        jedisHelper.destroyPool();
    }

    @Before
    public void setUp() throws Exception {
        uniqueVisit = new UniqueVisit(jedisHelper);
        assertNotNull(uniqueVisit);
    }

    @Test
    public void testRandomPV() {
        String today = getToday();
        int pv = uniqueVisit.getPVCount(today);
        for (int i = 0; i < VISIT_COUNT; i++) { // --- 4
            uniqueVisit.visit(random.nextInt(TOTAL_USER));
        }

        assertEquals(pv + VISIT_COUNT, uniqueVisit.getPVCount(today)); // --- 5
    }

    @Test
    public void testInvalidPV() {
        assertEquals(0, uniqueVisit.getPVCount(TEST_DATE)); // --- 6
        assertEquals(new Long(0), uniqueVisit.getUVCount(TEST_DATE));
    }

    @Test
    public void testPV() {
        String today = getToday();
        int result = uniqueVisit.getPVCount(today);
        uniqueVisit.visit(65487);

        assertEquals(result + 1, uniqueVisit.getPVCount(today));
    }

    @Test
    public void testUV() {
        String today = getToday();
        uniqueVisit.visit(65487);
        Long result = uniqueVisit.getUVCount(today);
        uniqueVisit.visit(65487); // --- 8

        assertEquals(result, uniqueVisit.getUVCount(today));
    }

    private String getToday() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }
}
