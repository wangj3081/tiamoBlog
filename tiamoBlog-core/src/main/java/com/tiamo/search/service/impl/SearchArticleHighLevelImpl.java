package com.tiamo.search.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tiamo.entity.BlogEntity;
import com.tiamo.search.dto.request.BlogRequest;
import com.tiamo.search.service.SearchArticle;
import com.tiamo.util.EsClient;
import com.tiamo.util.EsRHLClient;
import com.tiamo.util.EsRestHLClientUtil;
import com.tiamo.util.EsUtil;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 *  ES Rest High Level API 实现
 * Created by wangjian on 2019/3/9.
 */
@Service
public class SearchArticleHighLevelImpl implements SearchArticle {


    private Logger logger = LoggerFactory.getLogger(SearchArticleHighLevelImpl.class);


    @Override
    public List<BlogEntity> queryByAuther(BlogRequest request) {
        logger.debug("【根据作者获取文章列表】: {}", JSONObject.toJSONString(request));
        List<BlogEntity> result = new ArrayList<>();
        RestHighLevelClient client = EsRHLClient.getEsClient();

        String author = request.getAuthor(); // 作者
        SearchRequest searchRequest = new SearchRequest();
//        searchRequest.indices(author); // 索引名
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        TermQueryBuilder termQuery = new TermQueryBuilder("author", author); // 查找作者的名称,精确查找
        QueryBuilder multiMatchQuery = new MultiMatchQueryBuilder(author, "author", "otherName");
//        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//        boolQueryBuilder.must(termQuery);
        int from = request.getPage() - 1;
        int size = request.getSize();
        searchSourceBuilder.query(multiMatchQuery).from(from).size(size)  // 设置分页
                .sort("createTime", SortOrder.DESC); // 按时间倒序
        searchRequest.source(searchSourceBuilder); // 设置搜索条件
        try {
            System.out.println(searchSourceBuilder.toString());
            SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
            result = getSearchResultList(search, BlogEntity.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    @Override
    public List<BlogEntity> queryByContext(String contextStr) {
        List<BlogEntity> result = new ArrayList<>();
        RestHighLevelClient client = EsRHLClient.getEsClient();
        SearchRequest searchRequest = new SearchRequest();
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("context",contextStr);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(matchQueryBuilder).fetchSource(new String[]{"id","title","author","context"},null); // 只展示 source 中 context 与 title 两个字段
        searchRequest.source(sourceBuilder);
        System.out.println("DSL:" + searchRequest.source().toString());
        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
            result = getSearchResultList(response, BlogEntity.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public BlogEntity queryByArticleId(String articleId, String indexName) {
        logger.debug("【查询指定索引中的指定文章】: {}、{}", articleId, indexName);
        RestHighLevelClient client = EsRHLClient.getEsClient();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexName); // 索引名称
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("articleId.keyword", articleId); // 只有 mapping 中将该字段添加 keyword 类型
        sourceBuilder.query(matchQueryBuilder);
        searchRequest.source(sourceBuilder);
        System.out.println("DSL: " + sourceBuilder.toString());
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            List<BlogEntity> searchResultList = getSearchResultList(searchResponse, BlogEntity.class);
            if (!CollectionUtils.isEmpty(searchResultList)) {
                return searchResultList.get(0); // 只有一个
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insertArticle(List<BlogEntity> list,String indexName, String type) {
        logger.debug("【批量写入文章】:{}", JSONObject.toJSONString(list));

        try {
            if (!EsRestHLClientUtil.indexIsExists(indexName)) { // 索引不存在才创建
                EsRestHLClientUtil.createIndexTeample(indexName, getMapping(), type);
            }
//            if (!EsRestHLClientUtil.indexIsExists(indexName)) { // 不存在则创建
//                EsRestHLClientUtil.createIntex(indexName, getMapping(), type);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (BlogEntity blogEntity : list) {
            IndexRequest indexRequest = new IndexRequest(indexName, type, UuidUtil.getTimeBasedUuid().toString());
            String text = JSONObject.toJSONString(blogEntity);
            indexRequest.source(JSONObject.parseObject(text));
            EsRestHLClientUtil.getBulkProcessor().add(indexRequest);
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

                .startObject("otherName")
                .field("type","text")
                .field("analyzer","ik_max_word")
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


    /**
     *
     * @param search 搜索返回结果项
     * @param resultClass 需要转换的 class 对象
     * @param <T>  泛型类
     * @return
     */
    private <T> List<T>  getSearchResultList(SearchResponse search, Class<T> resultClass) {
        List<T> result = new ArrayList<>();
        if (search != null) {
            SearchHit[] hits = search.getHits().getHits();
            for (SearchHit hit : hits) {
                T entity = JSONArray.parseObject(hit.getSourceAsString(), resultClass);
                result.add(entity);
            }
        }
        return result;
    }

}
