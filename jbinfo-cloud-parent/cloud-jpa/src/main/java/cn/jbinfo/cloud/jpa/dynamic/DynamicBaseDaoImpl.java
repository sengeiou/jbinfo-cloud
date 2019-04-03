package cn.jbinfo.cloud.jpa.dynamic;

import cn.jbinfo.cloud.jpa.data.Finder;
import com.google.common.collect.Lists;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import static freemarker.template.Configuration.VERSION_2_3_25;

/**
 * Hibernate动态Sql
 */
public abstract class DynamicBaseDaoImpl<T, ID> extends SimpleJpaRepository<T, ID> implements
        InitializingBean, DynamicBaseDao<T, ID> {
    private static final Logger LOGER = LoggerFactory
            .getLogger(DynamicBaseDaoImpl.class);

    protected EntityManager em;

    protected Class<T> tidClass;

    /**
     * 模板缓存
     */
    protected Map<String, StatementTemplate> templateCache;
    protected DynamicHibernateAssembleBuilder dynamicAssembleBuilder;

    public DynamicBaseDaoImpl(Class<T> tClass, EntityManager entityManager) {
        super(tClass, entityManager);
        this.tidClass = tClass;
        this.em = entityManager;
    }


    @Autowired
    public void setDynamicStatementBuilder(
            DynamicHibernateAssembleBuilder dynamicAssembleBuilder) {
        this.dynamicAssembleBuilder = dynamicAssembleBuilder;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        templateCache = new ConcurrentHashMap<>();

        dynamicAssembleBuilder.init();
        Map<String, String> namedHQLQueries = dynamicAssembleBuilder
                .getNamedHQLQueries();
        Map<String, String> namedSQLQueries = dynamicAssembleBuilder
                .getNamedSQLQueries();
        Configuration configuration = new Configuration(VERSION_2_3_25);
        configuration.setNumberFormat("#");
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        for (Entry<String, String> entry : namedHQLQueries.entrySet()) {
            stringLoader.putTemplate(entry.getKey(), entry.getValue());
            templateCache
                    .put(tidClass.getSimpleName() + "_" + entry.getKey(),
                            new StatementTemplate(StatementTemplate.TYPE.HQL,
                                    new Template(entry.getKey(),
                                            new StringReader(entry.getValue()),
                                            configuration)));
        }
        for (Entry<String, String> entry : namedSQLQueries.entrySet()) {
            stringLoader.putTemplate(entry.getKey(), entry.getValue());
            templateCache
                    .put(tidClass.getSimpleName() + "_" + entry.getKey(),
                            new StatementTemplate(StatementTemplate.TYPE.SQL,
                                    new Template(entry.getKey(),
                                            new StringReader(entry.getValue()),
                                            configuration)));
        }
        configuration.setTemplateLoader(stringLoader);
    }

    protected String processTemplate(StatementTemplate statementTemplate,
                                     Map<String, ?> parameters) {
        StringWriter stringWriter = new StringWriter();
        try {
            statementTemplate.getTemplate().process(parameters, stringWriter);
        } catch (Exception e) {
            LOGER.error("处理DAO查询参数模板时发生错误：{}", e.toString());
            throw new RuntimeException(e);
        }
        return stringWriter.toString();
    }

    @Override
    public <X> List<X> findByNamedQuery(String queryName, Parameter parameter, Class<X>... clazzs) {
        StatementTemplate statementTemplate = templateCache.get(tidClass.getSimpleName() + "_" + queryName);
        if (statementTemplate == null) {
            LOGER.error("没有找到:" + queryName);
            return null;
        }
        String statement = processTemplate(statementTemplate, parameter.getMap());
        if (statementTemplate.getType() == StatementTemplate.TYPE.HQL) {
            return this.findByHQL(statement, parameter);
        } else {
            Class<X> clazz = null;
            if (clazzs != null && clazzs.length > 0) {
                clazz = clazzs[0];
            }
            return this.findBySQL(statement, clazz, parameter);
        }
    }

    public <X> Page<X> findPageByNamedQuery(Parameter parameter, Pageable p, Class<X> clazzs) {
        if (StringUtils.isBlank(parameter.getNamedQuery())) {
            throw new RuntimeException("namedQuery不能为空");
        }
        String queryName = parameter.getNamedQuery();
        StatementTemplate statementTemplate = templateCache.get(tidClass.getSimpleName() + "_" + queryName);
        if (statementTemplate == null) {
            LOGER.error("没有找到:" + queryName);
            return null;
        }
        Map<String, Object> parameters = parameter.getMap();
        String statement = processTemplate(statementTemplate, parameters);

        StatementTemplate statementTemplate_pagecount = templateCache
                .get(tidClass.getSimpleName() + "_" + queryName + "-count");

        Finder finder = Finder.create(statement);
        for (Entry<String, Object> entry : parameters.entrySet()) {
            if (StringUtils.contains(statement, ":" + entry.getKey())) {
                finder.setParam(entry.getKey(), entry.getValue());
            }
        }
        if (statementTemplate_pagecount != null) {
            String statement_pagecount = processTemplate(
                    statementTemplate_pagecount, parameters);
            finder.setRowCountSql(statement_pagecount);
        }
        if (statementTemplate.getType() == StatementTemplate.TYPE.HQL) {
            return findByClass(finder, p, clazzs);
        } else {
            return findDynamPage(finder, p, clazzs);
        }
    }

    private <X> Page<X> findDynamPage(Finder finder, Pageable p,
                                      Class<X> clazz) {
        int totalCount = countSQLQueryResult(finder);
        if (totalCount < 1) {
            return new PageImpl<>(Lists.newArrayList(), p, totalCount);
        }
        Query q = em.createNativeQuery(finder.getOrigHql());
        SQLQuery query = q.unwrap(SQLQuery.class);
        if (clazz.getName().equals(Map.class.getName())) {
            query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        } else {
            query.setResultTransformer(Transformers.aliasToBean(clazz));
        }
        finder.setParamsToQuery(query);
        query.setFirstResult(Long.valueOf(p.getOffset()).intValue());
        query.setMaxResults(p.getPageSize());
        if (finder.isCacheable()) {
            query.setCacheable(true);
        }
        List<X> list = query.list();
        return new PageImpl<>(list, p, totalCount);
    }

    /**
     * 获得Finder的记录总数
     *
     * @param finder
     * @return
     */
    protected int countSQLQueryResult(Finder finder) {
        Query q = em.createNativeQuery(finder.getRowCountHql());
        org.hibernate.query.Query query = q.unwrap(org.hibernate.query.Query.class);
        finder.setParamsToQuery(query);
        if (finder.isCacheable()) {
            query.setCacheable(true);
        }
        Object obj = query.uniqueResult();
        if (obj == null)
            return 0;
        return NumberUtils.toInt(obj.toString(), 0);
    }

    public <X> Page<X> findByClass(Finder finder, Pageable p, Class<X> clazz) {
        int totalCount = countQueryResult(finder);
        PageImpl<X> page;
        if (totalCount < 1) {
            page = new PageImpl<>(Lists.newArrayList(), p, 0);
            return page;
        }
        Query q = em.createQuery(finder.getOrigHql());
        org.hibernate.query.Query query = q.unwrap(org.hibernate.query.Query.class);
        if (clazz != null) {
            if (clazz.getName().equals(Map.class.getName())) {
                query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            }
        }
        finder.setParamsToQuery(query);
        query.setFirstResult(Long.valueOf(p.getOffset()).intValue());
        query.setMaxResults(p.getPageSize());
        List list = query.getResultList();
        page = new PageImpl<>(list, p, totalCount);
        return page;
    }


    /**
     * 获得Finder的记录总数
     *
     * @param finder
     * @return
     */
    protected int countQueryResult(Finder finder) {
        if (finder.getOrigHql().contains("group by")) {
            Query q = em.createQuery(finder.getOrigHql());
            org.hibernate.query.Query query = q.unwrap(org.hibernate.query.Query.class);
            finder.setParamsToQuery(query);
            List<?> iterator = query.getResultList();
            return iterator.size();
        }

        Query q = em.createQuery(finder.getRowCountHql());
        org.hibernate.query.Query query = q.unwrap(org.hibernate.query.Query.class);
        finder.setParamsToQuery(query);
        return NumberUtils.toInt(query.getSingleResult().toString(), 0);
    }


    @Override
    public int updateByNamedQuery(Parameter parameters) {
        String queryName = parameters.getNamedQuery();
        if (StringUtils.isBlank(queryName)) {
            throw new RuntimeException("namedQuery不能为空");
        }
        StatementTemplate statementTemplate = templateCache.get(tidClass.getSimpleName() + "_" + queryName);
        if (statementTemplate == null) {
            LOGER.error("没有找到:" + queryName);
            return 0;
        }
        String statement = processTemplate(statementTemplate, parameters.getMap());
        //log.info(String.format("sql=%s", statement));
        if (statementTemplate.getType() == StatementTemplate.TYPE.HQL) {

            return batchExecuteHQL(statement, parameters);
        } else {
            return batchExecuteSQL(statement, parameters);
        }
    }

    /**
     * 执行HQL进行批量修改/删除操作.
     *
     * @param values 命名参数,按名称绑定.
     * @return 更新记录数.
     */
    protected int batchExecuteHQL(final String hql, final Parameter values) {
        return createHQLQuery(hql, values).executeUpdate();
    }

    protected List findBySQL(String sql, Class clazz, Parameter values) {
        return createSQLQuery(sql, clazz, values).getResultList();
    }


    /**
     * 执行SQL进行批量修改/删除操作.
     *
     * @param values 命名参数,按名称绑定.
     * @return 更新记录数.
     */
    protected int batchExecuteSQL(final String sql, Parameter values) {
        return createSQLQuery(sql, null, values).executeUpdate();
    }

    /**
     * 按HQL查询对象列表.
     */
    @SuppressWarnings("unchecked")
    protected <X> List<X> findByHQL(final String hql,
                                    final Parameter parameter) {
        return createHQLQuery(hql, parameter).getResultList();
    }

    protected Query createHQLQuery(final String queryString,
                                   final Parameter parameter) {
        Map<String, Object> values = parameter.getMap();
        Query query = em.createQuery(queryString);
        if (values != null) {
            for (Entry<String, ?> entry : values.entrySet()) {
                if (StringUtils.contains(queryString,
                        String.format(":%s", entry.getKey()))) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }
        return query;
    }

    /**
     * 根据查询SQL与参数列表创建Query对象. 与find()函数可进行更加灵活的操作.
     *
     * @param queryString SQL语句
     */
    protected Query createSQLQuery(final String queryString, Class<?> clazz,
                                   final Parameter parameter) {

        Query query = em.createNativeQuery(queryString);
        Map<String, Object> values = parameter.getMap();
        SQLQuery sqlQuery = query.unwrap(SQLQuery.class);
        if (clazz != null) {
            if (clazz.getName().equals(Map.class.getName())) {
                sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            } else {
                sqlQuery.setResultTransformer(Transformers.aliasToBean(clazz));
            }
        }
        if (values != null) {
            for (Entry<String, ?> entry : values.entrySet()) {
                if (StringUtils.contains(queryString,
                        String.format(":%s", entry.getKey()))) {
                    if (entry.getValue() != null && ((entry.getValue() instanceof Array))) {
                        sqlQuery.setParameterList(entry.getKey(), (Object[]) entry.getValue());
                    } else if (entry.getValue() != null && ((entry.getValue() instanceof List))) {
                        sqlQuery.setParameterList(entry.getKey(), (List) entry.getValue());
                    } else if (entry.getValue() != null && ((entry.getValue() instanceof Collection))) {
                        sqlQuery.setParameterList(entry.getKey(), (Collection) entry.getValue());
                    } else {
                        sqlQuery.setParameter(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        return sqlQuery;
    }
}