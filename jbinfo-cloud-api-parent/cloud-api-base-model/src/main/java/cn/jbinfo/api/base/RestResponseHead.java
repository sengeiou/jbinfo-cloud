package cn.jbinfo.api.base;

import cn.jbinfo.api.constant.ApiCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xiaobin on 16/7/11.
 */
@Getter
@Setter
public class RestResponseHead {

    private Integer code;//80001

    private String msg;//name:不能缺少此参数

    private String bodyType;//html;json;text

    public RestResponseHead() {
    }

    public RestResponseHead(ApiCode apiCode) {
        this.code = apiCode.getCode();
        this.msg = apiCode.getMessage();
    }

    public void setSimpleCode(ApiCode apiCode, String message) {
        this.code = apiCode.getCode();
        this.msg = apiCode.getMessage() + ":(" + message + ")";
    }

    public void setSimpleCode(ApiCode apiCode) {
        this.code = apiCode.getCode();
        this.msg = apiCode.getMessage();
    }

    public static RestResponseHead oK() {
        return new RestResponseHead(ApiCode.SUCCESS);
    }


    public RestResponseHead(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public void setErrorMsg(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
