package cn.jbinfo.cloud.core.copier;

/**
 * Created by xiaobin on 2016/11/2.
 */
public class BeanTransformer<F,T> extends BeanCopier<F,T> {

    public BeanTransformer(Class<F> clazzFrom, Class<T> clazzTo) {
        super(clazzFrom, clazzTo);
    }

    @Override
    public T copy(F f, T t) {
        return doBusiness(f, super.copy(f, t));
    }

    public T doBusiness(F f, T t) {
        return t;
    }
}