package like;

import helper.JedisHelper;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LikePostingTest {
    static JedisHelper jedisHelper;
    private LikePosting likePosting;
    private static Random rand = new Random();
    private static final int POSTING_COUNT = 20;
    private static final int TESTUSER = rand.nextInt(1000000); // --- 1
    private static String[] POSTLIST = new String[POSTING_COUNT];

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        jedisHelper = JedisHelper.getInstance();

        // --- 3
        for (int i = 0; i < POSTING_COUNT; i++) {
            POSTLIST[i] = String.valueOf(i + 1);
        }
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        jedisHelper.destroyPool();
    }

    @Before
    public void setUp() throws Exception {
        this.likePosting = new LikePosting(jedisHelper);
        assertNotNull(this.likePosting);
    }

    @Test
    public void testLike() {
        String postingNo = String.valueOf(this.rand.nextInt(POSTING_COUNT)); // --- 4

        // --- 5
        String userNo = String.valueOf(TESTUSER);
        if (this.likePosting.isLiked(postingNo, userNo)) {
            this.likePosting.unLike(postingNo, userNo);
        }
        assertTrue(this.likePosting.like(postingNo, userNo)); // --- 6
    }

    @Test
    public void testUnLike() {
        String postingNo = String.valueOf(this.rand.nextInt(POSTING_COUNT));
        String userNo = String.valueOf(TESTUSER);

        // --- 7
        if (this.likePosting.isLiked(postingNo, userNo)) {
            assertTrue(this.likePosting.unLike(postingNo, userNo));
        } else {
            assertTrue(this.likePosting.like(postingNo, userNo));
            assertTrue(this.likePosting.unLike(postingNo, userNo));
        }
    }

    @Test
    public void testGetLikeCount() {
        String postingNo = String.valueOf(this.rand.nextInt(POSTING_COUNT));
        String userNo = String.valueOf(TESTUSER);

        // --- 8
        if (this.likePosting.isLiked(postingNo, userNo)) {
            assertTrue(this.likePosting.unLike(postingNo, userNo));
        }

        Long prevCount = this.likePosting.getLikeCount(postingNo); // --- 9
        this.likePosting.like(postingNo, userNo);
        assertEquals(this.likePosting.getLikeCount(postingNo), new Long(prevCount + 1)); // --- 10
    }

    @Test
    public void testGetLikeCountList() {
        List<Long> countList = this.likePosting.getLikeCountList(POSTLIST);
        assertEquals(countList.size(), POSTING_COUNT); // --- 11
    }

    @Test
    public void testIsLiked() {
        String postingNo = String.valueOf(this.rand.nextInt(POSTING_COUNT));
        String userNo = String.valueOf(TESTUSER);

        this.likePosting.like(postingNo, userNo);
        assertTrue(this.likePosting.isLiked(postingNo, userNo)); // --- 12
    }

    @Test
    public void testDeleteLikeInfo() {
        String postingNo = "A1234567890"; // --- 13
        String userNo = String.valueOf(TESTUSER);
        this.likePosting.like(postingNo, userNo);
        assertTrue(this.likePosting.deleteLikeInfo(postingNo)); // --- 14
    }

    @Test
    public void testRandomLike() {
        for (int i = 0; i < POSTING_COUNT; i++) {
            String sudoRandomUser = String.valueOf(rand.nextInt(10000000)); // --- 15
            this.likePosting.like(String.valueOf(i), sudoRandomUser);
        }
    }
}
