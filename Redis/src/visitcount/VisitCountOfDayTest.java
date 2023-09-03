package visitcount;

import helper.JedisHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class VisitCountOfDayTest {
    static JedisHelper jedisHelper;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        jedisHelper = JedisHelper.getInstance();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        jedisHelper.destroyPool();
    }

    @Test
    public void testAddVisit() {
        VisitCount visitCount = new VisitCount(jedisHelper);
        assertTrue(visitCount.addVisit("52") > 0);
        assertTrue(visitCount.addVisit("180") > 0);
        assertTrue(visitCount.addVisit("554") > 0);

        VisitCountOfDay visitCountOfDay = new VisitCountOfDay(jedisHelper);
        assertTrue(visitCountOfDay.addVisit("52") > 0);
        assertTrue(visitCountOfDay.addVisit("180") > 0);
        assertTrue(visitCountOfDay.addVisit("554") > 0);
    }

    @Test
    public void testGetVisitCountDate() {
        String[] dateList = {"20130512", "20130513", "20130514", "20130515"};
        VisitCountOfDay visitCountOfDay = new VisitCountOfDay(jedisHelper);
        List<String> result = visitCountOfDay.getVisitCountByDate("52", dateList);
        assertNotNull(result);
        assertTrue(result.size() == 4);
    }
}
