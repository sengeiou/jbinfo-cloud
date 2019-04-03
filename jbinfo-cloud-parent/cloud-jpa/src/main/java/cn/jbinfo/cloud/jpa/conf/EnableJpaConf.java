package cn.jbinfo.cloud.jpa.conf;

import cn.jbinfo.cloud.jpa.dynamic.DefaultDynamicHibernateAssembleBuilder;
import cn.jbinfo.cloud.jpa.dynamic.DynamicHibernateAssembleBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaobin
 * @create 2017-09-15 下午8:46
 **/
/*@EnableJpaRepositories(repositoryBaseClass = GenericJpaRepositoryImpl.class,
        repositoryFactoryBeanClass = GenericJpaRepositoryFactoryBean.class,basePackageClasses = GenericJpaRepositoryImpl.class)*/
@Configuration
public class EnableJpaConf {

    @Bean
    public DynamicHibernateAssembleBuilder defaultDynamicHibernateAssembleBuilder(){
        String[] fileNames = new String[]{"classpath*:sqls/*-dynamic.xml"};
        DynamicHibernateAssembleBuilder assembleBuilder = new DefaultDynamicHibernateAssembleBuilder();
        assembleBuilder.setFileNames(fileNames);
        return assembleBuilder;
    }

}
