package staticPart;


import log.LoggerFactory;
import org.apache.http.util.TextUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import utils.ThreadManager;


import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by ldy on 2017/6/26.
 */
public enum RedisCache {
    INSTANCE;

    private JedisPool jedisPool;

    private String redisHost = "172.17.0.1";
    private int redisPort = 6379;
    private int redisConnectionTimeout = 20;

    private static final String LOCK_SUCCESS = "OK";
    private static final String RELEASE_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    public void init(String host, int port, int connectionTimeout, String password) {
        if (!TextUtils.isEmpty(host)) {
            redisHost = host;
        }

        LoggerFactory.getLogger().info(String.format("Redis host is %s", redisHost));

        if (port >= 0) {
            redisPort = port;
        }

        LoggerFactory.getLogger().info(String.format("Redis port is %s", redisPort));

        if (connectionTimeout >= 0) {
            redisConnectionTimeout = connectionTimeout;
        }

        LoggerFactory.getLogger().info(String.format("Redis connection timeout is %s", redisConnectionTimeout));

        if (!TextUtils.isEmpty(password)) {
            LoggerFactory.getLogger().info(String.format("Redis has password"));
        }

        initJedisPool(password);
    }

    private void initJedisPool(String passowrd) {
        JedisPoolConfig jpc = new JedisPoolConfig();
        jpc.setMaxTotal(150);
        jpc.setMaxIdle(10);
        jpc.setMaxWaitMillis(redisConnectionTimeout);

        if (passowrd == null || passowrd.isEmpty()) {
            jedisPool = new JedisPool(jpc, redisHost, redisPort);
        } else {
            jedisPool = new JedisPool(jpc, redisHost, redisPort, redisConnectionTimeout, passowrd);
        }
    }

    /***
     * 获取锁
     * @param lockKey
     * @param requestId
     * @param expireTime
     * @return
     */
    public boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime) {

        try (Jedis jedis = jedisPool.getResource()) {
            String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
            if (LOCK_SUCCESS.equals(result)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /***
     * 释放锁
     * @param lockKey
     * @param requestId
     * @return
     */
    public boolean releaseDistributedLock(String lockKey, String requestId) {
        try (Jedis jedis = jedisPool.getResource()) {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
            if (RELEASE_SUCCESS.equals(result)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public void put(String key, String value, int redisItemTimeout) {

        ThreadManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                try (Jedis jedis = jedisPool.getResource()) {
                    jedis.set(key, value);
                    if (redisItemTimeout > 0) {
                        jedis.expire(key.toString(), redisItemTimeout);
                    }
                } catch (Exception e) {
                    LoggerFactory.getLogger().error(e.getMessage());
                }
            }
        });
    }

    public String get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            LoggerFactory.getLogger().error(e.getMessage());
            return null;
        }
    }


    public void hput(String key, String field, String value, int redisItemTimeout) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(key, field, value);
            if (redisItemTimeout > 0) {
                jedis.expire(key.toString(), redisItemTimeout);
            }
        } catch (Exception e) {
            LoggerFactory.getLogger().error(e.getMessage());
        }
    }

    public String hget(String key, String field) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hget(key, field);
        } catch (Exception e) {
            LoggerFactory.getLogger().error(e.getMessage());
            return null;
        }
    }

    public int hincr(String key, String field, long value, int redisItemTimeout) {
        try (Jedis jedis = jedisPool.getResource()) {
            Long result = jedis.hincrBy(key, field, value);
            if (result == null) {
                return -1;
            }
            if (redisItemTimeout > 0) {
                jedis.expire(key.toString(), redisItemTimeout);
            }
            return result.intValue();
        } catch (Exception e) {
            LoggerFactory.getLogger().error(e.getMessage());
            return -1;
        }
    }

    public void hmset(String key, Map<String, String> hash, int redisItemTimeout) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hmset(key, hash);
            if (redisItemTimeout > 0) {
                jedis.expire(key.toString(), redisItemTimeout);
            }
        } catch (Exception e) {
            LoggerFactory.getLogger().error(e.getMessage());
        }
    }

    public List<String> hmget(String key, String... fields) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hmget(key, fields);
        } catch (Exception e) {
            LoggerFactory.getLogger().error(e.getMessage());
            return null;
        }
    }

    public Long remove(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.del(key);
        } catch (Exception e) {
            LoggerFactory.getLogger().error(e.getMessage());
            return null;
        }
    }

    public Long expire(String key, int seconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.expire(key, seconds);
        } catch (Exception e) {
            LoggerFactory.getLogger().error(e.getMessage());
            return null;
        }
    }
}
