package cn.jbinfo.api.base;

import cn.jbinfo.api.constant.ApiCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xiaobin on 16/7/11.
 */
@Getter
@Setter
@ApiModel("响应")
public class RestResponse<E> {

    @ApiModelProperty("响应头")
    private RestResponseHead head;

    public void setOK() {
        head = new RestResponseHead(ApiCode.SUCCESS);
    }
    public void setNO(){
        head = new RestResponseHead(ApiCode.INTERNAL_ERROR);
    }

    @ApiModelProperty("响应体")
    @JsonProperty
    private E body;

    public RestResponse() {
    }

    public RestResponse(RestResponseHead head) {
        this.head = head;
    }
}
