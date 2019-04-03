package cn.jbinfo.cloud.core.jdbc;

import cn.jbinfo.cloud.core.jdbc.dynamic.DynamicJdbcBaseDaoImpl;
import cn.jbinfo.cloud.core.jdbc.dynamic.StatementTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * @author xiaobin
 * @create 2018-03-08 下午7:33
 **/
public class DynamicJdbcTemplate extends DynamicJdbcBaseDaoImpl implements DynamicBaseDao {

    protected Logger log = LoggerFactory.getLogger(getClass());

    public DynamicJdbcTemplate(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * 查询原生sql字段
     * @param queryName
     * @param parameter
     * @return
     */
    @Override
    public Map<String, Object> findByNamedForMap(String queryName, JdbcParameter parameter) {
        List<Map<String, Object>> mapList = findByNamedForListMap(queryName, parameter);
        if (CollectionUtils.isEmpty(mapList))
            return null;
        return mapList.get(0);
    }

    @Override
    public List<Map<String, Object>> findByNamedForListMap(String queryName, JdbcParameter parameter) {
        StatementTemplate statementTemplate = templateCache.get(queryName);
        if (statementTemplate == null) {
            log.error("没有找到:" + queryName);
            return null;
        }
        String statement = processTemplate(statementTemplate, parameter.getMap());
        return queryForList(statement, new MapSqlParameterSource(parameter.getMap()));
    }

    /**
     * 单一数据
     * 如：select count(id) from xxx
     * @param queryName
     * @param parameter
     * @param clazz
     * @param <X>
     * @return
     */
    @Override
    public <X> X queryForObject(String queryName, JdbcParameter parameter, Class<X> clazz) {
        StatementTemplate statementTemplate = templateCache.get(queryName);
        if (statementTemplate == null) {
            log.error("没有找到:" + queryName);
            return null;
        }
        String statement = processTemplate(statementTemplate, parameter.getMap());
        return queryForObject(statement, parameter.getMap(), clazz);
    }

    @Override
    public <X> X findByNamedForBean(String queryName, JdbcParameter parameter, Class<X> clazz) {
        List<X> xList = findByNamedQuery(queryName, parameter, clazz);
        if (CollectionUtils.isEmpty(xList))
            return null;
        return xList.get(0);
    }

    @Override
    public <X> List<X> findByNamedQuery(String queryName, JdbcParameter parameter, Class<X> clazz) {
        StatementTemplate statementTemplate = templateCache.get(queryName);
        if (statementTemplate == null) {
            log.error("没有找到:" + queryName);
            return null;
        }
        String statement = processTemplate(statementTemplate, parameter.getMap());
        return query(statement, parameter.getMap(), new BeanPropertyRowMapper<>(clazz));
    }

    @Override
    public <X> Page<X> queryForPage(String queryName, JdbcParameter parameter, Pageable pagination, Class<X> clazz) {
        StatementTemplate statementTemplate = templateCache.get(queryName);
        if (statementTemplate == null) {
            log.error("没有找到:" + queryName);
            return null;
        }
        String statement = processTemplate(statementTemplate, parameter.getMap());
        JdbcFinder finder = JdbcFinder.create(statement).setParam(parameter.getMap());
        //获取记录条数
        String countSql = finder.getRowCountSql();

        long total = super.queryForObject(countSql, finder.getMparams(), Long.class);

        String sql = finder.getOrigSql();
        sql += parseLimit(pagination);

        List<X> data = super.query(sql, finder.getMparams(), new BeanPropertyRowMapper<>(clazz));
        return new PageImpl<>(data, pagination, total);
    }

    private String parseLimit(Pageable pagination) {

        return " " +
                "limit" +
                " " +
                pagination.getOffset() +
                "," +
                pagination.getPageSize();
    }

    @Override
    public int updateByNamedQuery(String queryName, JdbcParameter parameters) {
        StatementTemplate statementTemplate = templateCache.get(queryName);
        if (statementTemplate == null) {
            log.error("没有找到:" + queryName);
            return 0;
        }
        String statement = processTemplate(statementTemplate, parameters.getMap());
        return super.update(statement, parameters.getMap());
    }
}
