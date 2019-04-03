package cn.jbinfo.cloud.jpa;

import cn.jbinfo.cloud.jpa.data.Finder;
import cn.jbinfo.cloud.jpa.dynamic.DynamicBaseDaoImpl;
import cn.jbinfo.cloud.jpa.dynamic.Parameter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/8/7
 */
public class GenericJpaRepositoryImpl<T, ID>
        extends DynamicBaseDaoImpl<T, ID> implements GenericJpaRepository<T, ID>, Serializable {

    private EntityManager em;

    private Class<T> eif;

    public GenericJpaRepositoryImpl(Class<T> eif, EntityManager em) {
        super(eif, em);
        this.em = em;
        this.eif = eif;
    }


    @Override
    public int updateDelFlag(Serializable id, String delFlag) {
        String hql = "update " + eif.getSimpleName() + " set delFlag=:delFlag,updateDate=:updateDate where id =:id";
        Query query = em.createQuery(hql);
        query.setParameter("delFlag", delFlag);
        query.setParameter("updateDate", new Date());
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public int updateByUpdate(Parameter parameter, Parameter where) {
        StringBuilder updateHql = new StringBuilder("update " + eif.getSimpleName());
        updateHql.append(" set ");
        parameter.getMap().forEach((key, value) -> updateHql.append(" ").append(key).append("=:").append(key).append(","));
        StringBuilder sql = new StringBuilder(StringUtils.removeEnd(updateHql.toString(),","));
        StringBuilder whereHql = new StringBuilder();
        whereHql.append(" where ");
        where.getMap().forEach((key, value) -> whereHql.append(" ").append(key).append("=:").append(key).append(","));
        sql.append(StringUtils.removeEnd(whereHql.toString(),","));
        Query query = em.createQuery(sql.toString());
        parameter.getMap().forEach(query::setParameter);
        where.getMap().forEach(query::setParameter);
        return query.executeUpdate();
    }

    @Override
    public int updateDelFlag(Serializable id) {
        return updateDelFlag(id, "1");
    }

    public Page<T> find(Finder finder, Pageable p) {
        int totalCount = countQueryResult(finder);
        PageImpl<T> page;
        if (totalCount < 1) {
            page = new PageImpl<>(Lists.newArrayList(), p, 0);
            return page;
        }
        Query q = em.createQuery(finder.getOrigHql());
        org.hibernate.query.Query query = q.unwrap(org.hibernate.query.Query.class);
        finder.setParamsToQuery(query);
        query.setFirstResult(Long.valueOf(p.getOffset()).intValue());
        query.setMaxResults(p.getPageSize());
        List list = query.getResultList();
        page = new PageImpl<>(list, p, totalCount);
        return page;
    }

    public List<T> find(Finder finder) {
        Query query = finder.createQuery(em);
        return query.getResultList();
    }



}
