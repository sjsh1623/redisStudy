package helper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class JedisHelper {
    protected static final String REDIS_HOST = "127.0.0.1";
    protected static final int REDIS_PORT = 6379;
    private final Set<Jedis> connectionList = new HashSet<Jedis>();
    private JedisPool pool;

    /**
     * 제디스 연결풀 생성을 위한 헬퍼 클래스 내부 생성자
     */
    private JedisHelper() {
        this.pool = new JedisPool(REDIS_HOST, REDIS_PORT);
    }

    /**
     * 싱글톤 처리를 위한 홀더 클래스
     * 제디스 연결풀이 포함된 헬퍼 객체를 반환한다.
     */
    private static class LazyHolder {
        private static final JedisHelper INSTANCE = new JedisHelper();
    }

    /**
     * 싱글톤 객체를 가져온다
     * @return 제디스 헬퍼 객체
     */
    public static JedisHelper getInstance() {
        return LazyHolder.INSTANCE;
    }

    /**
     * 제디스 클라이언트 연결을 가져온다
     * @return 제디스 객체
     */
    final public Jedis getConnection() {
        Jedis jedis = this.pool.getResource();
        this.connectionList.add(jedis);
        return jedis;
    }

    /**
     * 사용이 완료된 제디스 객체를 회수한다
     * @param jedis 사용 완료된 제디스 객체
     */
    final public void returnResource(Jedis jedis) {
        this.pool.returnResource(jedis);
    }

    /**
     * 제디스 연결풀을 제거한다
     */
    final public void destroyPool() {
        for (Jedis jedis : this.connectionList) {
            this.pool.returnResource(jedis);
        }

        this.pool.destroy();
    }
}
