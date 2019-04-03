package cn.jbinfo.cloud.core.copier;

import java.lang.reflect.Field;

/**
 * Created by xiaobin on 2016/11/2.
 */
public class MetaField {

    private String fieldName;

    private Field field;

    public MetaField() {
    }

    public MetaField(String fieldName, Field field) {
        this.fieldName = fieldName;
        this.field = field;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
