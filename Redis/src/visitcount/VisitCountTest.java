package visitcount;

import helper.JedisHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class VisitCountTest {
    static JedisHelper jedisHelper;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        jedisHelper = JedisHelper.getInstance();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        jedisHelper.destroyPool();
    }

    @Test
    public void testAddVisit() {
        VisitCount visitCount = new VisitCount(jedisHelper);
        assertNotNull(visitCount);

        assertTrue(visitCount.addVisit("52")>0);
        assertTrue(visitCount.addVisit("180")>0);
        assertTrue(visitCount.addVisit("554")>0);
    }

    @Test
    public void testGetVisitCount() {
        VisitCount visitCount = new VisitCount(jedisHelper);
        assertNotNull(visitCount);

        List<String> result = visitCount.getVisitCount("52", "180", "554");
        assertNotNull(result);
        assertTrue(result.size() == 3);

        long sum = 0;
        for (String count : result) {
            sum = sum + Long.parseLong(count);
        }

        String totalCount = visitCount.getVisitTotalCount();
        assertEquals(String.valueOf(sum), totalCount);
    }
}
