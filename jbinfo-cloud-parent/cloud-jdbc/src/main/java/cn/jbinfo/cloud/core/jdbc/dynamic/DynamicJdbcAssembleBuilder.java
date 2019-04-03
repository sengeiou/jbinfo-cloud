package cn.jbinfo.cloud.core.jdbc.dynamic;

import java.io.IOException;
import java.util.Map;

/**
 * @author xiaobin
 * @create 2018-03-08 下午7:35
 **/
public interface DynamicJdbcAssembleBuilder {

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
