package com.tiamo.es;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tiamo.entity.ReadHubNewsEntity;
import com.tiamo.entity.ReadHubTechnewsEntity;
import com.tiamo.entity.ReadHubTopicEntity;
import com.tiamo.es.index.ReadHubIndex;
import com.tiamo.redis.RedisQueue;
import com.tiamo.util.DateUtil;
import com.tiamo.util.EsRHLClient;
import com.tiamo.util.EsRestHLClientUtil;
import com.tiamo.util.ObjectUtils;
import com.tiamo.webdata.topic.ReadHubTopic;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexAction;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 *
 * 读取 redis 队列数据到 ES
 * @author wangjian
 * @version 1.0
 * @see com.tiamo.es.InsertDataToEsFromQueue
 * @since JDK1.8
 */
@Component
@Slf4j
public class InsertDataToEsFromQueue {

    @Resource
    private RedisQueue redisQueue;

    /**
     * 写入数据
     * @param topic 要读取的队列
     */
    public void insertData(String topic) {

        StopWatch stopWatch = new StopWatch("InsertDataToEsFromQueue:insertData");
        stopWatch.start("insertData");
        switch (topic){
            case "news":
                readReadhubNewsQueue();
                break;
            case "technews":
                readReadhubTechNewsQueue();
                break;
            case "blockchain":
                readReadhubBlockchainQueue();
                break;
            case "topic":
                readReadhubTopicQueue();
                break;
            default:
                break;
        }
        stopWatch.stop();
        stopWatch.prettyPrint();

    }

    /**
     * 读取 readhub 的 news 队列，写入到 es 的 readhub_news 索引之中
     */
    private void readReadhubNewsQueue() {
        // 索引名
        String indexName = "readhub_news";
        String docType = "_doc";
        // 在队列中存在的总消息数
        Long length = redisQueue.queueLen(ReadHubTopic.NEWS.getCode());
        for (int i = 0; i < length; i++) {
            // 取到一条队列记录,内容是一个data的JSON数组
            String newsValue = redisQueue.readQueue(ReadHubTopic.NEWS.getCode());
            JSONObject newsJson = JSONObject.parseObject(newsValue);
            JSONArray data = newsJson.getJSONArray("data");
            if (data != null && data.size() > 0) {
                // 获取到在队列中的数据列表
                List<ReadHubNewsEntity> readHubNewsList = JSONArray.parseArray(JSONObject.toJSONString(data), ReadHubNewsEntity.class);
                readHubNewsList.forEach(entity->{
                    IndexRequest indexRequest = new IndexRequest(indexName, docType, entity.getId().toString());
                    try {
                        Date date = DateUtil.formatToDate(entity.getPublishDate(), DateUtil.UTC_FORMAT);
                        String dateValue = DateUtil.formatToStr(date, DateUtil.DEFAULT_FORMAT);
                        entity.setPublishDate(dateValue);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        log.error("【日期格式转换错误】:{}", e);
                    }
                    Map<String, Object> source = ObjectUtils.toMap(entity);
                    indexRequest.source(JSONObject.parseObject(JSONObject.toJSONString(entity)));
                    EsRestHLClientUtil.getBulkProcessor().add(indexRequest);
                });
                log.info("【索引:{},插入数据任务已提交】", indexName);
            }
        }
    }
    /**
     * 读取 readhub 的 techNews 队列，写入到 es 的 readhub_news 索引之中
     */
    private void readReadhubTechNewsQueue() {
        // 索引名
        String indexName = ReadHubIndex.READHUB_TECHNEWS.getIndex();
        String docType = "_doc";
        // 在队列中存在的总消息数
        Long length = redisQueue.queueLen(ReadHubTopic.TECHNEWS.getCode());
        for (int i = 0; i < length; i++) {
            // 取到一条队列记录,内容是一个data的JSON数组
            String newsValue = redisQueue.readQueue(ReadHubTopic.TECHNEWS.getCode());
            JSONObject newsJson = JSONObject.parseObject(newsValue);
            JSONArray data = newsJson.getJSONArray("data");
            if (data != null && data.size() > 0) {
                // 获取到在队列中的数据列表
                List<ReadHubTechnewsEntity> readHubNewsList = JSONArray.parseArray(JSONObject.toJSONString(data), ReadHubTechnewsEntity.class);
                readHubNewsList.forEach(entity->{
                    IndexRequest indexRequest = new IndexRequest(indexName, docType, entity.getId().toString());
                    try {
                        Date date = DateUtil.formatToDate(entity.getPublishDate(), DateUtil.UTC_FORMAT);
                        String dateValue = DateUtil.formatToStr(date, DateUtil.DEFAULT_FORMAT);
                        entity.setPublishDate(dateValue);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        log.error("【日期格式转换错误】:{}", e);
                    }
                    JSONObject source = JSONObject.parseObject(JSONObject.toJSONString(entity));
                    indexRequest.source(source);
                    EsRestHLClientUtil.getBulkProcessor().add(indexRequest);
                });
                log.info("【索引:{},插入数据任务已提交】", indexName);
            }
        }
    }

    /**
     * 读取 readhub 的 blockchain 队列，写入到 es 的 readhub_blockchain 索引之中
     */
    private void readReadhubBlockchainQueue() {
        // 索引名
        String indexName = ReadHubIndex.READHUB_BLOCKCHAIN.getIndex();
        String docType = "_doc";
        // 在队列中存在的总消息数
        Long length = redisQueue.queueLen(ReadHubTopic.BLOCKCHAIN.getCode());
        for (int i = 0; i < length; i++) {
            // 取到一条队列记录,内容是一个data的JSON数组
            String newsValue = redisQueue.readQueue(ReadHubTopic.BLOCKCHAIN.getCode());
            JSONObject newsJson = JSONObject.parseObject(newsValue);
            JSONArray data = newsJson.getJSONArray("data");
            if (data != null && data.size() > 0) {
                // 获取到在队列中的数据列表
                List<ReadHubTechnewsEntity> readHubNewsList = JSONArray.parseArray(JSONObject.toJSONString(data), ReadHubTechnewsEntity.class);
                readHubNewsList.forEach(entity->{
                    IndexRequest indexRequest = new IndexRequest(indexName, docType, entity.getId().toString());
                    try {
                        Date date = DateUtil.formatToDate(entity.getPublishDate(), DateUtil.UTC_FORMAT);
                        String dateValue = DateUtil.formatToStr(date, DateUtil.DEFAULT_FORMAT);
                        entity.setPublishDate(dateValue);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        log.error("【日期格式转换错误】:{}", e);
                    }
                    JSONObject source = JSONObject.parseObject(JSONObject.toJSONString(entity));
                    indexRequest.source(source);
                    EsRestHLClientUtil.getBulkProcessor().add(indexRequest);
                });
                log.info("【索引:{},插入数据任务已提交】", indexName);
            }
        }
    }

    /**
     * 读取 readhub 热点新闻「topic」的数据队列，写入到 readhub_topic 索引中
     */
    private void readReadhubTopicQueue() {
        // 索引名
        String indexName = ReadHubIndex.READHUB_TOPIC.getIndex();
        // 索引类型
        String type = "_doc";
        String url = "https://readhub.cn/topic/";
        // 总数据批次数
        Long size = redisQueue.queueLen(ReadHubTopic.TOPIC.getCode());
        for (int i = 0; i < size; i++) {
            // 读取一条数据内容
            String dataJson = redisQueue.readQueue(ReadHubTopic.TOPIC.getCode());
            JSONObject data = JSONObject.parseObject(dataJson);
            JSONArray dataJSONArray = data.getJSONArray("data");
            if (dataJSONArray != null && dataJSONArray.size() > 0) {
                List<ReadHubTopicEntity> readhubTopicList = JSONArray.parseArray(JSONObject.toJSONString(dataJSONArray), ReadHubTopicEntity.class);
                readhubTopicList.forEach(entity->{
                    try {
                        // 索引请求
                        IndexRequest indexRequest = new IndexRequest(indexName, type, entity.getId());

                        String createdDate = DateUtil.formatToStr(DateUtil.formatToDate(entity.getCreatedAt(), DateUtil.UTC_FORMAT), DateUtil.DEFAULT_FORMAT);
                        String publishDate = DateUtil.formatToStr(DateUtil.formatToDate(entity.getPublishDate(), DateUtil.UTC_FORMAT), DateUtil.DEFAULT_FORMAT);
                        List<ReadHubNewsEntity> newsArray = entity.getNewsArray();
                        for (ReadHubNewsEntity news:newsArray) {
                            String newsPublishDate = DateUtil.formatToStr(DateUtil.formatToDate(news.getPublishDate(), DateUtil.UTC_FORMAT), DateUtil.DEFAULT_FORMAT);
                            news.setPublishDate(newsPublishDate);
                        }
                        entity.setUrl(url+entity.getId());
                        entity.setCreatedAt(createdDate);
                        entity.setPublishDate(publishDate);
                        entity.setNewsArray(newsArray);

                        // 写入请求
                        JSONObject source = JSONObject.parseObject(JSONObject.toJSONString(entity));
                        indexRequest.source(source);
                        EsRestHLClientUtil.getBulkProcessor().add(indexRequest);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                });
                log.info("【索引:{},插入数据任务已提交】", indexName);
            }
        }
    }

}
