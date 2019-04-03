package cn.jbinfo.cloud.core.model;

/**
 * <code>BaseMongoModel.java</code>
 * <p>功能: mongodb 基础model接口,所有实体文档对象需要实现
 */
public interface BaseMongoModel {

    /**
     * 功能:获取主键id
     */
    String getId();
}
