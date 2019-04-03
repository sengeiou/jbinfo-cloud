package cn.jbinfo.cloud.mongodb;

import cn.jbinfo.cloud.core.condition.Pagination;
import cn.jbinfo.cloud.core.model.BaseMongoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

/**
 * <code>MongoBaseDao.java</code>
 * <p>功能: mongodb 基础dao
 *
 * @author xiaobin
 */
public abstract class BaseMongoDao<T extends BaseMongoModel> {

    @Autowired
    @Qualifier("mongoTemplate")
    protected MongoOperations mongoOperations;

    private Class<T> modelClass;

    /**
     * 功能:得到实体泛型class
     *
     * @return
     */
    @SuppressWarnings({"unchecked"})
    private Class<T> getModelClass() {
        if (this.modelClass == null) {
            Type type = getClass().getGenericSuperclass();
            Type trueType = ((ParameterizedType) type).getActualTypeArguments()[0];
            this.modelClass = (Class<T>) trueType;
        }
        return this.modelClass;
    }

    /**
     * 功能:保存一个对象，如果id存在则修改
     */
    public void save(T t) {
        mongoOperations.save(t);
    }

    /**
     * 功能:直接插入单个对象
     */
    public void add(T t) {
        mongoOperations.insert(t);
    }

    /**
     * 功能: 插入多个文档对象
     *
     * @param list
     */
    public void add(Collection<T> list) {
        mongoOperations.insert(list, getModelClass());
    }

    /**
     * 功能:删除一个对象
     */
    public void deleteById(String id) {
        mongoOperations.remove(query(where("id").is(id)), getModelClass());
    }

    /**
     * 功能:删除一个对象
     */
    public void delete(T t) {
        mongoOperations.remove(t);
    }

    /**
     * 功能:删除一个对象
     */
    public void delete(Query query) {
        mongoOperations.remove(query, getModelClass());
    }


    /**
     * 功能:根据id查询一个对象
     */
    public T findById(String id) {
        return mongoOperations.findById(id, getModelClass());
    }

    /**
     * 功能:查询集合中所有数据
     *
     * @return
     */
    public List<T> findAll() {
        return mongoOperations.findAll(getModelClass());
    }

    /**
     * 功能:查询一组ids
     *
     * @return
     */
    public List<T> findByIds(List<String> ids) {
        return mongoOperations.find(query(where("id").in(ids)), getModelClass());
    }

    /**
     * 功能:根据某个条件查询
     *
     * @return
     */
    public List<T> findByQuery(Query query) {
        return mongoOperations.find(query, getModelClass());
    }

    /**
     * 功能:根据某个条件分页查询
     *
     * @return
     */
    public List<T> findByQuery(Pagination page, Query query) {
        return findByQuery(page, query, new Sort(Direction.ASC, "id"));
    }

    /**
     * 功能:根据某个条件分页查询
     *
     * @return
     */
    public List<T> findByQuery(Pagination page, Query query, Sort sort) {
        Assert.notNull(page, "findByQuery page is  must not be null");
        Assert.notNull(query, "findByQuery query is  must not be null");
        //查询总条数
        long totalCount = mongoOperations.count(query, getModelClass());
        page.init(totalCount, page.getPageSize());
        if (sort == null) {
            query.with(new Sort(Direction.ASC, "id"));
        } else {
            query.with(sort);
        }
        query.skip(page.getFirstResult()).limit(page.getPageSize());
        return mongoOperations.find(query, getModelClass());
    }

    public List<T> findByPage(Pagination page) {
        return findByQuery(page, new Query());
    }


    /**
     * 功能:根据id更新一个文档的key
     */
    public void updateKey(String id, String key, Object value) {
        mongoOperations.updateFirst(query(where("id").is(id)), update(key, value), getModelClass());
    }

    /**
     * 功能: 返回具体条数
     *
     * @param query
     * @return
     */
    public long findCount(Query query) {
        return mongoOperations.count(query, getModelClass());
    }
}
