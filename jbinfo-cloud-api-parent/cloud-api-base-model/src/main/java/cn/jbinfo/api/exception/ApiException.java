package cn.jbinfo.api.exception;


import cn.jbinfo.api.constant.ApiCode;

/**
 * Created by xiaobin on 16/7/11.
 */
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = -4837736460811153459L;

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable throwable) {
        super(throwable);
    }

    public ApiException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ApiException(ApiCode apiCode) {
        super(apiCode.getCode() + ":" + apiCode.getMessage());
        code = apiCode.getCode();
        desc = apiCode.getMessage();
    }

    public ApiException(ApiCode apiCode, String message) {
        super(apiCode.getCode() + ":" + apiCode.getMessage() + ":(" + message + ")");
        code = apiCode.getCode();
        desc = apiCode.getMessage() + ":(" + message + ")";
    }

    public ApiException(ApiCode apiCode, String message, String apiData) {
        super(apiCode.getCode() + ":" + apiCode.getMessage() + ":(" + message + ")");
        code = apiCode.getCode();
        desc = apiCode.getMessage() + ":(" + message + ")";
        this.apiData = apiData;
    }


    /**
     * 异常代码
     */
    private Integer code;

    /**
     * 异常说明
     */
    private String desc;

    private String apiData;



    public Integer getCode() {
        return code == null ? 500 : code;
    }


    public String getDesc() {
        return desc;
    }

    public String getApiData() {
        return apiData;
    }

    @Override
    public String getMessage() {
        if (super.getMessage() == null) {
            return desc;
        }
        return super.getMessage();
    }
}
