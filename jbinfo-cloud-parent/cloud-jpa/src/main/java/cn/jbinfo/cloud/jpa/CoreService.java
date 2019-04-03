package cn.jbinfo.cloud.jpa;

import cn.jbinfo.cloud.core.model.BaseModel;
import cn.jbinfo.cloud.core.utils.MyBeanUtils;
import cn.jbinfo.cloud.jpa.data.DataTableParameter;
import cn.jbinfo.cloud.jpa.data.Finder;
import cn.jbinfo.cloud.jpa.data.PageTable;
import cn.jbinfo.cloud.jpa.dynamic.Parameter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author xiaobin
 * @create 2017-10-09 下午3:07
 **/
public abstract class CoreService<DAO extends GenericJpaRepository<MODEL, String>, MODEL extends BaseModel> {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    protected DAO dao;

    private Class<MODEL> modelClass;

    public CoreService() {
        modelClass = (Class<MODEL>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    public MODEL get(String id) {
        return dao.findById(id).orElse(null);
    }

    @Transactional
    public MODEL save(MODEL model) {
        return dao.save(model);
    }

    public MODEL saveOrUpdate(MODEL model) {
        if (StringUtils.isBlank(model.getId())) {
            return save(model);
        }
        MODEL bean = dao.findById(model.getId()).orElse(null);
        if (bean == null) {
            return save(model);
        }
        MyBeanUtils.to(model, bean);
        return dao.save(bean);
    }

    /**
     * 选择性更新
     *
     * @param model
     * @param fields
     * @return
     */
    public MODEL saveOrUpdate(MODEL model, String... fields) {
        if (StringUtils.isBlank(model.getId()))
            return save(model);
        MODEL bean = dao.findById(model.getId()).orElse(null);
        if (bean == null)
            return save(model);
        MyBeanUtils.to(model, bean, fields);
        return dao.save(bean);
    }

    @Transactional
    public void deleteById(String id) {
        dao.updateDelFlag(id);
    }

    @Transactional
    public void deleteByIds(String[] ids) {
        Arrays.stream(ids).forEach(this::deleteById);
    }

    public PageTable<MODEL> findPage(Map<String, Object> map, DataTableParameter parameter) {
        PageRequest pageRequest = parameter.getPageRequest();
        String bean = modelClass.getSimpleName();
        Finder finder = Finder.create("select bean from " + bean + " bean where bean.delFlag=:delFlag")
                .setParam("delFlag", BaseModel.DEL_FLAG_NORMAL);
        if (map != null)
            map.forEach(finder::search);
        Page<MODEL> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }

    public PageTable<MODEL> findPageByFinder(Finder finder, DataTableParameter parameter) {
        PageRequest pageRequest = parameter.getPageRequest();
        Page<MODEL> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }

    @Transactional
    public int updateByUpdate(Parameter parameter, Parameter where) {
        return dao.updateByUpdate(parameter, where);
    }

    public List<MODEL> findAll(Specification<MODEL> specification, Sort... sorts) {
        if (sorts != null && sorts.length > 0)
            return dao.findAll(specification, sorts[0]);
        return dao.findAll(specification);
    }

    public List<MODEL> findAll(Sort... sorts) {
        if (sorts != null && sorts.length > 0)
            return dao.findAll(sorts[0]);
        return dao.findAll();
    }

    /**
     * 動態sql分頁查詢
     *
     * @param parameter
     * @param p
     * @param clazzs
     * @param <X>
     * @return
     */
    public <X> PageTable<X> findPageByNamedQuery(Parameter parameter, Pageable p, Class<X> clazzs) {
        Page<X> page = dao.findPageByNamedQuery(parameter, p, clazzs);
        return new PageTable<>(page);
    }

    /**
     * 動態sql更新
     *
     * @param parameter
     * @return
     */
    public int updateByNamedQuery(Parameter parameter) {
        return dao.updateByNamedQuery(parameter);
    }

}
