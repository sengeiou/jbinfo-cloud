package cn.jbinfo.cloud.core.jdbc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author xiaobin
 * @create 2018-03-09 上午9:17
 **/
public interface DynamicBaseDao {


    Map<String, Object> findByNamedForMap(final String queryName, final JdbcParameter parameters);

    List<Map<String, Object>> findByNamedForListMap(final String queryName, final JdbcParameter parameters);


    <X> X queryForObject(final String queryName, final JdbcParameter parameters, Class<X> clazz);

    <X> X findByNamedForBean(final String queryName,
                             final JdbcParameter parameters, Class<X> clazz);


    /**
     * 查询在xxx.dynamic.xml中配置的查询语句
     * <p>
     * 可查询sql或者hql占位符的语句,也可以直接写sql或者hql
     * </p>
     *
     * @param queryName
     * @param parameters
     * @return
     */
    <X> List<X> findByNamedQuery(final String queryName,
                                 final JdbcParameter parameters, Class<X> clazz);


    /**
     * 查询在xxx.dynamic.xml中配置的查询语句
     * <p>
     * 可查询sql或者hql占位符的语句,也可以直接写sql或者hql
     * </p>
     *
     * @return
     */
    <X> Page<X> queryForPage(final String queryName, final JdbcParameter parameters,
                                    Pageable pagination, Class<X> clazz);

    /**
     * 更新在xxx.dynamic.xml中配置的更新语句
     * <p>
     * 可更新sql或者hql占位符的语句,也可以直接写sql或者hql
     * </p>
     *
     * @param queryName  query名称
     * @param parameters 参数
     * @return
     */
    int updateByNamedQuery(final String queryName,
                           final JdbcParameter parameters);
}
