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
 * @see SearchReadHubService
 * @since JDK1.8
 */
public interface SearchReadHubService {

    /**
     * 获取相关的数据信息
     * @param jsonObject
     * @return
     */
    Map<String, Object> queryByReadHubNews(JSONObject jsonObject);

    /**
     * 获取标题列表
     * @param title
     * @return
     */
    JSONArray queryTitleList(String title);

    /**
     * 获取指定话题的最新数据
     * @param topic
     * @return
     */
    Map<String, Object> queryByTopicNewsLast(String topic);
}
