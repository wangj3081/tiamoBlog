package com.tiamo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: wangjian
 */
@Data
public class BookMappingEntity implements Serializable {

    private String title; // 标题

    private String linkAddress; // 链接地址

    private String author; // 作者

    private String introduction; // 简介

    private Date createTime; // 获取时间

    private String type; // 文件类型

    private String sourceInformation; // 信息来源


}
