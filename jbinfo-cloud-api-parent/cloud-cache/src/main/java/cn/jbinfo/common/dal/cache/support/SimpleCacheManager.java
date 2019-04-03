package cn.jbinfo.common.dal.cache.support;

import cn.jbinfo.common.dal.cache.Cache;
import cn.jbinfo.common.dal.cache.CacheManager;
import cn.jbinfo.common.dal.cache.impl.ConcurrentMapCache;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by xiaobin on 16/7/8.
 */
public class SimpleCacheManager implements InitializingBean, CacheManager {

    private Cache cache;

    //缓存域
    private int region;

    public  SimpleCacheManager(){}

    /**
     * Specify the Cache instances to use for this CacheManager.
     */
    public void setCache(Cache cache) {
        this.cache = cache;
    }


    public Cache getCache() {
        //默認是1
        return cache;
    }

    public Cache getCache(int db) {
        cache.setDatabase(region);
        return cache;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (cache == null) {
            cache = new ConcurrentMapCache();
        }
    }

}