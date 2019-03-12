package com.tiamo.search.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tiamo.entity.BlogEntity;
import com.tiamo.search.service.SearchArticle;
import com.tiamo.util.EsClient;
import com.tiamo.util.EsUtil;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by wangjian on 2019/3/9.
 */
@Service
public class SearchArticleImpl implements SearchArticle {


    private Logger logger = LoggerFactory.getLogger(SearchArticleImpl.class);


    @Override
    public List<BlogEntity> queryByAuther(String author) {
        logger.debug("【根据作者获取文章列表】: {}", author);
        List<BlogEntity> result = new ArrayList<>();

        Client client = EsClient.getEsClient();
        SearchRequestBuilder searchBuilder = client.prepareSearch(author); // 在该作者的索引
        TermQueryBuilder termQuery = new TermQueryBuilder("author", author); // 查找作者的名称,精确查找
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.should(termQuery);
        SearchRequestBuilder searchRequestBuilder = searchBuilder.setQuery(boolQueryBuilder)
                .addSort("createTime", SortOrder.DESC);
        System.out.println(searchRequestBuilder.toString());
        SearchResponse searchResponse = searchRequestBuilder // 按照时间做倒序处理
                .execute().actionGet();
        if (searchResponse != null) {
            SearchHit[] hits = searchResponse.getHits().getHits();
            for (SearchHit hit : hits) {
                BlogEntity blogEntity = JSONArray.parseObject(hit.getSourceAsString(), BlogEntity.class);
                result.add(blogEntity);
            }
        }
        return result;
    }

    @Override
    public List<BlogEntity> queryByContext(String contextStr) {
        return null;
    }

    @Override
    public boolean insertArticle(List<BlogEntity> list,String indexName, String type) {
        logger.debug("【批量写入文章】:{}", JSONObject.toJSONString(list));

        try {
            EsUtil.createIndexTeample(indexName, getMapping(), type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (BlogEntity blogEntity : list) {
            IndexRequest indexRequest = new IndexRequest(indexName, type, UuidUtil.getTimeBasedUuid().toString());
            String text = JSONObject.toJSONString(blogEntity);
            indexRequest.source(JSONObject.parseObject(text));
            EsUtil.getBulkProcessor().add(indexRequest);
        }
        return true;
    }

    // 创建Mapping
    public static XContentBuilder getMapping() throws IOException {
        XContentBuilder mapping;
        mapping = jsonBuilder()
                .startObject()
                .startObject("properties")
                .startObject("context")
                .field("type","text")
                .field("fielddata",true)
                .field("analyzer","ik_max_word")
                .startObject("fields")
                .startObject("keyword")
                .field("type","keyword")  // 新增的内部keyword字段
                .endObject()
                .endObject()
                .endObject()

                .startObject("createTime")
                .field("type","date")
                .endObject()

                .startObject("author")
                .field("type","text")
                .startObject("fields")
                .startObject("keyword")
                .field("type","keyword")
                .endObject()
                .endObject()
                .endObject()

                .startObject("title")
                .field("type","text")
                .startObject("fields")
                .startObject("keyword")
                .field("type","keyword")
                .endObject()
                .endObject()
                .endObject()

                .startObject("articleID")
                .field("type","text")
                .startObject("fields")
                .startObject("keyword")
                .field("type","keyword")
                .endObject()
                .endObject()
                .endObject();

        mapping = mapping.endObject().endObject();
        return mapping;
    }


}
