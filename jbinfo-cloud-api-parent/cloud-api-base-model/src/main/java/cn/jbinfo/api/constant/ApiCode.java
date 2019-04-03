package cn.jbinfo.api.constant;

/**
 * Created by xiaobin on 16/7/12.
 */
public enum ApiCode {

    SUCCESS(200, "成功"),

    INTERNAL_ERROR(500, "服务器内部错误"),
    INTERNAL_JSON_ERROR(501, "JSON处理失败"),


    REQUEST_PARAM_ERROR(2, "传入参数错误"),
    REQUEST_METHOD_ERROR(3, "请求方法错误"),
    REQUEST_JSON_ERROR(4, "JSON格式错误"),
    REQUEST_CONTENT_TYPE_ERROR(5, "需要 application/json 请求"),
    REQUEST_FREQUENCY_HIGH(6, "请求频率过高"),
    NOT_FOUND_VERSION(7, "无版本号信息"),
    AUTH_LESS(10, "缺少授权信息"),
    AUTH_TIME_OUT(11, "授权信息信息失效"),
    AUTH_ERROR(12, "授权信息验证失败"),
    SAVE_SESSIONTOKEN_ERROR(103, "生成token异常"),

    //设备绑定相关
    DEVICE_NOT_BIND(201, "设备未绑定"),

    DEVICE_NOT_FOUND_TENANT(203, "未找到对应的商户"),

    //10xx 应用相关
    APP_NOT_FOUND(1001, "未配置有效的API_ID"),
    APP_ALREADY_EXIST(1002, "应用名已存在"),
    APP_NAME_ERROR(1003, "应用名称错误"),

    //11xx 支付相关
    ORDER_GOOD_NOT_FOUND(1101, "商品不存在"),
    ORDER_GOOD_PRICE_NOT_CONFORMTTY(1102, "价格不符"),
    ORDER_ID_NOT_FOUND(1103, "订单不存在"),
    ORDER_GOOD_Authentication_ERROR(1104, "设备、商品鉴权失败"),
    ORDER_GOOD_PRICE_NOT_ZERO(1105, "价格不能为空或者为0"),

    //12xx 版本更新相关
    APP_VERSION_NOT_FOUND(1201, "不存在更新的版本"),

    //13xx PMS對接相關
    PMS_AUTH_ERROR(1301, "授权信息验证失败"),


    //参数相关
    PARAM_NOT_NULL(1101, "参数不能为空"),
    PARAM_NOT_VALID(1102, "参数的取值不是有效的值"),

    COMMON_ERROR(20002, "数据出现异常"),

    //8.APP登录相关
    APP_MEMBER_NOT_DATA_RANGE(80001,"无数据权限");



    private int code;
    private String message;

    ApiCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
