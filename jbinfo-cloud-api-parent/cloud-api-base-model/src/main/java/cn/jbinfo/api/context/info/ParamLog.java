package cn.jbinfo.api.context.info;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 参数日志
 *
 * @author xiaobin
 * @create 2017-08-27 下午7:04
 **/
@Getter
@Setter
@ToString
public class ParamLog implements Serializable {

    //接口参数
    private String interfaceParamsJson;

    //接口版本
    private String interfaceVersion;

    //1：出现错误
    private String isError;

    public ParamLog() {
        isError = "0";
    }

    public ParamLog(String interfaceVersion, String interfaceParamsJson) {
        this.interfaceVersion = interfaceVersion;
        this.interfaceParamsJson = interfaceParamsJson;
        isError = "0";
    }
}
