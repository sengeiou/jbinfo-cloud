package cn.jbinfo.cloud.core.model.plugin;

import cn.jbinfo.cloud.core.model.BaseModel;

/**
 * 持久类新增、修改扩展
 * @author xiaobin
 * @create 2017-10-13 下午4:17
 **/
public interface IModelInterceptor {

    <MODEL extends BaseModel> void add(MODEL model);

    <MODEL extends BaseModel> void update(MODEL model);
}
