package cn.jbinfo.cloud.core.model;

import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 功能:顶层实体类，提供实体ID属性，对已数据库的ID主键
 * @author xiaobin
 */
@MappedSuperclass
public class BaseIncrementIdModel implements Serializable{

    private static final long serialVersionUID = 7012274490303344923L;

    @Id
    @Column(length = 32)
    protected String id;

    public BaseIncrementIdModel() {
    }

    public String getId() {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
