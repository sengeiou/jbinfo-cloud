package cn.jbinfo.cloud.core.copier;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by xiaobin on 2016/11/1.
 */
public class BeanTemplate {

    public static <D, V> List<V> execute(List<D> list, Class<V> toClass) {
        List<V> result = Lists.newArrayList();
        ResultExtractor<D, V> rse = new CopierExtractor<>();
        for (D entity : list) {
            result.add(rse.extractData(entity, toClass));
        }
        return result;
    }

    public static <D, V> V executeBean(D entity, Class<V> toClass) {
        ResultExtractor<D, V> rse = new CopierExtractor<>();
        return rse.extractData(entity, toClass);
    }
}
