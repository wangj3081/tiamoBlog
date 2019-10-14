package com.tiamo.es;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tiamo.entity.ReadHubNewsEntity;
import com.tiamo.redis.RedisQueue;
import com.tiamo.util.DateUtil;
import com.tiamo.util.EsRestHLClientUtil;
import com.tiamo.util.ObjectUtils;
import com.tiamo.webdata.topic.ReadHubTopic;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.springframework.stereotype.Component;

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
 * @see InsertDataToEsFromQueue
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
        switch (topic){
            case "news":
                readReadhubNewsQueue();
                break;
            default:
                break;
        }
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
                log.info("【索引:{},插入数据完成】", indexName);
            }
        }


    }

}
