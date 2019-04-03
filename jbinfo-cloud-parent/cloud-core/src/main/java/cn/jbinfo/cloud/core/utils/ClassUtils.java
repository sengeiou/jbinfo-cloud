package cn.jbinfo.cloud.core.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * <code>ClassUtils.java</code>
 * <p>功能:class 工具类
 * </br>最后修改人 无
 */
@SuppressWarnings("unchecked")
public class ClassUtils {

    private static final String METHOD_GET = "get";

    private static final String METHOD_SET = "set";


    /**
     * 功能:首字母小写
     *
     * @param className
     * @return
     */
    public static String getLowerFirstLetterSimpleClassName(String className) {
        if (StringUtils.isEmpty(className)) return "";
        String[] parts = className.split("\\.");
        String result = parts[parts.length - 1];
        return result.substring(0, 1).toLowerCase() + (result.length() > 1 ? result.substring(1) : "");
    }


    /**
     * 功能:根据类全限定名称获取simpleName
     *
     * @param className
     * @return
     */
    public static String getSimpleClassName(String className) {
        if (StringUtils.isEmpty(className)) return "";
        String[] parts = className.split("\\.");
        return parts[parts.length - 1];
    }

    /**
     * 功能: 1.获取属性名称的简单名，如果属性名称是entity.property，那么将返回property
     * 2.如何输入的名称有 [ 标识 说明是查询字段，原样返回即可 ，不做任何处理
     *
     * @param fullName
     * @return
     */
    public static String getSimplePropertyName(String fullName) {
        if (!StringUtils.isEmpty(fullName) && fullName.contains("[")) {
            //如果字段含有 [ 说明是查询字段，直接返回
            return fullName;
        }
        if (!StringUtils.isEmpty(fullName) && fullName.contains(".")) {//检查要获取的属性名是否含有.
            //检查.是否是最后一个字符
            if (fullName.indexOf(".") != fullName.length()) {
                fullName = fullName.substring(fullName.indexOf(".") + 1);
            }
        }
        return fullName;
    }

    /**
     * 获取 get 方法对应的属性名
     *
     * @param readMethod
     * @return
     */
    public static String getPropertyName(Method readMethod) {
        String methodName = readMethod.getName();
        int getPosition = methodName.indexOf("get");
        if (getPosition == -1) {
            throw new RuntimeException(methodName + " 不是 以get开关的方法");
        }
        return getLowerFirstLetterSimpleClassName(methodName.substring(getPosition + 3));
    }

    /**
     * 根据字符串转化成ID
     * 1.如果属性名称是entity.property，那么将返回property
     * 2.如何输入的名称有 [ 标识 说明是查询字段   如 ：conditions['date_date_gt'].value  用  split("'") 截断 返回第二个
     *
     * @param fullName 属性名称
     * @return 处理后的属性名称
     */
    public static String changeToId(String fullName) {

        if (!StringUtils.isEmpty(fullName) && fullName.contains("[")) {
            //如果字段含有 [ 说明是查询字段，直接返回
            String[] ids = fullName.split("'");
            if (ids.length == 3)
                return ids[1];
            return fullName;
        }

        if (!StringUtils.isEmpty(fullName) && fullName.contains(".")) {//检查要获取的属性名是否含有.
            //检查.是否是最后一个字符
            if (fullName.indexOf(".") != fullName.length()) {
                fullName = fullName.substring(fullName.indexOf(".") + 1);
            }
        }

        return fullName;
    }

    /**
     * 克隆一个对象
     * <p>
     * 该对象必须实现了Serializable接口,<br/>
     * 使用序列化和反序列化形式进行克隆
     * </p>
     *
     * @param entity 要克隆的对象
     * @return 克隆结果
     */
    public static <T> T cloneObject(T entity) {
        T result = null;
        if (entity == null) {
            return null;
        }
        //检查克隆实体是否实现了Serializable接口
        if (!Serializable.class.isAssignableFrom(entity.getClass())) {
            throw new IllegalArgumentException("需要克隆的实体必须实现Serializable接口");
        }
        ByteArrayOutputStream t = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        ByteArrayInputStream t2 = null;
        try {
            t = new ByteArrayOutputStream();
            out = new ObjectOutputStream(t);
            out.writeObject(entity);
            out.flush();
            t2 = new ByteArrayInputStream(t.toByteArray());
            in = new ObjectInputStream(t2);
            result = (T) in.readObject();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
            if (t2 != null) {
                try {
                    t2.close();
                } catch (IOException ignored) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignored) {
                }
            }
            if (t != null) {
                try {
                    t.close();
                } catch (IOException ignored) {
                }
            }
        }
        return result;
    }

    /**
     * 获取某超类的子类列表
     *
     * @param superClass                                                         超类
     * @param packageName，如enums，最终会扫描符合cn/jbinfo\/**\/packageName/**\\/*.class的类
     * @return 子类列表
     */
    public static <T> List<Class<T>> getClassBySupperClass(Class<T> superClass, String packageName) {
        List<Class<T>> classList = new ArrayList<>();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(Thread.currentThread().getContextClassLoader());
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resolver);
        try {
            // 如com/kipo/**/model/**/*.class
            Resource[] rs = resolver.getResources("classpath*:cn/jbinfo/**/" + packageName + "/**/*.class");
            for (Resource resource : rs) {
                if (resource.getURL() != null && resource.getURL().getPath().contains("com/kipo/ppp"))
                    continue;
                MetadataReader mr = metadataReaderFactory.getMetadataReader(resource);
                //忽略所有加了NotScanInit注解类
                if (mr.getAnnotationMetadata().isAnnotated("com.kipo.core.annotation.NotScanInit")) {
                    continue;
                }
                String className = metadataReaderFactory.getMetadataReader(resource).getClassMetadata().getClassName();
                if (className.endsWith("Utils") || className.endsWith("Util")) {
                    continue;
                }
                Class<T> clazz;
                try {
                    clazz = (Class<T>) Class.forName(className);
                    //判断当前cl是否是 superclass的子类
                    if (superClass.isAssignableFrom(clazz) && clazz != superClass) {
                        classList.add(clazz);
                    }
                } catch (Throwable e) {
                    throw new IllegalArgumentException("加载类(" + className + ")失败", e);
                }

            }
        } catch (IOException e) {
            throw new IllegalArgumentException("加载类失败", e);
        }
        return classList;
    }

    /**
     * 功能:根据类的全限定名，获取类对象
     *
     * @param <T>
     * @param className
     * @return 返回类的Class对象
     */
    public static <T> Class<T> getClassBySimpleName(String className) {
        try {
            return (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }


    /**
     * 功能: 根据构造函数的参数，实例化一个对象
     *
     * @param <T>
     * @param clazz
     * @param params
     * @return
     */
    public static <T> T newInstance(Class<T> clazz, Object... params) {
        //如果构造函数的参数存在
        if (params != null) {
            Class<?>[] cs = new Class<?>[params.length];
            for (int i = 0; i < params.length; i++) {
                cs[i] = params[i].getClass();
                //如果是代理对象，则获取被代理的接口类
                if (Proxy.isProxyClass(cs[i])) {
                    cs[i] = cs[i].getInterfaces()[0];
                }
            }
            try {
                return clazz.getConstructor(cs).newInstance(params);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
        //默认构造方法
        else {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    /**
     * 功能:获取类方法对象
     *
     * @param clazz
     * @param method
     * @return
     */
    public static Method getMethod(Class<?> clazz, String method) {
        if (clazz == null || StringUtils.isEmpty(method)) {
            return null;
        }
        Method[] list = clazz.getMethods();
        Method rs = null;
        if (list != null) {
            for (Method m : list) {
                if (m.getName().equals(method)) {
                    rs = m;
                    break;
                }
            }
        }
        return rs;
    }

    /**
     * 功能:根据属性名称获取get方法
     *
     * @param clazz
     * @param propertyName
     * @return
     */
    public static Method getGetMethodByPropertyName(Class<?> clazz, String propertyName) {
        if (clazz == null || StringUtils.isEmpty(propertyName)) {
            return null;
        }
        propertyName = METHOD_GET + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        Method[] list = clazz.getMethods();
        Method rs = null;
        if (list != null) {
            for (Method m : list) {
                if (m.getName().equals(propertyName) && (m.getParameterTypes() == null || m.getParameterTypes().length == 0)) {
                    rs = m;
                    break;
                }
            }
        }
        return rs;
    }

    /**
     * 功能:根据属性名称获取get方法，jdk自带过滤方式
     *
     * @param clazz
     * @param propertyName
     * @return
     */
    public static Method getGetMethodByPropertyNameJdk(Class<?> clazz, String propertyName) {
        if (clazz == null || StringUtils.isEmpty(propertyName)) {
            return null;
        }
        String methodName = METHOD_GET + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        Method m = null;
        try {
            m = clazz.getMethod(methodName);
        } catch (NoSuchMethodException | SecurityException e) {
            return null;
        }
        return m;
    }


    /**
     * 功能: 根据属性名称获取set方法名
     *
     * @param clazz
     * @param propertyName
     * @return
     */
    public static Method getSetMethodByPropertyName(Class<?> clazz, String propertyName) {
        if (clazz == null || StringUtils.isEmpty(propertyName)) {
            return null;
        }
        propertyName = METHOD_SET + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        Method[] list = clazz.getMethods();
        Method rs = null;
        if (list != null) {
            for (Method m : list) {
                if (m.getName().equals(propertyName) && m.getParameterTypes() != null && m.getParameterTypes().length == 1) {
                    rs = m;
                    break;
                }
            }
        }
        return rs;
    }

    /**
     * 功能:根据属性名称获取其值
     *
     * @param target
     * @param propertyName
     * @return
     */
    public static Object getValueByPropertyName(Object target, String propertyName) {
        if (target == null || StringUtils.isEmpty(propertyName)) {
            return null;
        }
        Method method = getGetMethodByPropertyName(target.getClass(), propertyName);
        if (method == null) {
            return null;
        }
        try {
            return method.invoke(target);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }


    /**
     * 功能:根据属性名称，设置属性值
     *
     * @param value
     * @param propertyName
     * @param target
     */
    public static void setValueByPropertyName(Object value, String propertyName, Object target) {
        if (target == null || StringUtils.isEmpty(propertyName)) {
            return;
        }
        Method method = getSetMethodByPropertyName(target.getClass(), propertyName);
        if (method != null) {
            try {
                method.invoke(target, value);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
    }


    /**
     * 功能:获取泛型类型，如果有多个则递归获取*
     * @param <T>
     * @param clazz
     * @return
     */
    public static <T> Class<T> getGenericsClass(Class<?> clazz) {
        Type superType = clazz.getGenericSuperclass();
        //如果没有泛型
        if (superType == null || !ParameterizedType.class.isAssignableFrom(superType.getClass())) {
            return (Class<T>) clazz;
        }
        return recursionGenerics((ParameterizedType) superType);
    }

    /**
     * 功能:递归处理参数
     *
     * @param <T>
     * @param type
     * @return
     */
    private static <T> Class<T> recursionGenerics(ParameterizedType type) {
        Type returnType = type.getActualTypeArguments()[0];
        if (!ParameterizedType.class.isAssignableFrom(returnType.getClass())) {
            Class<T> rsClass = (Class<T>) type.getRawType();
            //如果不是基本类型，则向下获取
            if (!isJDKBaseCalss((Class<T>) returnType)) {
                rsClass = (Class<T>) returnType;
            }
            return rsClass;
        }
        return recursionGenerics((ParameterizedType) returnType);
    }

    /**
     * 功能:是否基本JDK 基本类型
     * class.isPrimitive() || isString
     * boolean、byte、char、short、int、long、float 和 double以及String
     *
     * @param clazz
     * @return
     */
    public static boolean isJDKBaseCalss(Class<?> clazz) {
        return clazz == null || clazz.isPrimitive() || clazz == String.class;
    }


}
