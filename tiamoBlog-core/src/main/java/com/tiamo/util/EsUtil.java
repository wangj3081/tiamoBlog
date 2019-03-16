package com.tiamo.util;

import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequestBuilder;
//import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by wangjian on 2019/3/10.
 */
public class EsUtil {

    private static Client client;
    private static BulkProcessor processor;

    static {
//        client = EsClient.getEsClient();
    }

    /**
     *  创建索引模板并设置mapping
     * @param templateName 索引类型, topic名称
     * @param mapping 要创建的mapping对象
     * @param type mapping 类型，一个mapping只能有一种类型
     */
    public static void createIndexTeample(String templateName, XContentBuilder mapping, String type){

        Settings settings = Settings.builder()
                .put("index.number_of_shards",3) // 分片大小,默认为5
                .put("index.max_result_window",100000)   //  查询索引返回的内容长度限制,默认为1W
                .put("number_of_replicas", 0) // 设置副本数
                .build();
        // 设置模板
/*      貌似不管用
        PutIndexTemplateRequest source = new PutIndexTemplateRequest(templateName).source(mapping);
        source.settings(settings);
        PutIndexTemplateResponse response = client.admin().indices().putTemplate(source).actionGet();*/
//        PutIndexTemplateRequestBuilder template = client.admin().indices().preparePutTemplate(templateName).setTemplate(templateName+"-*");
        PutIndexTemplateRequestBuilder template = client.admin().indices().preparePutTemplate(templateName).setTemplate(templateName);
//        template.setSettings(settings).addMapping(templateName, mapping);
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> enabled = new HashMap<>();
        enabled.put("enabled", false);
        map.put("_all",enabled);
        template.setSettings(settings).addMapping(type,mapping).addMapping("_default_").addMapping("_default_",map);
        template.execute().actionGet();
    }


    // 获取批量对象
    public static BulkProcessor getBulkProcessor(){
        Client esClient = EsClient.getEsClient();

        if(processor==null) {
            BulkProcessor.Builder builder = BulkProcessor.builder(esClient, new BulkProcessor.Listener() {
                @Override
                public void beforeBulk(long l, BulkRequest bulkRequest) {

                }

                @Override
                public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {

                }

                @Override
                public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {

                }
            }).setBulkActions(5000) // 超过5000条提交
                    .setFlushInterval(new TimeValue(2)) // 插入3秒后开始提交
                    .setBulkSize(new ByteSizeValue(10, ByteSizeUnit.MB))// 超过10MB数据
                    .setConcurrentRequests(5); // 同时可以有5个并发
            processor = builder.build();
        }
        return processor;
    }


}
