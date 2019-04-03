package cn.jbinfo.model.tenant.base;

import cn.jbinfo.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 租户以下所有资源继承此类
 * Created by xiaobin on 2017/8/14.
 */
@Getter
@Setter
@MappedSuperclass
public class TenantBaseModel extends BaseModel {

    //租户Id
    @Column(nullable = false)
    protected String tenantId;
}
