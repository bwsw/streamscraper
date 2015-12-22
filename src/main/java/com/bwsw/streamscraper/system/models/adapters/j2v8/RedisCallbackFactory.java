package com.bwsw.streamscraper.system.models.adapters.j2v8;

import com.bwsw.streamscraper.system.models.BasicHandler;
import com.bwsw.streamscraper.system.models.adapters.ICallbackFactory;
import redis.clients.jedis.JedisCluster;

/**
 * Created by ivan on 21.12.15.
 */
public class RedisCallbackFactory implements ICallbackFactory {
    JedisCluster redis;

    RedisCallbackFactory(JedisCluster redis) {
        this.redis = redis;
    }

    @Override
    public void generate(BasicHandler h) {

    }

}
