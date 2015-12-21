package com.bwsw.streamscraper.system.models.adapters;

import com.bwsw.streamscraper.system.models.BasicHandler;
import redis.clients.jedis.JedisCluster;

/**
 * Created by ivan on 21.12.15.
 */
public class J2V8RedisCallbackFactory implements ICallbackFactory {
    JedisCluster redis;

    J2V8RedisCallbackFactory(JedisCluster redis) {
        this.redis = redis;
    }

    @Override
    public void generate(BasicHandler h) {

    }
}
