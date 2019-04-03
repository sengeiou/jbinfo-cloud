package cn.jbinfo.cloud.jpa;

import org.springframework.core.io.Resource;

import java.util.Iterator;

/**
 * .
 *
 * @author stormning on 2016/12/17.
 */
public interface NamedTemplateResolver {

    Iterator<Void> doSqlInTemplateResource(Resource resource, final NamedTemplateCallback callback) throws Exception;

    Iterator<Void> doHqlInTemplateResource(Resource resource, final NamedTemplateCallback callback) throws Exception;
}
