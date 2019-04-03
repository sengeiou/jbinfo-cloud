package cn.jbinfo.cloud.jpa.dynamic;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查询参数类
 *
 * @author xiaobin
 */
public class Parameter {

    private Map<String, Object> map;

    private String namedQuery;

    public Parameter() {
        map = new LinkedHashMap<>();
    }

    public static Parameter create() {
        return new Parameter();
    }

    /**
     * 創建動態sql
     *
     * @param namedQuery ：對應動態sql的名稱
     * @return
     */
    public static Parameter create(String namedQuery) {
        Parameter parameter = new Parameter();
        parameter.namedQuery = namedQuery;
        return parameter;
    }

    public Parameter insertExcludeDel() {
        map.put("delFlag", "0");
        return this;
    }

    public Parameter insertExcludeDel(String alias) {
        map.put(alias + ".delFlag", "0");
        return this;
    }

    public static Parameter insert(Map<String, Object> map) {
        Parameter parameter = new Parameter();
        parameter.map = map;
        return parameter;
    }

    public Parameter insert(String key, Object value) {
        map.put(key, value);
        return this;
    }

    public Parameter insertExcludeNull(String key, Object value) {
        if (StringUtils.isBlank(key) || value == null)
            return this;
        if (StringUtils.isBlank(value.toString()))
            return this;
        map.put(key, value);
        return this;
    }

    public Parameter(String key, Object value) {
        map = new LinkedHashMap<>();
        map.put(key, value);
    }


    public Map<String, Object> getMap() {
        return map;
    }

    public String getNamedQuery() {
        return namedQuery;
    }


}
