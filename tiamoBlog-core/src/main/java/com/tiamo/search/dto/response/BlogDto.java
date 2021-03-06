package com.tiamo.search.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wangjian on 2019/3/9.
 */
@ApiModel
public class BlogDto implements Serializable {

    @ApiModelProperty(value = "文章ID")
    private String articleId;  // 文章ID

    @ApiModelProperty(value = "标题")
    private String title;   // 文章标题

    @ApiModelProperty(value = "作者")
    private String author;  // 作者

    @ApiModelProperty(value = "创建时间")
    private Date createTime;  // 创建时间

    @ApiModelProperty(value = "正文")
    private String context;  // 正文


    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }


}
