package com.wlx.middleware.rpc.registry;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisRegistryCenter {

    private static Jedis jedis;

    public static void init(String host, int port) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(5);
        jedisPoolConfig.setTestOnBorrow(false);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port);
        jedis = jedisPool.getResource();
    }

    public static void registryProvider(String interfaces, String alias, String info) {
        jedis.sadd(interfaces + "_" + alias, info);
    }

    public static String getProviderInfo(String interfaces, String alias) {
        return jedis.srandmember(interfaces + "_" + alias);
    }

}
