package cn.jbinfo.cloud.jpa.dynamic;import java.io.InputStream;import java.io.Serializable;import org.apache.commons.lang3.StringUtils;import org.hibernate.internal.util.ConfigHelper;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.xml.sax.EntityResolver;import org.xml.sax.InputSource;/** * hibernate动态sql dtd解析器 * *@author xiaobin */@Deprecatedpublic class DynamicAssembleDTDEntityResolver implements EntityResolver,        Serializable {    private static final long serialVersionUID = 8123799007554762965L;    private static final Logger LOGGER = LoggerFactory            .getLogger(DynamicAssembleDTDEntityResolver.class);    //private static final String HOP_DYNAMIC_STATEMENT = "http://www.dynamic.com/dtd/";    private static final String HOP_DYNAMIC_STATEMENT = "";    // dtd 文件路径    private static final String getDTDPackPath() {        String packName = DynamicAssembleDTDEntityResolver.class.getPackage()                .getName();        String newPackName = StringUtils.replace(packName, ".", "/");        return newPackName + "/";    }    public static void main(String[] args) {        System.out.println("newPackName==" + getDTDPackPath());    }    public InputSource resolveEntity(String publicId, String systemId) {        InputSource source = null;        source = resolveOnClassPath(publicId, systemId,                HOP_DYNAMIC_STATEMENT);        return source;    }    private InputSource resolveOnClassPath(String publicId, String systemId,                                           String namespace) {        InputSource source = null;        String path = getDTDPackPath() + systemId.substring(namespace.length());        InputStream dtdStream = resolveInHibernateNamespace(path);        if (dtdStream == null) {            LOGGER.debug("unable to locate [" + systemId + "] on classpath");            if (systemId.substring(namespace.length()).contains("2.0")) {                LOGGER.error("Don't use old DTDs, read the Hibernate 4.x Migration Guide!");            }        } else {            LOGGER.debug("located [" + systemId + "] in classpath");            source = new InputSource(dtdStream);            source.setPublicId(publicId);            source.setSystemId(systemId);        }        return source;    }    protected InputStream resolveInHibernateNamespace(String path) {        return this.getClass().getClassLoader().getResourceAsStream(path);    }    protected InputStream resolveInLocalNamespace(String path) {        try {            return ConfigHelper.getUserResourceAsStream(path);        } catch (Throwable t) {            return null;        }    }}