package com.tiamo.search.service;

import com.tiamo.entity.BookMappingEntity;

import java.util.List;

/**
 *  搜索图书服务
 * @Auther: wangjian
 */
public interface SearchBookService {

    /**
     *  根据标题搜索内容
     * @param title
     * @return
     */
    List<BookMappingEntity> searchBookForTitle(String title);

    /**
     * 根据标题文件类型搜索内容
     * @param title
     * @param type
     * @return
     */
    List<BookMappingEntity> searchBookForTitle(String title, String type);

    /**
     * 要写入的数据
     * @param bookList
     * @return
     */
    boolean insertBookList(List<BookMappingEntity> bookList);

}
