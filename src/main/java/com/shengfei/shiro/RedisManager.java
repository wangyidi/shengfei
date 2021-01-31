package com.shengfei.shiro;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

@Data
public class RedisManager extends BaseRedisManager implements IRedisManager {

	@Value("${redis.host}")
	private String HOST;

	@Value("${redis.port}")
	private int PORT;

	// timeout for jedis try to connect to redis server, not expire time! In milliseconds
	private int timeout = Protocol.DEFAULT_TIMEOUT;

	@Value("${redis.password}")
	private String PASSWORD;

	private int database = Protocol.DEFAULT_DATABASE;

	private JedisPool jedisPool;

	private void init() {
		synchronized (this) {
			if (jedisPool == null) {
					jedisPool = new JedisPool(getJedisPoolConfig(), HOST, PORT, timeout, PASSWORD, database);
				}
			}
		}

	@Override
	protected Jedis getJedis() {
		if (jedisPool == null) {
			init();
		}
		return jedisPool.getResource();
	}
}
