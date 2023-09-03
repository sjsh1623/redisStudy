package redislogger;


import helper.JedisHelper;
import redis.clients.jedis.Jedis;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class LogReceiver {
    private static final JedisHelper helper = JedisHelper.getInstance();
    private static final String KEY_WAS_LOG = "was:log";
    private static final String LOG_FILE_NAME_PREFIX = "./waslog";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH'.log'");
    private static final int WAITING_TERM = 5000;

    /**
     * 레디스 서버에서 로그를 읽어서 파일로 저장한다.
     * 프로그램이 종료되기 전까지 무한히 실행한다.
     */
    public void start() {
        Random random = new Random();
        Jedis jedis = helper.getConnection();

        // 로그는 항상 발생하기 때문에 프로그램이 종료될 때 까지 항상 실행되어야 하므로 무한루프를 사용했다
        while (true) {
            writeFile(jedis.getSet(KEY_WAS_LOG, ""));

            try {
                // 로그 파일을 기록하고 나서 지정된 시간 동안 대기한다 예제에서는 5,000밀리초를 지정했다.
                // 즉, 로그를 파일로 저장하는 주기가 5초다.
                Thread.sleep(random.nextInt(WAITING_TERM));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 메서드가 호출된 시간에 해당하는 로그 파일명을 생성한다.
     * @return 현재 시간에 해당하는 로그 파일명
     */
    public String getCurrentFileName() {
        return LOG_FILE_NAME_PREFIX + sdf.format(new Date());
    }

    private void writeFile(String log) {
        try {
            FileWriter writer = new FileWriter(getCurrentFileName(), true);

            writer.write(log);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
