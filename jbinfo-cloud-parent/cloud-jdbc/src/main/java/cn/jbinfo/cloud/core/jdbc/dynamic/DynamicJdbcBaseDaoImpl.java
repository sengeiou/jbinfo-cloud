package cn.jbinfo.cloud.core.jdbc.dynamic;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaobin
 * @create 2018-03-08 下午7:44
 **/
public class DynamicJdbcBaseDaoImpl extends NamedParameterJdbcTemplate implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicJdbcBaseDaoImpl.class);

    protected DynamicJdbcAssembleBuilder dynamicAssembleBuilder;

    protected Map<String, StatementTemplate> templateCache;

    public DynamicJdbcBaseDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Autowired
    public void setDynamicStatementBuilder(
            DynamicJdbcAssembleBuilder dynamicAssembleBuilder) {
        this.dynamicAssembleBuilder = dynamicAssembleBuilder;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        templateCache = new ConcurrentHashMap<>();

        dynamicAssembleBuilder.init();
        Map<String, String> namedSQLQueries = dynamicAssembleBuilder
                .getNamedSQLQueries();
        Configuration configuration = new Configuration();
        configuration.setNumberFormat("#");
        StringTemplateLoader stringLoader = new StringTemplateLoader();

        for (Map.Entry<String, String> entry : namedSQLQueries.entrySet()) {
            stringLoader.putTemplate(entry.getKey(), entry.getValue());
            templateCache
                    .put(entry.getKey(),
                            new StatementTemplate(StatementTemplate.TYPE.SQL,
                                    new Template(entry.getKey(),
                                            new StringReader(entry.getValue()),
                                            configuration)));
        }
        configuration.setTemplateLoader(stringLoader);
    }

    protected String processTemplate(StatementTemplate statementTemplate,
                                     Map<String, ?> parameters) {
        StringWriter stringWriter = new StringWriter();
        try {
            statementTemplate.getTemplate().process(parameters, stringWriter);
        } catch (Exception e) {
            LOGGER.error("处理DAO查询参数模板时发生错误：{}", e.toString());
            throw new RuntimeException(e);
        }
        return stringWriter.toString();
    }
}
