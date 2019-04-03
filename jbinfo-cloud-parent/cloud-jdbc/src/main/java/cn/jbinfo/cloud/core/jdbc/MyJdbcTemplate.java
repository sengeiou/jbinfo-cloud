package cn.jbinfo.cloud.core.jdbc;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.*;

/**
 * Created by xiaobin on 2016/8/29.
 */
public class MyJdbcTemplate extends NamedParameterJdbcTemplate {

    public MyJdbcTemplate(DataSource dataSource) {
        super(dataSource);
    }

    public Map<String, Object> queryForMap(String sql, String key, Object value) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        List<Map<String, Object>> list = queryForList(sql, map);
        if (CollectionUtils.isEmpty(list))
            return null;
        return list.get(0);
    }

    public Map<String, Object> queryForMap(String sql, String key1, Object value1, String key2, Object value2) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        List<Map<String, Object>> list = queryForList(sql, map);
        if (CollectionUtils.isEmpty(list))
            return null;
        return list.get(0);
    }

    public Map<String, Object> queryForMap(String sql, String key1, Object value1, String key2, Object value2,
                                           String key3, Object value3) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        List<Map<String, Object>> list = queryForList(sql, map);
        if (CollectionUtils.isEmpty(list))
            return null;
        return list.get(0);
    }

    public Map<String, Object> queryForMap(String sql, String key1, Object value1, String key2, Object value2,
                                           String key3, Object value3, String key4, Object value4) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        map.put(key4, value4);
        List<Map<String, Object>> list = queryForList(sql, map);
        if (CollectionUtils.isEmpty(list))
            return null;
        return list.get(0);
    }

    public Map<String, Object> queryForMap(String sql, String key1, Object value1, String key2, Object value2,
                                           String key3, Object value3, String key4, Object value4,
                                           String key5, Object value5) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        map.put(key4, value4);
        map.put(key5, value5);
        List<Map<String, Object>> list = queryForList(sql, map);
        if (CollectionUtils.isEmpty(list))
            return null;
        return list.get(0);
    }

    //------------------------start list map------------------

    public List<Map<String, Object>> queryForList(String sql, String key, Object value) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return queryForList(sql, map);
    }

    public List<Map<String, Object>> queryForList(String sql, String key1, Object value1, String key2, Object value2) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        return queryForList(sql, map);
    }

    public List<Map<String, Object>> queryForList(String sql, String key1, Object value1, String key2, Object value2,
                                                  String key3, Object value3) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        return queryForList(sql, map);
    }

    public List<Map<String, Object>> queryForList(String sql, String key1, Object value1, String key2, Object value2,
                                                  String key3, Object value3, String key4, Object value4) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        map.put(key4, value4);
        return queryForList(sql, map);
    }

    public List<Map<String, Object>> queryForList(String sql, String key1, Object value1, String key2, Object value2,
                                                  String key3, Object value3, String key4, Object value4,
                                                  String key5, Object value5) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        map.put(key4, value4);
        map.put(key5, value5);
        return queryForList(sql, map);
    }

    //------------------------end list map------------------


    //------------------------start object------------------

    public <T> T queryForObject(String sql, Class<T> clazz, String key1, Object value1) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put(key1, value1);
        return queryForObject(sql, map, clazz);
    }

    public <T> T queryForObject(String sql, Class<T> clazz, String key1, Object value1, String key2, Object value2) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        return queryForObject(sql, map, clazz);
    }

    public <T> T queryForObject(String sql, Class<T> clazz, String key1, Object value1, String key2, Object value2,
                                String key3, Object value3) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        return queryForObject(sql, map, clazz);
    }

    public <T> T queryForObject(String sql, Class<T> clazz, String key1, Object value1, String key2, Object value2,
                                String key3, Object value3, String key4, Object value4) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        map.put(key4, value4);
        return queryForObject(sql, map, clazz);
    }

    public <T> T queryForObject(String sql, Class<T> clazz, String key1, Object value1, String key2, Object value2,
                                String key3, Object value3, String key4, Object value4,
                                String key5, Object value5) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        map.put(key4, value4);
        map.put(key5, value5);
        return queryForObject(sql, map, clazz);
    }

    //------------------------end object------------------


    //------------------------start object------------------

    public <T> T queryForBean(String sql, Map<String, Object> map, Class<T> clazz) throws DataAccessException {
        List<T> list = query(sql, map, new BeanPropertyRowMapper<>(clazz));
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    public <T> T queryForBean(String sql, Class<T> clazz, String key1, Object value1) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put(key1, value1);
        List<T> list = query(sql, map, new BeanPropertyRowMapper<>(clazz));
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    public <T> T queryForBean(String sql, Class<T> clazz, String key1, Object value1, String key2, Object value2) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        List<T> list = query(sql, map, new BeanPropertyRowMapper<>(clazz));
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    public <T> T queryForBean(String sql, Class<T> clazz, String key1, Object value1, String key2, Object value2,
                              String key3, Object value3) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        List<T> list = query(sql, map, new BeanPropertyRowMapper<>(clazz));
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    public <T> T queryForBean(String sql, Class<T> clazz, String key1, Object value1, String key2, Object value2,
                              String key3, Object value3, String key4, Object value4) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        map.put(key4, value4);
        List<T> list = query(sql, map, new BeanPropertyRowMapper<>(clazz));
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    public <T> T queryForBean(String sql, Class<T> clazz, String key1, Object value1, String key2, Object value2,
                              String key3, Object value3, String key4, Object value4,
                              String key5, Object value5) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        map.put(key4, value4);
        map.put(key5, value5);
        List<T> list = query(sql, map, new BeanPropertyRowMapper<>(clazz));
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    //------------------------end object------------------

    //------------------------start list map------------------

    public <T> List<T> queryForListBean(String sql, Map<String, Object> map, Class<T> clazz) throws DataAccessException {
        return query(sql, map, new BeanPropertyRowMapper<>(clazz));
    }

    //------------------------end list map------------------


    public <T> Page<T> queryForPage(String sql, Map<String, Object> map,
                                    Pageable pagination, RowMapper<T> rowMapper) throws DataAccessException {
        return queryForPage(sql, pagination, map, rowMapper);
    }

    public <T> T queryFinder(JdbcFinder finder, RowMapper<T> rowMapper) throws DataAccessException {

        return queryForObject(finder.getOrigSql(), finder.getMparams(), rowMapper);
    }


    public <T> Page<T> queryForPage(JdbcFinder finder, Pageable pagination, RowMapper<T> rowMapper) throws DataAccessException {
        //获取记录条数
        String countSql = finder.getRowCountSql();

        long total = super.queryForObject(countSql, finder.getMparams(), Long.class);

        String sql = finder.getOrigSql();
        sql += parseLimit(pagination);

        List<T> data = super.query(sql, finder.getMparams(), rowMapper);
        Page<T> result = new PageImpl<>(data, pagination, total);
        return result;
    }


    public <T> Page<T> queryForPage(String sql, Pageable pagination,
                                    Map<String, Object> map, RowMapper<T> var3) throws DataAccessException {


        //获取记录条数
        String countSql = "select count(1) as count from (" + sql + ") temp";

        long total = super.queryForObject(countSql, map, Long.class);
        sql += parseLimit(pagination);

        List<T> data = super.query(sql, map, var3);
        Page<T> result = new PageImpl<>(data, pagination, total);
        return result;
    }

    public <T> int insertBean(String tableName, T bean) {
        Map<String, Object> map = beanToMap(bean);
        StringBuilder sql = new StringBuilder();
        sql.append("insert into  ").append(tableName).append(" (");
        Set<String> columnList = map.keySet();
        sql.append(StringUtils.join(columnList, ','));
        sql.append(" ) ");
        sql.append("values(");
        sql.append(StringUtils.join(paramList(columnList), ','));
        sql.append(")");
        return super.update(sql.toString(), map);
    }

    public <T> int updateBean(String tableName, T bean) {
        Map<String, Object> map = beanToMap(bean);
        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(tableName).append(" set ");
        Set<String> columnList = map.keySet();
        List<String> updateParam = new ArrayList<>();
        for (String key : columnList) {
            if ("id".equals(key))
                continue;
            updateParam.add(key + "=:" + key);
        }

        sql.append(StringUtils.join(updateParam, ','));
        sql.append(" where id=:id");
        return super.update(sql.toString(), map);
    }


    private List<String> paramList(Set<String> columnList) {
        List<String> list = new ArrayList<>();
        for (String column : columnList) list.add(":" + column);
        return list;
    }

    private static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                if (beanMap.get(key) == null)
                    continue;
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }


    private String parseLimit(Pageable pagination) {

        String stringBuffer = " " +
                "limit" +
                " " +
                pagination.getOffset() +
                "," +
                pagination.getPageSize();

        return stringBuffer;
    }


    protected static class Demo {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
