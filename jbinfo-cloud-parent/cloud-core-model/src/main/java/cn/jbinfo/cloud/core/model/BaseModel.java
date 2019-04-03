package cn.jbinfo.cloud.core.model;


import cn.jbinfo.cloud.core.model.plugin.IModelInterceptor;
import cn.jbinfo.cloud.core.utils.IdGen;
import cn.jbinfo.cloud.core.utils.SpringContextHolder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 功能:基础实体类
 * @author xiaobin
 */
@Getter
@Setter
@MappedSuperclass
public class BaseModel extends BaseIncrementIdModel implements Serializable {

    private static final long serialVersionUID = -8719003747106030351L;

    public BaseModel() {
        super();
        if (StringUtils.isBlank(id)) {
            isNewRecord = true;
            this.createDate = new Date();
            this.updateDate = new Date();
            this.delFlag = DEL_FLAG_NORMAL;
        } else {
            this.updateDate = new Date();
        }
        if (StringUtils.isBlank(id)) {
            id = IdGen.uuid();
        }
    }

    /**
     * 创建者ID
     */
    @Column(length = 32)
    private String createBy;
    /**
     * 创建者账号
     */
    @Column(length = 32)
    private String updateBy;

    private Date createDate;

    private Date updateDate;

    @Column(length = 2)
    protected String delFlag;    // 删除标记（0：正常；1：删除；2：审核）

    @Column(length = 1000)
    private String remarks;

    @Transient
    protected Map<String, String> sqlMap;

    @JsonIgnore
    @XmlTransient
    public Map<String, String> getSqlMap() {
        if (sqlMap == null) {
            sqlMap = Maps.newHashMap();
        }
        return sqlMap;
    }

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }


    /**
     * 删除标记（0：正常；1：删除；2：审核；）
     */
    public static final String DEL_FLAG_NORMAL = "0";
    public static final String DEL_FLAG_DELETE = "1";
    public static final String DEL_FLAG_AUDIT = "2";

    /**
     * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
     * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
     */
    @Transient
    protected boolean isNewRecord = false;

    public static final String DEL_FLAG = "delFlag";

    @PrePersist
    public void prePersist(){
        try {
            IModelInterceptor modelInterceptor = SpringContextHolder.getBean("modelInterceptor");
            modelInterceptor.add(this);
        }catch (Exception ex){

        }
    }

    @PreUpdate
    public void preUpdate(){
        try {
            IModelInterceptor modelInterceptor = SpringContextHolder.getBean("modelInterceptor");
            modelInterceptor.update(this);
        }catch (Exception ex){

        }
    }

}
