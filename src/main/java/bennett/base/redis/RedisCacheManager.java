package bennett.base.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bennett.base.redis.JedisTemplate.JedisAction;
import bennett.base.redis.JedisTemplate.JedisActionNoResult;
import bennett.base.redis.pool.JedisPool;
import bennett.base.redis.pool.JedisPoolBuilder;
import redis.clients.jedis.Jedis;


public class RedisCacheManager<T> implements CacheManager<T> {

	public RedisCacheManager() {
		super();
	}
	
//	public RedisCacheManager(String host, String port, String maxPoolSize,String password) {
//		JedisPoolBuilder builder = new JedisPoolBuilder();
//		builder.setMasterName("direct:" + host + ":" + port);
//		builder.setPoolSize(Integer.parseInt(maxPoolSize));
//		builder.setPassword(password);
//		setJedisPool(builder.buildPool());
//	}


	public RedisCacheManager(String host, String port, String maxPoolSize) {
		JedisPoolBuilder builder = new JedisPoolBuilder();
		builder.setMasterName("direct:" + host + ":" + port);
		builder.setPoolSize(Integer.parseInt(maxPoolSize));
		setJedisPool(builder.buildPool());
	}

	private void setJedisPool(JedisPool jedisPool) {
		this.jedisTemplate = new JedisTemplate(jedisPool);

	}

	public final static Logger logger = LoggerFactory.getLogger(RedisCacheManager.class);
	private JedisTemplate jedisTemplate;

	public JedisTemplate getJedisTemplate() {
		return jedisTemplate;
	}

	public void setJedisTemplate(JedisTemplate jedisTemplate) {
		this.jedisTemplate = jedisTemplate;
	}

	@Override
	public void notifyCacheinvalid(String cacheCategory) {

	}

	@Override
	public List<T> getCacheList(final String key) {
		if (isOutOfDate(key)) {
			return null;
		}
		return jedisTemplate.execute(new JedisAction<List<T>>() {
			@Override
			public List<T> action(Jedis jedis) {
				byte[] out = jedis.get(key.getBytes());
				if (out == null) {
					return null;
				}
				return (List<T>) ListTranscoder.deserialize(out);
			}
		});
	}

	@Override
	public void setCacheList(final String key, final List<T> vos) {
		jedisTemplate.execute(new JedisActionNoResult() {
			@Override
			public void action(Jedis jedis) {
				jedis.set(key.getBytes(), ListTranscoder.serialize(vos));
			}
		});
	}

	static class ListTranscoder {
		public static byte[] serialize(Object value) {
			long begin = System.currentTimeMillis();
			if (value == null) {
				throw new NullPointerException("Can't serialize null");
			}
			byte[] rv = null;
			ByteArrayOutputStream bos = null;
			ObjectOutputStream os = null;
			try {
				bos = new ByteArrayOutputStream();
				os = new ObjectOutputStream(bos);
				os.writeObject(value);
				os.close();
				bos.close();
				rv = bos.toByteArray();
			} catch (IOException e) {
				throw new IllegalArgumentException("Non-serializable object", e);
			} finally {
				try {
					os.close();
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			logger.debug("serialize:" + String.valueOf(System.currentTimeMillis() - begin));
			return rv;
		}

		public static Object deserialize(byte[] in) {
			long begin = System.currentTimeMillis();
			Object rv = null;
			ByteArrayInputStream bis = null;
			ObjectInputStream is = null;
			try {
				if (in != null) {
					bis = new ByteArrayInputStream(in);
					is = new ObjectInputStream(bis);
					rv = is.readObject();
					is.close();
					bis.close();
				}
			} catch (IOException e) {
				logger.warn("Caught IOException decoding %d bytes of data", in == null ? 0 : in.length, e);
			} catch (ClassNotFoundException e) {
				logger.warn("Caught CNFE decoding %d bytes of data", in == null ? 0 : in.length, e);
			} finally {
				try {
					is.close();
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			logger.debug("deserialize:" + String.valueOf(System.currentTimeMillis() - begin));
			return rv;
		}
	}

	@Override
	public boolean isOutOfDate(String key) {
		return CacheValidateUtil.isOutofDate(key);
	}

}
