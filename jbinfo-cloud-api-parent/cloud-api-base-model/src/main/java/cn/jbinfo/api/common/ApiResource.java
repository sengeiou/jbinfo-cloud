package cn.jbinfo.api.common;

import cn.jbinfo.api.constant.ApiCode;
import cn.jbinfo.api.exception.ApiException;
import cn.jbinfo.common.beanvalidator.BeanValidators;

import java.lang.reflect.Method;

/**
 * Created by xiaobin on 2017/8/14.
 */
public class ApiResource {

    public static <T> T validateWithException(Class<T> clazz, Object validateVo) {
        try {
            BeanValidators.validateWithExceptionError(validateVo);
        } catch (Exception ex) {
            return buildErrorMsg(clazz, 80001, ex.getMessage());
        }
        return null;
    }

    private static <T> T buildErrorMsg(Class<T> clazz, Integer code, String msg) {
        try {
            T obj = clazz.newInstance();
            Method setErr = clazz.getMethod("setErrorMsg", Integer.class, String.class);
            setErr.invoke(obj, code, msg);
            return obj;
        } catch (Exception e) {
            throw new ApiException(ApiCode.REQUEST_PARAM_ERROR, "类型转化错误:" + clazz.getName());
        }
    }
}