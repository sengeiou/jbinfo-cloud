package cn.jbinfo.cloud.core.copier;

/**
 * Created by xiaobin on 2016/11/1.
 */
public interface ResultExtractor<D, V> {

    V extractData(D entity, Class<V> toClass);
}
