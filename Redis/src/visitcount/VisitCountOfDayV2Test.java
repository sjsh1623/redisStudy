package visitcount;

import helper.JedisHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedMap;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class VisitCountOfDayV2Test {
    static JedisHelper helper;

    @BeforeClass
    public static void setUp() throws Exception {
        helper = JedisHelper.getInstance();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        helper.destroyPool();
    }

    @Test
    public void testAddVisit() {
        VisitCount visitCount = new VisitCount(helper);
        assertTrue(visitCount.addVisit("52") > 0);
        assertTrue(visitCount.addVisit("180") > 0);
        assertTrue(visitCount.addVisit("554") > 0);

        VisitCountOfDayV2 visitCountOfDayV2 = new VisitCountOfDayV2(helper);
        assertTrue(visitCountOfDayV2.addVisit("52") > 0);
        assertTrue(visitCountOfDayV2.addVisit("180") > 0);
        assertTrue(visitCountOfDayV2.addVisit("554") > 0);
    }

    @Test
    public void testGetVisitCountByDate() {
        String toDay = new SimpleDateFormat("yyyyMMdd").format(new Date());
        VisitCountOfDayV2 visitCountOfDayV2 = new VisitCountOfDayV2(helper);

        SortedMap<String, String> visitCount = visitCountOfDayV2.getVisitCountByDaily("554");

        assertTrue(visitCount.size() > 0);
        assertNotNull(visitCount);
        assertNotNull(visitCount.firstKey());
        assertNotNull(visitCount.lastKey());

        System.out.println(visitCount);

        SortedMap<String, String> totalVisit = visitCountOfDayV2.getVisitCountByDailyTotal();
        assertTrue(totalVisit.size() > 0);
        assertNotNull(totalVisit);
        assertNotNull(totalVisit.firstKey());
        assertNotNull(totalVisit.lastKey());

        System.out.println(totalVisit);
    }
}
