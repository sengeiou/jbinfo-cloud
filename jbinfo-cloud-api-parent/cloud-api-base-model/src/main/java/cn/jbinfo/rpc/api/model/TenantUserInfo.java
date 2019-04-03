package cn.jbinfo.rpc.api.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by xiaobin on 2017/8/17.
 */
@Getter
@Setter
public class TenantUserInfo implements Serializable {
    private static final long serialVersionUID = 1745217612613743749L;

    private String userId;

    private String username;

    private String unitName;

    public TenantUserInfo() {
    }
}
