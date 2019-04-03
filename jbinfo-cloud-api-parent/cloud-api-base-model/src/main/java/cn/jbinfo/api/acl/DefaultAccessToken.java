package cn.jbinfo.api.acl;

import cn.jbinfo.api.constant.Resource;
import cn.jbinfo.common.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

/**
 * access token for api
 * Created by xiaobin on 16/7/8.
 */
public class DefaultAccessToken implements Serializable, AccessToken {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAccessToken.class);

    private static final long serialVersionUID = 914967629530462926L;

    private String value;

    private Long expiration;

    private String tokenType = BEARER_TYPE.toLowerCase();

    private Set<String> scope;

    private Map<String, Object> additionalInformation = Collections.emptyMap();

    /**
     * Create an access token from the value provided.
     */
    public DefaultAccessToken(String value) {
        this.value = value;
    }

    /**
     * Create an access token from the value provided.
     */
    public DefaultAccessToken(String bucket, String value) {
        this.value = value;
        Map<String, Object> additionalInformation = new HashMap<>();
        additionalInformation.put(AccessToken.BUCKET, bucket);
        this.setAdditionalInformation(additionalInformation);
        //默认:48小时
        this.setExpiration(System.currentTimeMillis() + (Resource.ACCESS_TOKEN_EXPIRATION_TIME * 49L));
        if (this.scope == null) {
            this.scope = new HashSet<>();
        }
        this.scope.add(bucket);
    }

    /**
     * 手动设置时长
     * Create an access token from the value provided.
     */
    public DefaultAccessToken(String bucket, String value, Long expiration) {
        this.value = value;
        Map<String, Object> additionalInformation = new HashMap<>();
        additionalInformation.put(AccessToken.BUCKET, bucket);
        this.setAdditionalInformation(additionalInformation);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(expiration);
        this.setExpiration(cal.getTimeInMillis());
        //LOGGER.info("expiration:" + expiration + ",有效期至:" + DateUtils.formatDate(this.getExpiration(), "yyyy-MM-dd HH:mm:ss"));
        if (this.scope == null) {
            this.scope = new HashSet<>();
        }
        this.scope.add(bucket);
    }

    /**
     * Private constructor for JPA and other serialization tools.
     */
    @SuppressWarnings("unused")
    private DefaultAccessToken() {
        this(null);
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * The token value.
     *
     * @return The token value.
     */
    public String getValue() {
        return value;
    }

    public Integer getExpiresIn() {
        return 0;
    }

    /*protected void setExpiresIn(int delta) {
        setExpiration(new Date(System.currentTimeMillis() + delta));
    }*/

    /**
     * The instant the token expires.
     *
     * @return The instant the token expires.
     */
    public Long getExpiration() {
        return expiration;
    }

    /**
     * The instant the token expires.
     *
     * @param expiration The instant the token expires.
     */
    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    /**
     * Convenience method for checking expiration
     *
     * @return true if the expiration is befor ethe current time
     */
    @JsonIgnore
    public Boolean isExpired() {
        //return expiration != null && expiration.before(new Date());
        return true;
    }

    /**
     * The token type, as introduced in draft 11 of the OAuth 2 spec. The spec doesn't define (yet) that the valid token
     * types are, but says it's required so the default will just be "undefined".
     *
     * @return The token type, as introduced in draft 11 of the OAuth 2 spec.
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     * The token type, as introduced in draft 11 of the OAuth 2 spec.
     *
     * @param tokenType The token type, as introduced in draft 11 of the OAuth 2 spec.
     */
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    /**
     * The scope of the token.
     *
     * @return The scope of the token.
     */
    public Set<String> getScope() {
        return scope;
    }

    /**
     * The scope of the token.
     *
     * @param scope The scope of the token.
     */
    public void setScope(Set<String> scope) {
        this.scope = scope;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && toString().equals(obj.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }

    /**
     * Additional information that token granters would like to add to the token, e.g. to support new token types.
     *
     * @return the additional information (default empty)
     */
    public Map<String, Object> getAdditionalInformation() {
        return additionalInformation;
    }

    /**
     * Additional information that token granters would like to add to the token, e.g. to support new token types. If
     * the values in the map are primitive then remote communication is going to always work. It should also be safe to
     * use maps (nested if desired), or something that is explicitly serializable by Jackson.
     *
     * @param additionalInformation the additional information to set
     */
    public void setAdditionalInformation(Map<String, Object> additionalInformation) {
        this.additionalInformation = new LinkedHashMap<>(additionalInformation);
    }

}
