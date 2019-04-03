package cn.jbinfo.api.context.info;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 异常日志
 *
 * @author xiaobin
 * @create 2017-08-27 下午7:23
 **/
@Getter
@Setter
@ToString
public class ErrorLog implements Serializable {

    //返回信息
    private String returnJson;

    //1：出现错误
    private String isError;

    private String errorInfo;

    public ErrorLog(){
        this.isError =  "1";
    }

}
