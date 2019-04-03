package cn.jbinfo.api.acl;

import java.util.Map;
import java.util.Set;

/**
 * Token基类
 * Created by xiaobin on 16/7/8.
 */
public interface AccessToken {

    String BEARER_TYPE = "Bearer";

    String BUCKET = "bucket";

    /**
     * The additionalInformation map is used by the token serializers to export any fields used by extensions of OAuth.
     *
     * @return a map from the field name in the serialized token to the value to be exported. The default serializers
     * make use of Jackson's automatic JSON mapping for Java objects (for the Token Endpoint flows) or implicitly call
     * .toString() on the "value" object (for the implicit flow) as part of the serialization process.
     */
    Map<String, Object> getAdditionalInformation();

    Set<String> getScope();

    String getTokenType();

    Boolean isExpired();

    Long getExpiration();

    Integer getExpiresIn();

    String getValue();
}
