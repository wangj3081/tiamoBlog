package com.tiamo.search.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tiamo.entity.ReadHubNewsEntity;
import com.tiamo.entity.ReadHubTopicEntity;
import com.tiamo.es.index.ReadHubIndex;
import com.tiamo.search.service.SearchReadHubService;
import com.tiamo.util.DateUtil;
import com.tiamo.util.EsRHLClient;
import com.tiamo.util.EsRestHLClientUtil;
import com.tiamo.util.EsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.script.mustache.SearchTemplateRequest;
import org.elasticsearch.script.mustache.SearchTemplateResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.support.ValueType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * readhub 新闻搜索服务
 * @author wangjian
 * @version 1.0
 * @see com.tiamo.search.service.impl.SearchReadHubServiceImpl
 * @since JDK1.8
 */
@Slf4j
@Service
public class SearchReadHubServiceImpl implements SearchReadHubService {


    @Override
    public <T> Map<String, Object> queryByReadHubNews(JSONObject jsonObject, Class<T> clazz) {
        RestHighLevelClient client = EsRHLClient.getEsClient();
        Map<String, Object> resultMap = new HashMap<>();
        // 获取索引主题
        String topic = jsonObject.getString("topic");
        // 搜索消息
        String message = jsonObject.getString("message");
        // 请求页码
        Integer pageSize = jsonObject.getInteger("pageSize");
        Integer size = 3;
        if (pageSize == 1) {
            size = 50;
        }
        ReadHubIndex readHubIndex = ReadHubIndex.mapReadhubTopic.get(topic);
        if (readHubIndex == null) {
            return resultMap;
        }
        // 搜索模板
//        SearchTemplateRequest templateRequest = new SearchTemplateRequest();
        // 搜索语句请求
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(readHubIndex.getIndex());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        // 全文检索新闻内容,Operator.AND 分词后的内容都包含
        MatchQueryBuilder matchQuerySummary = new MatchQueryBuilder("summary",message).operator(Operator.OR).boost(10);
        boolQuery.should(matchQuerySummary);
        // 短语检索新闻标题
        MatchPhraseQueryBuilder phraseQueryBuilder = new MatchPhraseQueryBuilder("title", message).boost(50);
        boolQuery.should(phraseQueryBuilder);
        sourceBuilder.query(boolQuery).from((pageSize-1) * size).size(size);
        // 设置聚合处理,按作者分组统计, 第一个参数为分组的组名，第二个参数为每个组的 Key 的数据类型
        TermsAggregationBuilder groupAggregation = new TermsAggregationBuilder("authorGroup", ValueType.STRING)
                .field("siteName").size(10);
        sourceBuilder.aggregation(groupAggregation);
        searchRequest.source(sourceBuilder);
        log.info("DSL:{}", sourceBuilder.toString());
        // 往模板中设置搜索请求
//        templateRequest.setRequest(searchRequest);
        try {
//            SearchTemplateResponse response = client.searchTemplate(templateRequest, RequestOptions.DEFAULT);
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            // 返回内容
            List<T> searchResultList = EsRestHLClientUtil.getSearchResultList(response, clazz);
            // 聚合内容
            Aggregations aggregations = response.getAggregations();
            ParsedStringTerms authorGroup = aggregations.get("authorGroup");
            // 获取到集合
            List<? extends Terms.Bucket> bucketList = authorGroup.getBuckets();
//            log.info("【完整内容】:{}", JSONObject.toJSONString(authorGroup));
            Map<String, Long> aggMap = new HashMap<>();
            for ( Terms.Bucket bucket : bucketList) {
//                log.info("value:{}, name:{}, type:{}",JSONObject.toJSONString(bucket.getDocCount()), bucket.getKeyAsString());
//                log.info("buket: {}", ReflectionToStringBuilder.toString(bucket));
                aggMap.put(bucket.getKeyAsString(), bucket.getDocCount());
            }
            // 符合条件的总条数
            long total = response.getHits().getTotalHits().value;
            resultMap.put("listVal", searchResultList);
            resultMap.put("aggs", aggMap);
            resultMap.put("total", total);
//            log.info("【聚合内容】:{}", JSONObject.toJSONString(mapAggregation));
            return resultMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JSONArray  queryTitleList(String title) {
        log.debug("【获取标题列表集合入参】:{}", title);
//        List<ReadHubNewsEntity> result = new ArrayList<>();
        JSONArray result = new JSONArray();
        RestHighLevelClient client = EsRHLClient.getEsClient();
        Set<String> keySet = ReadHubIndex.mapReadhubIndex.keySet();
        // 只搜索指定的索引集合
        String[] indexs = keySet.toArray(new String[keySet.size()]);
        SearchRequest searchRequest = new SearchRequest(indexs);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MatchPhraseQueryBuilder phraseQueryBuilder = new MatchPhraseQueryBuilder("title", title);
        // 只返回 title 字段，其他字段为 null
        sourceBuilder.query(phraseQueryBuilder).from(0).size(10).fetchSource(new String[]{"title"}, null);
        searchRequest.source(sourceBuilder);
        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            if (response.getHits() != null) {
                SearchHit[] searchHits = response.getHits().getHits();
                for (SearchHit searchHit : searchHits) {
                    String index = searchHit.getIndex();
                    JSONObject object = new JSONObject();
                    ReadHubIndex readHubIndex = ReadHubIndex.mapReadhubIndex.get(index);
                    object.put("topic", readHubIndex.getTopic());
                    object.put("value", searchHit.getSourceAsMap().get("title"));
                    result.add(object);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public <T> Map<String, Object> queryByTopicNewsLast(String topic, Class<T> clazz) {
        log.debug("【传入话题】: {}", topic);
        HashMap<String, Object> resultMap = new HashMap<>();
        // 获取指定索引
        ReadHubIndex readHubIndex = ReadHubIndex.mapReadhubTopic.get(topic);
        if (readHubIndex == null) {
            return resultMap;
        }
        RestHighLevelClient client = EsRHLClient.getEsClient();
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        searchRequest.indices(readHubIndex.getIndex());
        RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder("publishDate");
        DateTime dateTime = DateTime.now().plusMonths(-1);
        String dateStr = DateUtil.formatToStr(dateTime.toDate(), DateUtil.DEFAULT_FORMAT);
        rangeQueryBuilder.gte(dateStr);
        sourceBuilder.query(rangeQueryBuilder).sort("publishDate", SortOrder.DESC); // 按发布时间排倒序

      // 设置聚合处理,按作者分组统计, 第一个参数为分组的组名，第二个参数为每个组的 Key 的数据类型
        TermsAggregationBuilder groupAggregation = new TermsAggregationBuilder("siteNameGroup", ValueType.STRING)
          .field("siteName").size(10);
        sourceBuilder.aggregation(groupAggregation);
        log.info(sourceBuilder.toString());
        searchRequest.source(sourceBuilder);
        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            // 返回内容
            List<T> searchResultList = EsRestHLClientUtil.getSearchResultList(response, clazz);
            // 聚合内容
            Aggregations aggregations = response.getAggregations();
            ParsedStringTerms siteGroup = aggregations.get("siteNameGroup");
            // 获取到集合
            List<? extends Terms.Bucket> bucketList = siteGroup.getBuckets();
            Map<String, Long> aggMap = new HashMap<>();
            for ( Terms.Bucket bucket : bucketList) {
                aggMap.put(bucket.getKeyAsString(), bucket.getDocCount());
            }
            // 符合条件的总条数
            long total = response.getHits().getTotalHits().value;
            resultMap.put("listVal", searchResultList);
            resultMap.put("aggs", aggMap);
            resultMap.put("total", total);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    /*@Override
    public Map<String, Object> queryByTopicNewsLast(JSONObject jsonObject) {
        log.debug("【获取热门话题的数据信息】:{}", JSONObject.toJSONString(jsonObject));
        RestHighLevelClient client = EsRHLClient.getEsClient();
        Map<String, Object> resultMap = new HashMap<>();
        // 获取索引主题
        String topic = jsonObject.getString("topic");
        // 搜索消息
        String message = jsonObject.getString("message");
        // 请求页码
        Integer pageSize = jsonObject.getInteger("pageSize");
        Integer size = 3;
        if (pageSize == 1) {
            size = 50;
        }
        ReadHubIndex readHubIndex = ReadHubIndex.mapReadhubTopic.get(topic);
        if (readHubIndex == null) {
            return resultMap;
        }

        // 搜索语句请求
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(readHubIndex.getIndex());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        // 全文检索新闻内容,Operator.AND 分词后的内容都包含
        MatchQueryBuilder matchQuerySummary = new MatchQueryBuilder("summary",message).operator(Operator.OR).boost(10);
        boolQuery.should(matchQuerySummary);
        // 短语检索新闻标题
        MatchPhraseQueryBuilder phraseQueryBuilder = new MatchPhraseQueryBuilder("title", message).boost(50);
        boolQuery.should(phraseQueryBuilder);
        sourceBuilder.query(boolQuery).from((pageSize-1) * size).size(size);
        // 设置聚合处理,按作者分组统计, 第一个参数为分组的组名，第二个参数为每个组的 Key 的数据类型
        TermsAggregationBuilder groupAggregation = new TermsAggregationBuilder("authorGroup", ValueType.STRING)
                .field("siteName").size(10);
        sourceBuilder.aggregation(groupAggregation);
        searchRequest.source(sourceBuilder);
        log.info("DSL:{}", sourceBuilder.toString());
        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            // 搜索返回结果
            List<ReadHubTopicEntity> searchList = EsRestHLClientUtil.getSearchResultList(response, ReadHubTopicEntity.class);
            // 聚合内容
            Aggregations aggregations = response.getAggregations();
            ParsedStringTerms authorGroup = aggregations.get("authorGroup");
            // 获取到集合
            List<? extends Terms.Bucket> bucketList = authorGroup.getBuckets();
            Map<String, Long> aggMap = new HashMap<>();
            for ( Terms.Bucket bucket : bucketList) {
                aggMap.put(bucket.getKeyAsString(), bucket.getDocCount());
            }
            // 符合条件的总条数
            long total = response.getHits().getTotalHits().value;
            resultMap.put("listVal", searchList);
            resultMap.put("aggs", aggMap);
            resultMap.put("total", total);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultMap;
    }*/
}
