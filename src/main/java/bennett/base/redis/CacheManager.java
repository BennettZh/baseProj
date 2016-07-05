package bennett.base.redis;

import java.util.List;


public interface CacheManager<T> {
	public JedisTemplate getJedisTemplate();

	public void notifyCacheinvalid(String cacheCategory);

	public List<T> getCacheList(final String key);

	public void setCacheList(final String key, final List<T> vos);

	public boolean isOutOfDate(String key);
}
