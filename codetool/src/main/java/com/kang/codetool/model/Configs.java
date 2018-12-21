package com.kang.codetool.model;

import java.util.Date;

public class Configs {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 类型,1=白名单
     */
    private Integer type;

    /**
     * 配置key
     */
    private String key;

    /**
     * 配置信息
     */
    private String value;

    /**
     * 描述
     */
    private String describe;

    /**
     * 有效标识：1=有效,0=无效
     */
    private Integer validFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public String getDescribe() {
        return describe;
    }
    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Integer getValidFlag() {
        return validFlag;
    }
    public void setValidFlag(Integer validFlag) {
        this.validFlag = validFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}