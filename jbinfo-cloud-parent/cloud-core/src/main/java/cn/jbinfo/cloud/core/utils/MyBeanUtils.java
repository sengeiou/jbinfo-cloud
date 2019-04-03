package cn.jbinfo.cloud.core.utils;import com.esotericsoftware.reflectasm.MethodAccess;import jodd.bean.BeanTool;import org.apache.commons.beanutils.BeanUtilsBean;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.util.Assert;import java.beans.BeanInfo;import java.beans.Introspector;import java.beans.PropertyDescriptor;import java.lang.reflect.Field;import java.lang.reflect.InvocationTargetException;import java.lang.reflect.Method;import java.lang.reflect.Modifier;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import java.util.Map;import static jodd.bean.BeanUtil.*;/** * 对象操作辅助类 */public class MyBeanUtils {	/*     * private static final Logger log = LoggerFactory	 * .getLogger(MyBeanUtils.class);	 */    private static final Logger LOGGER = LoggerFactory.getLogger(MyBeanUtils.class);    /**     * 实例化对象     *     * @param clazz 类     * @return 对象     */    @SuppressWarnings("unchecked")    public static <T> T newInstance(Class<?> clazz) {        try {            return (T) clazz.newInstance();        } catch (InstantiationException | IllegalAccessException e) {            throw new RuntimeException(e);        }    }    /**     * 实例化对象     *     * @param clazzStr 类名     * @return {T}     */    public static <T> T newInstance(String clazzStr) {        try {            Class<?> clazz = Class.forName(clazzStr);            return newInstance(clazz);        } catch (ClassNotFoundException e) {            throw new RuntimeException(e);        }    }    /**     * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.     */    @SuppressWarnings("unchecked")    public static <T> T getFieldValue(final Object object,                                      final String fieldName) {        if (hasDeclaredProperty(object, fieldName)) {            return (T) getDeclaredProperty(object, fieldName);        }        return null;    }    @SuppressWarnings("unchecked")    public static <T> T getGetFieldValue(final Object object,                                         final String fieldName) {        return (T) getProperty(object, fieldName);    }    public static Object getGetFieldObjValue(final Object object,                                             final String fieldName) {        return getProperty(object, fieldName);    }    @SuppressWarnings("unchecked")    public static Map<String, Object> describe(Object bean) {        if (bean instanceof Map) {            return (Map<String, Object>) bean;        }        try {            return BeanUtilsBean.getInstance().getPropertyUtils().describe(bean);        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {            throw new RuntimeException("never happend exception!", e);        }    }    public static Map<String, Object> describe(Object bean, String... fileds) {        try {            Map<String, Object> map = new HashMap<>();            for (String field : fileds) {                try {                    Object value = getSimpleProperty(bean, field);                    map.put(field, value);                } catch (Exception e) {                    throw new RuntimeException(e);                }            }            return map;        } catch (RuntimeException ex) {            throw new RuntimeException(ex);        }    }    /**     * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.     */    public static void setFieldValue(final Object object,                                     final String fieldName, final Object value) {        if (!hasDeclaredProperty(object, fieldName)) {            return;        }        Field field = getDeclaredField(object, fieldName);        if (field == null) {            return;        }        makeAccessible(field);        try {            field.set(object, value);        } catch (IllegalAccessException e) {            throw new RuntimeException("never happend exception!", e);        }    }    /**     * 循环向上转型,获取对象的DeclaredField.     */    protected static Field getDeclaredField(final Object object,                                            final String fieldName) {        Assert.notNull(object);        return getDeclaredField(object.getClass(), fieldName);    }    /**     * 循环向上转型,获取类的DeclaredField.     */    @SuppressWarnings("rawtypes")    protected static Field getDeclaredField(final Class clazz,                                            final String fieldName) {        Assert.notNull(clazz);        Assert.hasText(fieldName);        for (Class superClass = clazz; superClass != Object.class; superClass = superClass                .getSuperclass()) {            try {                return superClass.getDeclaredField(fieldName);            } catch (NoSuchFieldException e) {                // Field不在当前类定义,继续向上转型            }        }        return null;    }    public static List<Field> getDeclaredFields(final Class clazz) {        Assert.notNull(clazz);        List<Field> fieldList = new ArrayList<>();        for (Class superClass = clazz; superClass != Object.class; superClass = superClass                .getSuperclass()) {            Field[] fields = superClass.getDeclaredFields();            for (Field field : fields) {                fieldList.add(field);            }        }        return fieldList;    }    /**     * 强制转换fileld可访问.     */    protected static void makeAccessible(final Field field) {        if (!Modifier.isPublic(field.getModifiers())                || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {            field.setAccessible(true);        }    }    public static Object getSimpleProperty(Object bean, String propName)            throws IllegalArgumentException, SecurityException,            IllegalAccessException, InvocationTargetException,            NoSuchMethodException {        return getProperty(bean, propName);    }    public static List<Object> getPropertyByList(List<?> list, String propName)            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {        List<Object> returnV = new ArrayList<>();        for (Object o : list) {            Object v = getSimpleProperty(o, propName);            returnV.add(v);        }        return returnV;    }    /**     * 实例化并复制属性     */    public static void to(Object orig, Object dest) {        Map<String, Object> map = describe(orig);        for (String key : map.keySet()) {            if (map.get(key) != null) {                try {                    setFieldValue(dest, key, map.get(key));                } catch (Exception e) {                    LOGGER.error("error:", e);                }            }        }    }    public static void to(Object orig, Object dest, String... fields) {        Map<String, Object> map = describe(orig, fields);        for (String key : map.keySet()) {            if (map.get(key) != null) {                try {                    setFieldValue(dest, key, map.get(key));                } catch (Exception e) {                    LOGGER.error("error:", e);                }            }        }    }    public static void copyProperties(Object orig, Object dest) {        BeanTool.copy(orig, dest);    }    public static Map<String, MethodAccess> methodMap = new HashMap<>();    /**     * 执行某对象方法     *     * @param owner      对象     * @param methodName 方法名     * @param args       参数     * @return 方法返回值     */    public static final Object invokeMethod(Object owner, String methodName, Object... args) {        Class<?> ownerClass = owner.getClass();        String key = null;        if (args != null) {            Class<?>[] argsClass = new Class[args.length];            for (int i = 0, j = args.length; i < j; i++) {                if (args[i] != null) {                    argsClass[i] = args[i].getClass();                }            }            key = ownerClass + "_" + methodName + "_" + org.apache.commons.lang3.StringUtils.join(argsClass, ","); // 用于区分重载的方法        } else {            key = ownerClass + "_" + methodName; // 用于区分重载的方法        }        // 缓存Method对象        MethodAccess methodAccess = methodMap.computeIfAbsent(key, k -> MethodAccess.get(ownerClass));        return methodAccess.invoke(owner, methodName, args);    }    // Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map    public static Map<String, Object> transBean2Map(Object obj) {        Map<String, Object> map = new HashMap<>();        if (obj == null) {            return map;        }        try {            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();            for (PropertyDescriptor property : propertyDescriptors) {                String key = property.getName();                // 过滤class属性                if (!key.equals("class")) {                    // 得到property对应的getter方法                    Method getter = property.getReadMethod();                    Object value = getter.invoke(obj);                    map.put(key, value);                }            }        } catch (Exception e) {            System.out.println("transBean2Map Error " + e);        }        return map;    }    /**     * @param oldBean     * @param newBean     * @return     */    public static <T> T getDiff(T oldBean, T newBean) {        if (oldBean == null && newBean != null) {            return newBean;        } else if (newBean == null) {            return null;        } else {            Class<?> cls1 = oldBean.getClass();            try {                @SuppressWarnings("unchecked")                T object = (T) cls1.newInstance();                BeanInfo beanInfo = Introspector.getBeanInfo(cls1);                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();                for (PropertyDescriptor property : propertyDescriptors) {                    String key = property.getName();                    // 过滤class属性                    if (!key.equals("class")) {                        // 得到property对应的getter方法                        Method getter = property.getReadMethod();                        // 得到property对应的setter方法                        Method setter = property.getWriteMethod();                        Object oldValue = getter.invoke(oldBean);                        Object newValue = getter.invoke(newBean);                        if (newValue != null) {                            if (oldValue == null) {                                setter.invoke(object, newValue);                            } else if (!newValue.equals(oldValue)) {                                setter.invoke(object, newValue);                            }                        }                    }                }                return object;            } catch (Exception e) {                throw new RuntimeException(e);            }        }    }}