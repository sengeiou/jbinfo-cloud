package cn.jbinfo.cloud.core.jdbc;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaobin
 * @create 2017-10-27 下午1:05
 **/
public class JdbcFinder {

    protected JdbcFinder() {
        hqlBuilder = new StringBuilder();
    }

    protected JdbcFinder(String hql) {
        hqlBuilder = new StringBuilder(hql);
    }

    public static JdbcFinder create() {
        return new JdbcFinder();
    }

    public static JdbcFinder create(String hql) {
        return new JdbcFinder(hql);
    }

    public JdbcFinder setAliasName(String aliasName) {
        this.aliasName = aliasName;
        return this;
    }

    public JdbcFinder append(String hql) {
        hqlBuilder.append(" ").append(hql);
        return this;
    }


    public JdbcFinder where() {
        hqlBuilder.append(" where ");
        return this;
    }

    public JdbcFinder where(String sql) {
        hqlBuilder.append(" where ").append(sql).append(" ");
        return this;
    }

    public JdbcFinder and() {
        hqlBuilder.append(" and ");
        return this;
    }

    public JdbcFinder and(String sql) {
        hqlBuilder.append(" and ").append(sql).append(" ");
        return this;
    }

    public JdbcFinder or() {
        hqlBuilder.append(" or ");
        return this;
    }

    public JdbcFinder or(String sql) {
        hqlBuilder.append(" or ").append(sql).append(" ");
        return this;
    }

    public JdbcFinder orderBy_desc(String param) {
        setOrders(param, OrderType.DESC);
        return this;
    }

    public JdbcFinder orderBy_asc(String param) {
        setOrders(param, OrderType.ASC);
        return this;
    }

    /**
     * like
     *
     * @param value
     * @return
     */
    public JdbcFinder like(Object value) {
        if (value instanceof String) {
            value = (value.toString())
                    .toUpperCase();
        }
        String param = setWheres(value);
        hqlBuilder.append(" like :").append(param);
        return setParam(param, "%" + value + "%");
    }

    public JdbcFinder likeNoUn(Object value) {
        String param = setWheres(value);
        hqlBuilder.append(" like :").append(param);
        return setParam(param, "%" + value + "%");
    }

    /**
     * 相等
     *
     * @param value
     * @return
     */
    public JdbcFinder eq(Object value) {
        if (value instanceof String) {
            value = ((String) value).toUpperCase();
        }
        String param = setWheres(value);
        hqlBuilder.append(" =:").append(param);
        return setParam(param, value);
    }

    /**
     * 不相等
     *
     * @param value
     * @return
     */
    public JdbcFinder noEq(Object value) {
        String param = setWheres(value);
        hqlBuilder.append(" !=:").append(param);
        return setParam(param, value);
    }

    /**
     * 大于等于
     *
     * @param value
     * @return
     */
    public JdbcFinder ge(Object value) {
        String param = setWheres(value);
        hqlBuilder.append(" >=:").append(param);
        return setParam(param, value);
    }

    /**
     * 大于
     *
     * @param value
     * @return
     */
    public JdbcFinder gt(Object value) {
        String param = setWheres(value);
        hqlBuilder.append(" >:").append(param);
        return setParam(param, value);
    }

    /**
     * 小于等于
     *
     * @param value
     * @return
     */
    public JdbcFinder le(Object value) {
        String param = setWheres(value);
        hqlBuilder.append(" <=:").append(param);
        return setParam(param, value);
    }

    /**
     * 小于
     *
     * @param value
     * @return
     */
    public JdbcFinder lt(Object value) {
        String param = setWheres(value);
        hqlBuilder.append(" <:").append(param);
        return setParam(param, value);
    }

    /**
     * 不等于
     *
     * @param value
     * @return
     */
    public JdbcFinder ne(Object value) {
        String param = setWheres(value);
        hqlBuilder.append(" !=:").append(param);
        return setParam(param, value);
    }

    /**
     * 不为空
     *
     * @return
     */
    public JdbcFinder isNotNull() {
        hqlBuilder.append(" is not null");
        return this;
    }

    /**
     * 为空
     *
     * @return
     */
    public JdbcFinder isNull() {
        hqlBuilder.append(" is null");
        return this;
    }

    /**
     * in
     *
     * @param value
     * @return
     */
    public JdbcFinder in(List<?> value) {
        String param = setWheres(value);
        hqlBuilder.append(" in(:").append(param).append(")");
        return setParam(param, value);
    }

    /**
     * in
     *
     * @param sql
     * @return
     */
    public JdbcFinder in(String sql) {
        hqlBuilder.append(" in(").append(sql).append(")");
        return this;
    }

    /**
     * not in
     *
     * @param value
     * @return
     */
    public JdbcFinder notIn(List<?> value) {
        String param = setWheres(value);
        hqlBuilder.append(" not in(:").append(param).append(")");
        return setParam(param, value);
    }

    /**
     * not in
     *
     * @param sql
     * @return
     */
    public JdbcFinder notIn(String sql) {
        hqlBuilder.append(" not in(").append(sql).append(")");
        return this;
    }

    /**
     * 统一用and连接,此查询不存在or连接
     *
     * @param key
     * @param value
     * @param type
     * @return
     */
    public JdbcFinder search(String key, Object value, SearchType type) {
        if (StringUtils.isBlank(key) || value == null)
            return this;
        if (value instanceof String) {
            String v = (String) value;
            if (StringUtils.isBlank(v)) {
                return this;
            }
        }
        if (type == SearchType.EQ) {
            return and(key).eq(value);
        } else if (type == SearchType.NEQ) {
            return and(key).noEq(value);
        } else if (type == SearchType.GE) {
            return and(key).ge(value);
        } else if (type == SearchType.GT) {
            return and(key).gt(value);
        } else if (type == SearchType.LE) {
            return and(key).le(value);
        } else if (type == SearchType.LT) {
            return and(key).lt(value);
        } else if (type == SearchType.LIKE) {
            return and(key).likeNoUn(value);
        }
        throw new RuntimeException("Null in point searchtype");
    }

    /**
     * 获得原始hql语句
     *
     * @return
     */
    public String getOrigSql() {
        return hqlBuilder.toString();
    }

    private String getAliasName() {
        if (StringUtils.isBlank(aliasName)) {
            return "";
        }
        return " as " + aliasName;
    }

    /**
     * 获得查询数据库记录数的sql语句。
     *
     * @return
     */
    public String getRowCountSql() {
        String hql = hqlBuilder.toString();
        return "select count(*) from ( ".concat(hql).concat(" ) temp") + getAliasName();
    }

    public static void main(String[] args) {
        JdbcFinder finder = JdbcFinder.create("select * from log_device_summary_day a where 1=1");
        finder.search("a.createDay", "1", SearchType.GE);
        finder.search("a.createDay", "2", SearchType.LE);
        finder.search("a.tenantName", "5", SearchType.LIKE);
        finder.append(" order by a.createDay desc,a.id asc");


        System.out.println(finder.getRowCountSql());
    }

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public JdbcFinder setParam(Object args) {
        getValues().add(args);
        return this;
    }

    public JdbcFinder setParam(String key, Object value) {
        if (value != null)
            getMaps().put(key, value);
        return this;
    }

    public JdbcFinder setParam(Map<String, Object> map) {
        getMaps().putAll(map);
        return this;
    }

    /**
     * * @return
     */
    public JdbcFinder setParams(List<Object> args) {
        for (Object object : args) {
            setParam(object);
        }
        return this;
    }

    private List<String> getOrders() {
        if (orders == null)
            orders = new ArrayList<>();
        return orders;
    }

    private void setOrders(String order, OrderType orderType) {
        String type;
        if (orderType == OrderType.ASC) {
            type = " asc";
        } else {
            type = " desc";
        }
        if (getOrders().size() < 1)
            hqlBuilder.append(" order by ").append(order).append(type);
        else
            hqlBuilder.append(" ,").append(order).append(type);
        getOrders().add(order);
    }

    private String wrapProjection(String projection) {
        if (!projection.contains("select")) {
            return ROW_COUNT;
        } else {
            return projection.replace("select", "select count(") + ") ";
        }
    }

    private List<Object> getWheres() {
        if (wheres == null) {
            wheres = new ArrayList<>();
        }
        return wheres;
    }

    private String setWheres(Object where) {
        String param = "param" + getWheres().size();
        getWheres().add(where);
        return param;
    }

    private List<Object> getValues() {
        if (values == null) {
            values = new ArrayList<>();
        }
        return values;
    }

    private Map<String, Object> getMaps() {
        if (maps == null) {
            maps = new HashMap<>();
        }
        return maps;
    }

    public Object[] getParams() {
        if (values == null || values.size() < 1)
            return null;
        return values.toArray();
    }

    public Map<String, Object> getMparams() {
        return maps;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    private StringBuilder hqlBuilder;
    //别名
    private String aliasName;

    private Class clazz;

    private List<Object> values;

    private List<String> orders;

    private List<Object> wheres;

    private Map<String, Object> maps;

    private int firstResult = 0;

    private int maxResults = 0;

    public static final String ROW_COUNT = "select count(1) ";
    public static final String FROM = "from";
    public static final String DISTINCT = "distinct";
    public static final String HQL_FETCH = "fetch";
    public static final String ORDER_BY = "order ";

    public static enum OrderType {
        ASC, DESC
    }

    public static enum SearchType {
        EQ, NEQ, GE, GT, LE, LT, LIKE
    }

}