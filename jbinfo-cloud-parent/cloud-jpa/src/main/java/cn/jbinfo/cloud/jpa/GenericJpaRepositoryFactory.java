package cn.jbinfo.cloud.jpa;

import cn.jbinfo.cloud.jpa.dynamic.DefaultDynamicHibernateAssembleBuilder;
import cn.jbinfo.cloud.jpa.dynamic.DynamicHibernateAssembleBuilder;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/8/9.
 */
class GenericJpaRepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {

	private final EntityManager em;

	public GenericJpaRepositoryFactory(EntityManager em) {
		super(em);
		this.em = em;
	}

	//设置具体的实现类是BaseRepositoryImpl
	@Override
	protected Object getTargetRepository(RepositoryInformation information) {
		GenericJpaRepositoryImpl jpaRepository = new GenericJpaRepositoryImpl<T, I>((Class<T>) information.getDomainType(), em);
		try {
			String[] fileNames = new String[]{"classpath*:sqls/*-dynamic.xml"};
			DynamicHibernateAssembleBuilder assembleBuilder = ContextHolder.getBean(DynamicHibernateAssembleBuilder.class);
			assembleBuilder.setFileNames(fileNames);
			jpaRepository.setDynamicStatementBuilder(assembleBuilder);
			jpaRepository.afterPropertiesSet();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return jpaRepository;
	}

	//设置具体的实现类的class
	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		return GenericJpaRepositoryImpl.class;
	}

}
