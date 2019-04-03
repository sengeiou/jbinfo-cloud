package cn.jbinfo.cloud.core.model;

import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * @author xiaobin
 * @create 2017-10-28 下午2:30
 **/
public abstract class BaseIdModel<ID extends Serializable> implements Serializable {

    protected ID id;

    public BaseIdModel() {
    }

    public ID getId() {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseIdModel other = (BaseIdModel) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
