package cn.jbinfo.cloud.core.copier;

/**
 * Created by xiaobin on 2016/11/2.
 */
public abstract class BeanCopiers {

    public static <A,B> BeanCopier<A,B> copier(Class<A> clazzA,Class<B> clazzB){
        return new BeanCopier<>(clazzA, clazzB);
    }
}