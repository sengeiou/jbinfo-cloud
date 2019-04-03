package cn.jbinfo.cloud.core.copier;

import cn.jbinfo.cloud.core.copier.core.BeanCopierUtils;
import com.google.common.base.Function;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaobin on 2016/11/2.
 */
public class BeanCopier<F, T> implements Function<F, T> {

    private Class<F> clazzFrom;

    private Class<T> clazzTo;

    private List<Pair<Field, Field>> fieldsToCopy;

    private boolean ignoreCase;

    public BeanCopier(Class<F> clazzFrom, Class<T> clazzTo) {
        this(clazzFrom, clazzTo, false);
    }

    public BeanCopier(Class<F> clazzFrom, Class<T> clazzTo, boolean ignoreCase) {
        this.clazzFrom = clazzFrom;
        this.clazzTo = clazzTo;
        this.ignoreCase = ignoreCase;
        compile();
    }

    private void compile() {
        //源
        List<Field> declaredFieldsFrom = BeanCopierUtils.getDeclaredFields(clazzFrom, false);
        //过滤掉无须转化的
        List<Field> declaredFieldsTo = BeanCopierUtils.getDeclaredFields(clazzTo, true);
        fieldsToCopy = new ArrayList<>(Math.min(declaredFieldsFrom.size(), declaredFieldsTo.size()));
        Map<String, MetaField> declaredFieldsMapTo = BeanCopierUtils.getMetaFields(clazzTo, ignoreCase);
        String fieldName;
        for (Field field : declaredFieldsFrom) {
            if (field.getName().equals("serialVersionUID"))
                continue;
            fieldName = ignoreCase ? field.getName().toLowerCase() : field.getName();
            if (declaredFieldsMapTo.containsKey(fieldName)) {
                MetaField fieldTo = declaredFieldsMapTo.get(fieldName);
                if (typeCompatible(field, fieldTo.getField())) {
                    field.setAccessible(true);
                    fieldTo.getField().setAccessible(true);
                    fieldsToCopy.add(new Pair<>(field, fieldTo.getField()));
                }
            }
        }

    }

    private boolean typeCompatible(Field field, Field fieldTo) {
        return field.getType().equals(fieldTo.getType()) || field.getType().isAssignableFrom(fieldTo.getType());
    }

    public T copy(F f, T t) {
        for (Pair<Field, Field> fieldFieldPair : fieldsToCopy) {
            try {
                fieldFieldPair.getB().set(t, fieldFieldPair.getA().get(f));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return t;
    }

    public T copy(F f) {
        try {
            return copy(f, clazzTo.newInstance());
        } catch (Exception e) {
            throw new RuntimeException("init class " + f.getClass() + "error", e);
        }
    }

    @Override
    public T apply(F input) {
        return copy(input);
    }
}
