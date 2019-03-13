package com.tiamo.util;

import io.swagger.annotations.ApiModelProperty;

/**
 * 分页请求
 * @Auther: wangjian
 * @Date: 2019-03-13 16:23:48
 */
public class PageUtil {

    @ApiModelProperty(value = "页码")
    private Integer page;

    @ApiModelProperty(value = "每页展示数")
    private Integer size;

    @ApiModelProperty(value = "排序字段")
    private String order;

    @ApiModelProperty(value = "是否倒序, true:正序 false: 倒序")
    private Boolean desc;

    public Integer getPage() {
        return page == null ? 1 : this.page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size == null ? 10 : this.size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Boolean getDesc() {
        return desc == null ? false : desc;
    }

    public void setDesc(Boolean desc) {
        this.desc = desc;
    }
}
