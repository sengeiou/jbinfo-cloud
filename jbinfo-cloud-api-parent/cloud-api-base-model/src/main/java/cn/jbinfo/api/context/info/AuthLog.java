package cn.jbinfo.api.context.info;

import cn.jbinfo.rpc.api.model.DeviceInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 认证信息
 *
 * @author xiaobin
 * @create 2017-08-27 下午7:02
 **/
@Getter
@Setter
@ToString
public class AuthLog implements Serializable {

    private DeviceInfo deviceInfo;

    private String token;

    public AuthLog() {
    }

    public AuthLog(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
