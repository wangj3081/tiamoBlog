package com.tiamo.search.service;

import com.tiamo.entity.BlogEntity;

import java.util.List;

/**
 * Created by wangjian on 2019/3/9.
 */
public interface SearchArticle {

    /**
     * 根据作者获取文章
     * @param auther
     * @return
     */
    List<BlogEntity> queryByAuther(String auther);

    /**
     * 根据文章内容获取相应文章
     * @param contextStr
     * @return
     */
    List<BlogEntity> queryByContext(String contextStr);

    /**
     * 批量写入文章
     * @param list
     * @param indexName  索引名
     * @param type 类型
     * @return
     */
    boolean insertArticle(List<BlogEntity> list, String indexName, String type);

}
