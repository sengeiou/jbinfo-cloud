package cn.jbinfo.api.exception;


import cn.jbinfo.api.constant.ApiCode;

/**
 * <p>
 * 异常类
 * </p>
 *
 * @author xiaobin
 * @Date 2016-01-23
 */
public class SystemException extends RuntimeException {

    public SystemException(String message) {
        super(message);
    }

    public SystemException(Throwable throwable) {
        super(throwable);
    }

    public SystemException(String message, Throwable throwable) {
        super(message, throwable);
    }

    private static final long serialVersionUID = 8604424364318396626L;


    public SystemException(ApiCode simpleCode) {
        super(simpleCode.getCode() + ":" + simpleCode.getMessage());
        code = simpleCode.getCode();
        desc = simpleCode.getMessage();
    }

    public SystemException(ApiCode simpleCode, String message) {
        super(simpleCode.getCode() + ":" + simpleCode.getMessage() + ":(" + message + ")");
        code = simpleCode.getCode();
        desc = simpleCode.getMessage() + ":(" + message + ")";
    }


    /**
     * 异常代码
     */
    private Integer code;

    /**
     * 异常说明
     */
    private String desc;


    public SystemException() {
        super();
    }


    public SystemException(Integer code, String desc) {
        super(code + ":(" + desc + ")");
        this.code = code;
        this.desc = desc;
    }


    public SystemException(Integer code, String desc, Throwable cause) {
        super(cause);
        this.code = code;
        this.desc = desc;
    }


    public SystemException(Integer code, String desc, String message) {
        super(code + ":(" + desc + ")" + ",message:" + message);
        this.code = code;
        this.desc = desc;
    }


    public Integer getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }


    @Override
    public String getMessage() {
        if (super.getMessage() == null) {
            return desc;
        }
        return super.getMessage();
    }
}
