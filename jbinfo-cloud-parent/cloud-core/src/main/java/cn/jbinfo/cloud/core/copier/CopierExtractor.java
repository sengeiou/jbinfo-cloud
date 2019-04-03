package cn.jbinfo.cloud.core.copier;


/**
 * 默认转化器
 * Created by xiaobin on 2016/11/2.
 */
public class CopierExtractor<D, V> implements ResultExtractor<D, V> {

    @Override
    public V extractData(D entity, Class<V> toClass) {
        Class<D> formClass = (Class<D>) entity.getClass();
        BeanCopier<D, V> copier = BeanCopiers.copier(formClass, toClass);
        return copier.copy(entity);
    }
}
