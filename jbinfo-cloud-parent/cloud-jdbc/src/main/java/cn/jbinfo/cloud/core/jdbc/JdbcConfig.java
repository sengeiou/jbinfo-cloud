package cn.jbinfo.cloud.core.jdbc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author xiaobin
 * @create 2017-10-27 下午5:37
 **/
@Configuration
public class JdbcConfig {

    @Bean
    public MyJdbcTemplate myJdbcTemplate(DataSource dataSource){
        return new MyJdbcTemplate(dataSource);
    }
}
