package cn.jbinfo.api.handler;

import cn.jbinfo.api.base.RestResponse;
import cn.jbinfo.api.base.RestResponseHead;
import cn.jbinfo.api.context.InterfaceContextManager;
import cn.jbinfo.api.context.info.ErrorLog;
import cn.jbinfo.api.exception.ApiException;
import cn.jbinfo.api.exception.SystemException;
import cn.jbinfo.cloud.core.utils.Exceptions;
import cn.jbinfo.common.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

/**
 * Created by xiaobin on 16/5/26.
 */
@ControllerAdvice(annotations = RestController.class)
public class ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public RestResponse<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) throws Exception{
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
            throw e;
        BindingResult bindingResult = e.getBindingResult();
        FieldError firstError = bindingResult.getFieldErrors().get(0);
        RestResponse<String> restResponse = new RestResponse<>();
        RestResponseHead head = new RestResponseHead(2, firstError.getField()+":"+firstError.getDefaultMessage());
        restResponse.setHead(head);
        buildErrorLog(restResponse, e);
        //LOGGER.info("接口出现异常（ControllerException）：" + JsonUtils.writeObjectToJson(restResponse));
        return restResponse;
    }

    @ExceptionHandler(SystemException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public RestResponse errorResponse(SystemException e) {
        RestResponse<String> restResponse = new RestResponse<>();
        RestResponseHead head = new RestResponseHead(e.getCode(), e.getMessage());
        restResponse.setHead(head);
        buildErrorLog(restResponse, e);
        //interfaceError.set
        //LOGGER.info("接口出现异常（SystemException）：" + JsonUtils.writeObjectToJson(restResponse));
        return restResponse;
    }

    @ExceptionHandler(ApiException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public RestResponse errorResponse(ApiException e) {
        RestResponse<String> restResponse = new RestResponse<>();
        RestResponseHead head = new RestResponseHead(e.getCode(), e.getMessage());
        restResponse.setHead(head);
        buildErrorLog(restResponse, e);
        //LOGGER.info("接口出现异常（ControllerException）：" + JsonUtils.writeObjectToJson(restResponse));
        return restResponse;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public RestResponse errorResponse(Throwable t) {
        LOGGER.error("error:", t);
        RestResponse<String> restResponse = new RestResponse<>();
        RestResponseHead head = new RestResponseHead(500, t.getMessage());
        restResponse.setHead(head);
        buildErrorLog(restResponse, t);
        //LOGGER.info("接口出现异常（Throwable）：" + JsonUtils.writeObjectToJson(restResponse));
        return restResponse;
    }


    private void buildErrorLog(RestResponse<String> restResponse, Throwable t) {
        ErrorLog paramLog = new ErrorLog();
        paramLog.setIsError("1");
        paramLog.setReturnJson(JsonUtils.writeObjectToJson(restResponse));
        paramLog.setErrorInfo(Exceptions.getStackTraceAsString(t));
        InterfaceContextManager.error(paramLog);
    }

}
