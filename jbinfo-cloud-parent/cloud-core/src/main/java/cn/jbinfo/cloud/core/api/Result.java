package cn.jbinfo.cloud.core.api;


import cn.jbinfo.cloud.core.utils.Strings;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Result implements Serializable{

    private int code;
    private String msg;
    private Object data;

    public Result() {

    }

    public static Result NEW() {
        return new Result();
    }


    public Result addCode(int code) {
        this.code = code;
        return this;
    }

    public Result addMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Result addData(Object data) {
        this.data = data;
        return this;
    }

    public Result(int code, String msg, Object data) {
        this.code = code;
        if (Strings.isBlank(msg)) {
            this.msg = "";
        } else {
            this.msg = msg;
        }
        this.data = data;
    }

    public static Result success(String content) {
        return new Result(0, content, null);
    }

    public static Result success(String content, Object data) {
        return new Result(0, content, data);
    }

    public static Result error(int code, String content) {
        return new Result(code, content, null);
    }

    public static Result error(String content) {
        return new Result(1, content, null);
    }

    public static Result success() {
        return new Result(0, "操作成功", null);
    }

    public static Result error() {
        return new Result(1, "操作失败", null);
    }


}
