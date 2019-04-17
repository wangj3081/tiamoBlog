package com.tiamo.search.service;

import com.tiamo.entity.BlogEntity;
import com.tiamo.search.dto.BlogRequest;

import java.io.IOException;
import java.util.List;

/**
 * Created by wangjian on 2019/3/9.
 */
public interface SearchArticle {

    /**
     * 根据作者获取文章
     * @param request
     * @return
     */
    List<BlogEntity> queryByAuther(BlogRequest request);

    /**
     * 根据文章内容获取相应文章,只返回文章与标题
     * @param contextStr
     * @return
     */
    List<BlogEntity> queryByContext(String contextStr);

    /**
     * 根据文章 Id 查询指定索引名(作者)的文章
     * @param articleId
     * @param indexName
     * @return
     */
    BlogEntity queryByArticleId(String articleId, String indexName);

    /**
     * 批量写入文章
     * @param list
     * @param indexName  索引名
     * @param type 类型
     * @return
     */
    boolean insertArticle(List<BlogEntity> list, String indexName, String type);

}
