package com.tiamo.search.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 搜索 Readhub 请求参数
 * @author wangjian
 * @version 1.0
 * @see SearchReadhubReq
 * @since JDK1.8
 */
@Data
public class SearchReadhubReq implements Serializable {

    @ApiModelProperty(value = "请求消息",required = true)
    private String message;
    @ApiModelProperty(value = "请求的主题名", required = true)
    private String topic;
    @ApiModelProperty(value = "请求页码")
    private Integer pageSize;



}
