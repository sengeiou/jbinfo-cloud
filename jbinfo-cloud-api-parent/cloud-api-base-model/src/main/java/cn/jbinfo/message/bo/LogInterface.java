package cn.jbinfo.message.bo;

import cn.jbinfo.common.utils.DateUtils;
import cn.jbinfo.rpc.api.model.DeviceInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

/**
 * 接口异常描述
 *
 * @author xiaobin
 * @create 2017-08-27 下午5:05
 **/
@Getter
@Setter
@ToString
/*@Document(collection = "LOG_INTERFACE")*/
public class LogInterface implements Serializable {

    private static final long serialVersionUID = -2275927051565208767L;
    @Id
    private String id;

    private String apiName;

    private String url;

    //请求方式
    private String requestMethod;


    //会话token
    private String token;

    private DeviceInfo deviceInfo;

    //接口版本
    private String apiVersion;

    //接口参数
    private String apiParamsJson;

    //返回信息
    private String returnJson;

    //1：出现错误
    private String isError;

    private String errorInfo;

    //进入时间
    private String startTime;
    //离开时间
    private String endTime;
    //是否需要登錄
    private Boolean isNeedLogin = true;

    private Long whenLong;

    public Long getWhenLong() {
        if (endTime != null && startTime != null) {
            whenLong = getMillisecond(startTime, endTime);
            return whenLong;
        }
        return 0L;
    }


    private long getMillisecond(String before, String after) {
        try {
            Date beforeTime = DateUtils.parseDate(before, "yyyy-MM-dd HH:mm:ss SSSS");
            Date afterTime = DateUtils.parseDate(after, "yyyy-MM-dd HH:mm:ss SSSS");
            return (afterTime.getTime() - beforeTime.getTime()) / (1000 * 60);
        } catch (ParseException e) {
            //用不报错
        }
        return 0L;
    }

    public static final String COLLECTION_NAME = "LOG_INTERFACE_V2";
}
