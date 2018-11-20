package cn.liuht.seckill.common.lock;

/**
 * 分布式锁顶级接口
 *
 * @author liuht
 * @date 2018/5/29 14:12
 */
public interface DistributedLock {

    /**
     * 默认超时时间
     */
    public static final long TIMEOUT_MILLIS = 3000;

    public static final int RETRY_TIMES = 2;

    public static final long SLEEP_MILLIS = 100;

    public boolean lock(String key);

    public boolean lock(String key, int retryTimes);

    public boolean lock(String key, int retryTimes, long sleepMillis);

    public boolean lock(String key, long expire);

    public boolean lock(String key, long expire, int retryTimes);

    public boolean lock(String key, long expire, int retryTimes, long sleepMillis);

    /**
     * 释放锁
     *
     * @param key
     * @return
     */
    public boolean releaseLock(String key);
}
