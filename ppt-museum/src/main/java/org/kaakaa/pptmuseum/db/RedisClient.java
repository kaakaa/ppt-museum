package org.kaakaa.pptmuseum.db;

import org.kaakaa.pptmuseum.db.mongo.DBResourceBundle;
import redis.clients.jedis.BinaryJedis;

/**
 * Created by kaakaa on 16/02/20.
 */
public class RedisClient {
    private static final BinaryJedis jedis = new BinaryJedis(DBResourceBundle.getString("redis.host"));

    public static void upload(String id, byte[] file) {
        jedis.set(id.getBytes(), file);
    }

    public static byte[] get(String id) {
        return jedis.get(id.getBytes());
    }
}
