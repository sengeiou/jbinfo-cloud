package cn.jbinfo.integration.swagger.version;

import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;

import java.lang.annotation.*;

/**
 * @author xiaobin
 * @create 2017-11-01 下午4:18
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Api
public @interface Api2{

    /**
     * Implicitly sets a tag for the operations, legacy support (read description).
     * <p>
     * In swagger-core 1.3.X, this was used as the 'path' that is to host the API Declaration of the
     * resource. This is no longer relevant in swagger-core 1.5.X.
     * <p>
     * If {@link #tags()} is <i>not</i> used, this value will be used to set the tag for the operations described by this
     * resource. Otherwise, the value will be ignored.
     * <p>
     * The leading / (if exists) will be removed.
     *
     * @return tag name for operations under this resource, unless {@link #tags()} is defined.
     */
    String value() default "";

    /**
     * A list of tags for API documentation control.
     * Tags can be used for logical grouping of operations by resources or any other qualifier.
     * <p>
     * A non-empty value will override the value provided in {@link #value()}.
     *
     * @return a string array of tag values
     * @since 1.5.2-M1
     */
    String[] tags() default "";

    /**
     * Not used in 1.5.X, kept for legacy support.
     *
     * @return a longer description about this API, no longer used.
     */
    @Deprecated String description() default "";

    /**
     * Not used in 1.5.X, kept for legacy support.
     *
     * @return the basePath for this operation, no longer used.
     */
    @Deprecated String basePath() default "";

    /**
     * Not used in 1.5.X, kept for legacy support.
     *
     * @return the position of this API in the resource listing, no longer used.
     */
    @Deprecated int position() default 0;

    /**
     * Corresponds to the `produces` field of the operations under this resource.
     * <p>
     * Takes in comma-separated values of content types.
     * For example, "application/json, application/xml" would suggest the operations
     * generate JSON and XML output.
     * <p>
     * For JAX-RS resources, this would automatically take the value of the {@code @Produces}
     * annotation if such exists. It can also be used to override the {@code @Produces} values
     * for the Swagger documentation.
     *
     * @return the supported media types supported by the server, or an empty string if not set.
     */
    String produces() default "";

    /**
     * Corresponds to the `consumes` field of the operations under this resource.
     * <p>
     * Takes in comma-separated values of content types.
     * For example, "application/json, application/xml" would suggest the operations
     * accept JSON and XML input.
     * <p>
     * For JAX-RS resources, this would automatically take the value of the {@code @Consumes}
     * annotation if such exists. It can also be used to override the {@code @Consumes} values
     * for the Swagger documentation.
     *
     * @return the consumes value, or empty string if not set
     */
    String consumes() default "";

    /**
     * Sets specific protocols (schemes) for the operations under this resource.
     * <p>
     * Comma-separated values of the available protocols. Possible values: http, https, ws, wss.
     *
     * @return the protocols supported by the operations under the resource.
     */
    String protocols() default "";

    /**
     * Corresponds to the `security` field of the Operation Object.
     * <p>
     * Takes in a list of the authorizations (security requirements) for the operations under this resource.
     * This may be overridden by specific operations.
     *
     * @return an array of authorizations required by the server, or a single, empty authorization value if not set.
     * @see Authorization
     */
    Authorization[] authorizations() default @Authorization(value = "");

    /**
     * Hides the operations under this resource.
     *
     * @return true if the api should be hidden from the swagger documentation
     */
    boolean hidden() default false;
}
