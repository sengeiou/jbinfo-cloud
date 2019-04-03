package cn.jbinfo.common.dal.cache.impl;

import cn.jbinfo.common.dal.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiaobin on 16/7/8.
 */
public class ConcurrentMapCache implements Cache {

    private static final Logger LOG = LoggerFactory.getLogger(ConcurrentMapCache.class);

    private static final ConcurrentMap<String, Object> cache = new ConcurrentHashMap<>();

    @Override
    public void setDatabase(int db) {
        
    }

    public int getSize() {
        return cache.size();
    }

    public void putObject(String key, Object value) {
        cache.put(key, value);
        LOG.debug("Storing " + key + " to cache.");
    }

    @Override
    public void putObject(String key, String hashKey, Object value) {

    }

    public Object getObject(String key) {
        Object value = cache.get(key);
        if(LOG.isDebugEnabled()){
            if(value == null){
                LOG.debug(key + " cache not exists.");
            }else{
                LOG.debug("Reading " + key + " from cache.");
            }
        }
        return value;
    }

    public Object removeObject(String key) {
        LOG.debug("Removing " + key + " from cache.");
        return cache.remove(key);
    }

    public void clear(String id) {
        for (Object o : cache.keySet()) {
            String key = String.valueOf(o);
            if (key.contains(id)) {
                LOG.debug("Clearing " + key + " from cache.");
                cache.remove(key);
            }
        }
        LOG.debug("Clearing *" + id + "* from cache.");
    }

    @Override
    public void putObject(String key, Object value, long t, TimeUnit timeUnit) {
        //nothing
    }

    @Override
    public void putObject(String key, Object value, long seconds) {
        putObject(key, value);
        LOG.debug("Storing " + key + " to cache[seconds:"+seconds+"].");
    }

    @Override
    public <T> T getObject(String key, String hashKey) {
        return null;
    }

    @Override
    public Map<String, Object> getHasObject(String key) {
        return null;
    }

    @Override
    public <T> T removeObject(String key, String hashKey) {
        return null;
    }

}