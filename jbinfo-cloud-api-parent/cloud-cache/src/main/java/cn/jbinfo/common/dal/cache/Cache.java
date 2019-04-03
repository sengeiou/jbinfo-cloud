package cn.jbinfo.common.dal.cache;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiaobin on 16/7/8.
 */
public interface Cache {

    void setDatabase(int db);

    int getSize();


    /**
     * 键值对缓存对象
     * @param key key值
     * @param value value值
     */
    void putObject(String key, Object value);

    /**
     * 双键缓存对象
     * 如：key：会议标识，hashKey：会议Id，value：参会人员列表
     * @param key key
     * @param hashKey
     * @param value
     */
    void putObject(String key, String hashKey, Object value);

    /**
     * 键值对缓存对象
     * @param key
     * @param value
     * @param seconds 失效时间
     */
    void putObject(String key, Object value, long seconds);

    /**
     * 从缓存中获取值
     * @param key
     * @param <T>
     * @return
     */
    <T> T getObject(String key);

    /**
     * 获取双键缓存对象的数据
     * @param key
     * @param hashKey
     * @param <T>
     * @return
     */
    <T> T getObject(String key, String hashKey);

    /**
     * 获取：双键缓存对象的第二键和值
     * @param key
     * @return
     */
    Map<String, Object> getHasObject(String key);

    /**
     * 移除缓存对象
     * @param key
     * @param <T>
     * @return
     */
    <T> T removeObject(String key);

    /**
     * 移除缓存对象
     * @param key
     * @param hashKey
     * @param <T>
     * @return
     */
    <T> T removeObject(String key, String hashKey);

    void clear(String id);

    void putObject(String key, Object value, long t, TimeUnit timeUnit);


}