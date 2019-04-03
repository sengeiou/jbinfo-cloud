package cn.jbinfo.cloud.jpa.dynamic;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface DynamicBaseDao<T, ID> extends JpaRepository<T, ID> {

	/**
	 * 查询在xxx.dynamic.xml中配置的查询语句
	 * <p>
	 * 可查询sql或者hql占位符的语句,也可以直接写sql或者hql
	 * </p>
	 *
	 * @param queryName
	 * @param parameters
	 * @return
	 */
	<X> List<X> findByNamedQuery(final String queryName,
                                 final Parameter parameters, Class<X>... clazz);

	/**
	 * 更新在xxx.dynamic.xml中配置的更新语句
	 * <p>
	 * 可更新sql或者hql占位符的语句,也可以直接写sql或者hql
	 * </p>
	 *
	 * @param parameters
	 *            参数
	 * @return
	 */
	int updateByNamedQuery(final Parameter parameters);

	<X> Page<X> findPageByNamedQuery(Parameter parameter, Pageable p, Class<X> clazzs);
}
