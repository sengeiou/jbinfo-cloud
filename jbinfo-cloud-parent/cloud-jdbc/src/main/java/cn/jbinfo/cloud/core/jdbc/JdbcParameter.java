package cn.jbinfo.cloud.core.jdbc;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaobin
 * @create 2018-03-09 上午9:20
 **/
public class JdbcParameter {

    private Map<String, Object> map;

    public JdbcParameter() {
        map = new HashMap<>();
    }

    public static JdbcParameter create() {
        return new JdbcParameter();
    }

    public static JdbcParameter create(Map<String,Object> value) {
        return new JdbcParameter(value);
    }

    public static JdbcParameter create(String key, Object value) {
        return new JdbcParameter(key, value);
    }

    public JdbcParameter insert(String key, Object value) {
        map.put(key, value);
        return this;
    }

    public JdbcParameter insert(Map<String, Object> value) {
        map.putAll(value);
        return this;
    }

    public JdbcParameter insertFilterNullValue(String key, String value) {
        if (StringUtils.isBlank(value)) {
            return this;
        }
        map.put(key, value);
        return this;
    }

    public JdbcParameter(String key, Object value) {
        map = new HashMap<String, Object>();
        map.put(key, value);
    }

    public JdbcParameter(Map<String,Object> value) {
        this.map = value;
    }


    public Map<String, Object> getMap() {
        return map;
    }
}
