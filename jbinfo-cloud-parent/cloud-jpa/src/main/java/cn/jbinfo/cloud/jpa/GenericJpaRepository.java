package cn.jbinfo.cloud.jpa;

import cn.jbinfo.cloud.jpa.data.Finder;
import cn.jbinfo.cloud.jpa.dynamic.DynamicBaseDao;
import cn.jbinfo.cloud.jpa.dynamic.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/8/7
 */
@NoRepositoryBean
public interface GenericJpaRepository<T, ID> extends DynamicBaseDao<T, ID>, JpaSpecificationExecutor<T> {

    int updateDelFlag(Serializable id, String delFlag);

    int updateDelFlag(Serializable id);

    int updateByUpdate(Parameter parameter, Parameter where);

    Page<T> find(Finder finder, Pageable p);

    List<T> find(Finder finder);


}
