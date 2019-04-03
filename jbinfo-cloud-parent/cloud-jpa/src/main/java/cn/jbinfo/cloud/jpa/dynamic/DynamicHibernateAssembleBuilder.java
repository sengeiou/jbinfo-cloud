package cn.jbinfo.cloud.jpa.dynamic;

import java.io.IOException;
import java.util.Map;

/**
 * 动态sql/hql语句组装器
 * 
 * 
 * 
 */
public interface DynamicHibernateAssembleBuilder {
	/**
	 * hql语句map
	 * 
	 * @return
	 */
	Map<String, String> getNamedHQLQueries();

	void setFileNames(String[] fileNames);

	/**
	 * sql语句map
	 * 
	 * @return
	 */
	Map<String, String> getNamedSQLQueries();

	/**
	 * 初始化
	 * 
	 * @throws IOException
	 */
	void init() throws IOException;
}
