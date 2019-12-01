package com.tiamo.search.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tiamo.entity.ReadHubNewsEntity;
import com.tiamo.webdata.ReadHubData;

import java.util.List;
import java.util.Map;

/**
 * 针对从 readhub API处获取到的数据做查询
 * @author wangjian
 * @version 1.0
 * @see com.tiamo.search.service.SearchReadHubService
 * @since JDK1.8
 */
public interface SearchReadHubService {

    /**
     * 获取相关的数据信息
     * @param jsonObject
     * @param clazz  返回结果集要的数据实体
     * @return
     */
    <T> Map<String, Object> queryByReadHubNews(JSONObject jsonObject, Class<T> clazz);


    /**
     * 获取标题列表
     * @param title
     * @return
     */
    JSONArray queryTitleList(String title);

    /**
     * 获取指定话题的最新数据
     * @param topic
     * @param clazz 返回的列表集合的数据类型
     * @return
     */
    <T> Map<String, Object> queryByTopicNewsLast(String topic, Class<T> clazz);
}
