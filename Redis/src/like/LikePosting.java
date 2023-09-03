package like;

import helper.JedisHelper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.ArrayList;
import java.util.List;

public class LikePosting {
    private Jedis jedis;
    private static final String KEY_LIST_SET = "posting:like:";

    public LikePosting(JedisHelper jedisHelper) {
        this.jedis = jedisHelper.getConnection();
    }

    /**
     * 지정된 사용자가 게시물에 '좋아요'를 표시한다.
     * @param postingNo 게시물 번호
     * @param userNo 사용자 번호
     * @return 정상처리되었으면 true
     */
    public boolean like(String postingNo, String userNo) {
        return this.jedis.sadd(KEY_LIST_SET + postingNo, userNo) > 0;
    }

    /**
     * 지정된 사용자가 게시물에 '좋아요'를 표시한다.
     * @param postingNo 게시물 번호
     * @param userNo 사용자 번호
     * @return 정상처리되었으면 true
     */
    public boolean unLike(String postingNo, String userNo) {
        return this.jedis.srem(KEY_LIST_SET + postingNo, userNo) > 0;
    }

    /**
     * 지정된 사용자의 '좋아요' 표시 여부를 확인한다
     * @param postingNo 게시물 번호
     * @param userNo 사용자 번호
     * @return 정상처리되었으면 true
     */
    public boolean isLiked(String postingNo, String userNo) {
        return jedis.sismember(KEY_LIST_SET + postingNo, userNo);
    }

    /**
     * 게시물에 대한 '좋아요' 정보를 삭제한다
     * @param postingNo 게시물 번호
     * @return 삭제되었으면 true
     */
    public boolean deleteLikeInfo(String postingNo) {
        return this.jedis.del(KEY_LIST_SET + postingNo) > 0;
    }

    /**
     * 게시물의 '좋아요' 횟수를 조회한다
     * @param postingNo 게시물 번호
     * @return 좋아요 개수
     */
    public Long getLikeCount(String postingNo) {
        return this.jedis.scard(KEY_LIST_SET + postingNo);
    }

    /**
     * 주어진 게시물 목록의 '좋아요' 횟수를 조회한다.
     * @param postingList 조회 대상 포스팅 목록
     * @return '좋아요' 횟수 목록
     */
    public List<Long> getLikeCountList(String[] postingList) {
        List<Long> result = new ArrayList<>();

        Pipeline pipeline = jedis.pipelined();
        for (String postingNo : postingList) {
            pipeline.scard(postingNo);
        }

        List<Object> pipelineResult = pipeline.syncAndReturnAll();

        for (Object item : pipelineResult) {
            result.add((Long) item);
        }

        return result;
    }
}
