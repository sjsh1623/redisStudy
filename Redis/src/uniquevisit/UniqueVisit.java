package uniquevisit;

import helper.JedisHelper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UniqueVisit {
    private Jedis jedis;
    private static final String KEY_PAGE_VIEW = "page:view:"; // --- 1
    private static final String KEY_UNIQUE_VISITOR = "unique:visitor:"; // --- 2

    public UniqueVisit(JedisHelper jedisHelper) {
        this.jedis = jedisHelper.getConnection();
    }

    /**
     * 특정 사용자의 순 방문 횟수와 누적 방문 횟수를 증가시킨다.
     * @param userNo 사용자 번호
     */
    public void visit(int userNo) {
        Pipeline pipeline = this.jedis.pipelined(); // --- 3
        pipeline.incr(KEY_PAGE_VIEW + getToday());
        pipeline.setbit(KEY_UNIQUE_VISITOR + getToday(), userNo, true); // --- 4
        pipeline.sync();
    }

    private String getToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date());
    }

    /**
     * 요청된 날짜의 누적 방문자 수를 조회
     * @param date 조회 대상 날짜
     * @return 누적 방문자 수
     */
    public int getPVCount(String date) {
        int result = 0;
        try {
            result = Integer.parseInt(jedis.get(KEY_PAGE_VIEW + date)); // --- 5
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 요청된 날짜의 순 방문자 수를 조회
     * @param date
     * @return
     */
    public Long getUVCount(String date) {
        return jedis.bitcount(KEY_UNIQUE_VISITOR + date); // --- 6
    }
}
