package cn.jbinfo.cloud.mongodb;

import cn.jbinfo.cloud.core.condition.Pagination;
import cn.jbinfo.cloud.core.model.BaseMongoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <code>BaseMongoBiz.java</code>
 * <p>功能:mongo 业务基础类
 *
 * @author xiaobin
 */
public abstract class BaseMongoBiz<T extends BaseMongoModel, K extends BaseMongoDao<T>> {

    /**
     * dao原型属性
     */
    protected K baseDao;

    /**
     * 根据K泛型自动装载BaseDao
     *
     * @param baseDao
     */
    @Autowired
    protected final void setBaseDao(K baseDao) {
        this.baseDao = baseDao;
    }

    public void save(T entity) {
        baseDao.save(entity);
    }

    public void add(T entity) {
        baseDao.add(entity);
    }

    public void add(Collection<T> entitys) {
        baseDao.add(entitys);
    }

    public void deleteById(String id) {
        baseDao.deleteById(id);
    }

    public void delete(T entity) {
        if (entity != null)
            baseDao.deleteById(entity.getId());
    }

    public T findById(String id) {
        return baseDao.findById(id);
    }

    /**
     * 获取批量数据
     * <p>
     * 该方法只是简单的循环调用findById方法实现，如果需要提高效率，请自行实现baoseDao find
     * </p>
     *
     * @param ids
     * @return
     */
    public List<T> findByIds(List<String> ids) {
        List<T> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(ids)) {
            for (String id : ids) {
                T obj = findById(id);
                if (obj != null) {
                    list.add(obj);
                }
            }
        }
        return list;
    }

    /**
     * 功能: 分页查询
     *
     * @param page
     * @return
     */
    public List<T> findByPage(Pagination page) {
        return baseDao.findByPage(page);
    }

    /**
     * 功能:根据条件分页查询
     *
     * @param page
     * @param query
     * @return
     */
    public List<T> findByQuery(Pagination page, Query query) {
        return baseDao.findByQuery(page, query);
    }

    /**
     * 功能:更新指定id对应key的值
     *
     * @param id
     * @param key
     * @param value
     */
    public void updateKey(String id, String key, String value) {
        Assert.hasLength(id, "id不能为空");
        Assert.hasLength(key, "key不能为空");
        Assert.hasLength(value, "value不能为空");
        baseDao.updateKey(id, key, value);
    }
}
