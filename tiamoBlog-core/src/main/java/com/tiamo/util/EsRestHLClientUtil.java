package com.tiamo.util;

import com.alibaba.fastjson.JSONArray;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequest;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;


/**
 * ES Rest 高阶 API 处理工具类
 * Created by wangjian on 2019/3/10.
 */
public class EsRestHLClientUtil {

    private static RestHighLevelClient client;

    private static BulkProcessor processor;

    private static Logger logger = LoggerFactory.getLogger(EsRestHLClientUtil.class);

    static {
        client = EsRHLClient.getEsClient();
    }

    /**
     * 判断索引是否存在
     * @param indexName 索引名
     * @return
     */
    public static boolean indexIsExists(String indexName) {
        GetIndexRequest request = new GetIndexRequest();
        request.indices(indexName);
        try {

            boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
            return exists;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建索引
     * @param indexName
     * @param mapping
     * @param type
     */
    public static void createIntex(String indexName,XContentBuilder mapping, String type) {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        request.mapping(type, mapping);
        Settings settings = Settings.builder()
                .put("index.number_of_shards",3) // 分片大小,默认为5
                .put("index.max_result_window",100000)   //  查询索引返回的内容长度限制,默认为1W
                .put("number_of_replicas", 0) // 设置副本数
                .build();
        request.settings(settings); // 设置特定属性
//        request.timeout(TimeValue.timeValueSeconds(2)); // 创建时间超过2秒超时
        try {
            System.out.println(request.toString());
            CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
            System.out.println(response.isAcknowledged()? "索引创建成功" : "索引创建失败");

            System.out.println(response.isShardsAcknowledged() ? "有节点创建超时":"全部成功");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     *  创建索引模板并设置通用 mapping
     * @param indexName 索引名称
     * @param mapping 要创建的mapping对象
     * @param type mapping 类型，一个mapping只能有一种类型
     */
    public static void createIndexTeample(String indexName, XContentBuilder mapping, String type){

        Settings settings = Settings.builder()
                .put("index.number_of_shards",3) // 分片大小,默认为5
                .put("index.max_result_window",10000)   //  查询索引返回的内容长度限制,默认为1W
                .put("number_of_replicas", 0) // 设置副本数
                .build();
        UpdateSettingsRequest settingsRequest = new UpdateSettingsRequest();
        settingsRequest.settings(settings);


        // 设置模板
        PutIndexTemplateRequest templateRequest = new PutIndexTemplateRequest(indexName+"*"); // 设置模板名称
        templateRequest.patterns(Arrays.asList("pattern-1", indexName +"*"));
        templateRequest.settings(settings); // 设置模板索引配置
        templateRequest.mapping(type, mapping); // 设置 mapping
        templateRequest.masterNodeTimeout(TimeValue.timeValueMinutes(1)); // 设置创建超时时间为 1 分钟
        try {
            AcknowledgedResponse response = client.indices().putTemplate(templateRequest, RequestOptions.DEFAULT); // 同步执行
            System.out.println(response.isAcknowledged() ? "创建Template成功":"创建Template失败");
            // 异步执行创建
            ActionListener<AcknowledgedResponse> listener = new  ActionListener<AcknowledgedResponse>(){

                @Override
                public void onResponse(AcknowledgedResponse rep) {
                    logger.debug("【异步创建模板:「{}」】成功", indexName);
                }

                @Override
                public void onFailure(Exception e) {
                    logger.error("【异步创建模板:「{}」】失败", indexName);
                    logger.error("异常信息:{}", e);
                }
            };
            client.indices().putTemplateAsync(templateRequest, RequestOptions.DEFAULT, listener);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        template.setSettings(settings).addMapping(templateName, mapping);
        /*Map<String, Object> map = new HashMap<>();
        Map<String, Object> enabled = new HashMap<>();
        enabled.put("enabled", false);
        map.put("_all",enabled);
        template.setSettings(settings).addMapping(type,mapping).addMapping("_default_").addMapping("_default_",map);
        template.execute().actionGet();*/
    }


    // 获取批量对象
    public static BulkProcessor getBulkProcessor() {

       if (processor == null) {

           BulkProcessor.Listener listener = new BulkProcessor.Listener() { // 1、 创建 BulkProcessor.Listener

               @Override
               public void beforeBulk(long l, BulkRequest bulkRequest) {  // 2、 在每次执行之前调用此方法 BulkRequest
                   int bynverIfActions = bulkRequest.numberOfActions();
                   logger.debug("使用{}请求批量执行[{}]",l, bynverIfActions);
               }

               @Override
               public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) { // 3、 每次执行后调用此方法
                    if (bulkResponse.hasFailures()) {
                        logger.warn("Bulk:[{}] 执行失败，" , l);
                    } else {
                        logger.debug("Bulk:[{}] 在{}毫秒内完成", l, bulkResponse.getTook().getMillis());
                    }
               }

               @Override
               public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) { // 4、 每次执行失败后调用此方法
                    logger.error("无法批量执行失败: {}", throwable);
               }
           };
//           RestHighLevelClient esClient = EsRHLClient.getEsClient();

           BiConsumer<BulkRequest, ActionListener<BulkResponse>> bulkConsumer = (request, bulkListener) -> client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener);
           processor = BulkProcessor.builder(bulkConsumer, listener)
                   .setFlushInterval(TimeValue.timeValueSeconds(2)) // 插入2秒后刷新提交， -1 禁用
                   .setBulkSize(new ByteSizeValue(10L, ByteSizeUnit.MB)) // 当写入的数据大于 10MB 后提交， -1 禁用
                   .setConcurrentRequests(0)   // 设置可以有5个并发请求，默认为1，设置为0时表示只能接收单个请求
                   .setBulkActions(5000)    // 超过5000条请求就提交，默认为 1000，设置 -1 为禁用
                   .build(); // 5、通过该RestHighLevelClient.bulkAsync() 方法将用于执行BulkRequest
       }

       return processor;
    }

    /**
     * 关闭批量请求处理器
     */
    public static void closeBulkProcessor() {
        if (processor != null) {
            processor.close();
            logger.debug("【闭批量请求处理器】");
            /*try {
                processor.awaitClose(30, TimeUnit.MICROSECONDS); // 30秒后关闭批量请求
                logger.debug("【30秒后关闭批量请求处理器】");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }

    /**
     *
     * @param search 搜索返回结果项
     * @param resultClass 需要转换的 class 对象
     * @param <T>  泛型类
     * @return
     */
    public static <T> List<T> getSearchResultList(SearchResponse search, Class<T> resultClass) {
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
