package cn.jbinfo.cloud.core.copier.core;

import cn.jbinfo.cloud.core.copier.Exclude;
import cn.jbinfo.cloud.core.copier.Mapping;
import cn.jbinfo.cloud.core.copier.MetaField;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaobin on 2016/11/2.
 */
public class BeanCopierUtils {

    public static List<Field> getDeclaredFields(final Class clazz, boolean excule) {
        Assert.notNull(clazz);
        List<Field> fieldList = Lists.newArrayList();
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            Field[] fields = superClass.getDeclaredFields();
            for (Field field : fields) {
                Exclude exclude = field.getAnnotation(Exclude.class);
                if (exclude != null) {
                    continue;
                }
                fieldList.add(field);
            }
        }
        return fieldList;
    }


    public static Map<String, MetaField> getMetaFields(final Class clazz, boolean ignoreCase) {
        Assert.notNull(clazz);
        //meta定义、fieldname、Fileld本身
        Map<String, MetaField> map = Maps.newConcurrentMap();
        List<Field> fieldList = Lists.newArrayList();
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            Field[] fields = superClass.getDeclaredFields();
            MetaField metaField;
            for (Field field : fields) {
                Exclude exclude = field.getAnnotation(Exclude.class);
                if (exclude != null) {
                    continue;
                }
                Mapping mapping = field.getAnnotation(Mapping.class);
                metaField = new MetaField(field.getName(), field);
                if (mapping != null) {
                    map.put(ignoreCase ? mapping.value().toLowerCase() : mapping.value(), metaField);
                } else {
                    map.put(ignoreCase ? field.getName().toLowerCase() : field.getName(), metaField);
                }
                fieldList.add(field);
            }
        }
        return map;
    }
}
