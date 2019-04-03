package cn.jbinfo.cloud.jpa;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;

import javax.persistence.Parameter;
import javax.persistence.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/8/11.
 */
public class QueryBuilder {

    private static final Pattern ORDERBY_PATTERN_1 = Pattern
            .compile("order\\s+by.+?$", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

    //TODO cache transformers
    /*public static <C> TypedQuery transform(TypedQuery query, Class<C> clazz) {
        if (Map.class.isAssignableFrom(clazz)) {
            return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        } else if (Number.class.isAssignableFrom(clazz) || clazz.isPrimitive() || String.class.isAssignableFrom(clazz) ||
                Date.class.isAssignableFrom(clazz)) {
            return query.setResultTransformer(new SmartTransformer(clazz));
        } else {
            return query.setResultTransformer(new BeanTransformerAdapter<C>(clazz));
        }
    }*/

    private static String wrapCountQuery(String query) {
        return "select count(*) from (" + query + ") as ctmp";
    }

    private static String cleanOrderBy(String query) {
        Matcher matcher = ORDERBY_PATTERN_1.matcher(query);
        StringBuffer sb = new StringBuffer();
        int i = 0;
        while (matcher.find()) {
            String part = matcher.group(i);
            if (canClean(part)) {
                matcher.appendReplacement(sb, "");
            } else {
                matcher.appendTail(sb);
            }
            i++;
        }
        return sb.toString();
    }

    private static boolean canClean(String orderByPart) {
        return orderByPart != null && (!orderByPart.contains(")")
                ||
                StringUtils.countOccurrencesOf(orderByPart, ")") == StringUtils.countOccurrencesOf(orderByPart, "("));
    }

    public static String toCountQuery(String query) {
        return wrapCountQuery(cleanOrderBy(query));
    }

    public static void setParams(Query query, Object beanOrMap) {
        Set<Parameter<?>> nps = query.getParameters();
        if (nps != null) {
            Map<String, Object> params = toParams(beanOrMap);
            for (Parameter<?> key : nps) {
                Object arg = params.get(key.getName());
                if (arg == null) {
                    query.setParameter(key,null);
                } else {
                    query.setParameter(key.getName(), arg);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toParams(Object beanOrMap) {
        Map<String, Object> params;
        if (beanOrMap instanceof Map) {
            params = (Map<String, Object>) beanOrMap;
        } else {
            params = toMap(beanOrMap);
        }
        if (!CollectionUtils.isEmpty(params)) {
            Iterator<String> keys = params.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                if (!isValidValue(params.get(key))) {
                    keys.remove();
                }
            }
        }
        return params;
    }

    public static boolean isValidValue(Object object) {
        if (object == null) {
            return false;
        }
        /*if (object instanceof Number && ((Number) object).longValue() == 0) {
            return false;
		}*/
        return !(object instanceof Collection && CollectionUtils.isEmpty((Collection<?>) object));
    }

    public static Map<String, Object> toMap(Object bean) {
        if (bean == null) {
            return Collections.emptyMap();
        }
        try {
            Map<String, Object> description = new HashMap<>();
            if (bean instanceof DynaBean) {
                DynaProperty[] descriptors = ((DynaBean) bean).getDynaClass().getDynaProperties();
                for (DynaProperty descriptor : descriptors) {
                    String name = descriptor.getName();
                    description.put(name, BeanUtils.getProperty(bean, name));
                }
            } else {
                PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(bean);
                for (PropertyDescriptor descriptor : descriptors) {
                    String name = descriptor.getName();
                    if (PropertyUtils.getReadMethod(descriptor) != null) {
                        description.put(name, PropertyUtils.getNestedProperty(bean, name));
                    }
                }
            }
            return description;
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }


    public static void main(String[] args) {
        String t1 = "select * from user order by id";
        String t2 = "select * from abc order by xxx(convert( resName using gbk )) collate gbk_chinese_ci asc";
        String t3 = "select count * from ((select * from aaa group by a order by a) union all (select * from aaa group by a order by a))";
        System.out.println(QueryBuilder.toCountQuery(t1));
        System.out.println(QueryBuilder.toCountQuery(t2));
        System.out.println(QueryBuilder.toCountQuery(t3));
    }
}
