package org.kaakaa.pptmuseum.db;

import org.kaakaa.pptmuseum.db.mongo.DBResourceBundle;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by kaakaa on 16/02/20.
 */
public class RedisClient {
    private static final JedisPool pool = new JedisPool(new JedisPoolConfig(), DBResourceBundle.getString("redis.host"));

    public static void upload(String id, byte[] file) {
        try (BinaryJedis bJedis = pool.getResource()) {
            bJedis.set(id.getBytes(), file);
        }
    }

    public static byte[] get(String id) {
        try (BinaryJedis bJedis = pool.getResource()) {
            return bJedis.get(id.getBytes());
        }
    }
}
