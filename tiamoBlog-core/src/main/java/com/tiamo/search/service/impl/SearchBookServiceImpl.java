package com.tiamo.search.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tiamo.entity.BookMappingEntity;
import com.tiamo.search.service.SearchBookService;
import com.tiamo.util.EsRHLClient;
import com.tiamo.util.EsRestHLClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.search.MatchQuery;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * @Auther: wangjian
 */
@Slf4j
@Service
public class SearchBookServiceImpl implements SearchBookService {

    @Resource
    private SearchBookService bookService;

    private final String INDEX_NAME = "book";

    @PostConstruct
    public void  test() {
        log.debug("【初始化完毕】");

    }

    @Override
    public List<BookMappingEntity> searchBookForTitle(String title) {
        log.debug("【根据图书标题搜索图书】入参: {}", title);
        RestHighLevelClient client = EsRHLClient.getEsClient();
        SearchRequest searchRequest = new SearchRequest();
        try {
            if (StringUtils.isBlank(title)) {
                return new ArrayList<>(); // 返回空内容
            }
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder(); // 搜索语句构建
            MatchQueryBuilder matchQuery = new MatchQueryBuilder("title",title); // 搜索查询方式构建
            matchQuery.zeroTermsQuery(MatchQuery.ZeroTermsQuery.NONE);
            matchQuery.analyzer("ik_max_word");
            TermsAggregationBuilder termsAggregation = AggregationBuilders.terms("groupType").field("type"); // 按书籍文件类型进行聚合分组
//            ValueCountAggregationBuilder type = AggregationBuilders.count("groupType").field("type"); // 下钻继续分组
//            termsAggregation.subAggregation(type);
            sourceBuilder.query(matchQuery).aggregation(termsAggregation);
            searchRequest.source(sourceBuilder);
            System.out.println("DSL:"+ sourceBuilder.toString());
            SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
            List<BookMappingEntity> resultList = EsRestHLClientUtil.getSearchResultList(search, BookMappingEntity.class);
            return resultList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<BookMappingEntity> searchBookForTitle(String title, String type) {
        return null;
    }

    @Override
    public boolean insertBookList(List<BookMappingEntity> bookList) {
        log.debug("【写入的图书信息数据】: {}", JSONObject.toJSONString(bookList));
        if (!EsRestHLClientUtil.indexIsExists(INDEX_NAME)) {
            try {
//                EsRestHLClientUtil.createIndexTeample(INDEX_NAME, getMapping(), "book");
                EsRestHLClientUtil.createIntex(INDEX_NAME, getMapping(), INDEX_NAME);
            } catch (IOException e) {
                log.error("【启动异常】: {}", e);
                e.printStackTrace();
            }
        }
        if (!CollectionUtils.isEmpty(bookList)) {
//            BulkProcessor bulkProcessor = EsRestHLClientUtil.getBulkProcessor();
            for (BookMappingEntity entity : bookList) {
                IndexRequest request = new IndexRequest(INDEX_NAME, "book", UUID.randomUUID().toString());
                String sourceValue = JSONObject.toJSONString(entity);
                request.source(JSONObject.parseObject(sourceValue));
                EsRestHLClientUtil.getBulkProcessor().add(request);
            }
            return true;
        }
        return false;
    }


    // 创建Mapping
    public static XContentBuilder getMapping() throws IOException {
        XContentBuilder mapping;
        mapping = jsonBuilder()
                .startObject()
                .startObject("properties")
                .startObject("title")
                .field("type","text")
                .field("fielddata",true)
                .field("analyzer","ik_max_word")
                .endObject()

                .startObject("linkAddress") // 链接地址
                .field("type","text")
                .endObject()

                .startObject("author") // 作者
                .field("type","text")
                .field("analyzer", "ik_smart")
                .startObject("fields")
                .startObject("keyword")
                .field("type","keyword")
                .endObject()
                .endObject()
                .endObject()

                .startObject("introduction") // 内容简介
                .field("type","text")
                .field("analyzer","ik_smart")
                .endObject()

                .startObject("createTime")
                .field("type","date")
                .endObject()

                .startObject("type") // 文件类型
                .field("type","text")
                .field("fielddata",true)
                .field("analyzer","ik_smart")
                .startObject("fields")
                .startObject("keyword")
                .field("type","keyword")
                .endObject()
                .endObject()
                .endObject()

                .startObject("sourceInformation") // 信息来源
                .field("type","text")
                .endObject();

        mapping = mapping.endObject().endObject();
        return mapping;
    }

}
