package com.tiamo.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by wangjian on 2019/3/9.
 */
public class BlogEntity implements Serializable {

    private String articleId;  // 文章ID

    private String title;   // 文章标题

    private String author;  // 作者

    private Date createTime;  // 创建时间

    private String context;  // 正文

    private List<String> imageUrls; // 文章图片地址

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

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
