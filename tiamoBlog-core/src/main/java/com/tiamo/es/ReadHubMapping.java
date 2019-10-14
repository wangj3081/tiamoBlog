package com.tiamo.es;

/**
 * 创建 readhub 索引 mapping
 * @author wangjian
 * @version 1.0
 * @see ReadHubMapping
 * @since JDK1.8
 */
public class ReadHubMapping {

    // news
    /**
     PUT /readhub_news
     {
     "settings": {
     "refresh_interval": "2s",
     "translog.durability": "async",
     "translog.sync_interval": "100s",
     "index":{
     "number_of_shards": 5,
     "number_of_replicas": 0,
     "max_result_window": 100000
     }
     },
     "mappings": {
     "properties": {
     "id":{
     "store": true,
     "type": "long"
     },
     "summary":{
     "store": true,
     "type": "text",
     "analyzer": "ik_smart"
     },
     "title":{
     "store": true,
     "type": "text",
     "analyzer": "ik_smart",
     "fields": {
     "keyword":{
     "type":"keyword",
     "ignore_above": 256
     }
     }
     },
     "summaryAuto":{
     "store": true,
     "type": "text",
     "analyzer": "ik_smart"
     },
     "url":{
     "store": true,
     "type": "keyword"
     },
     "mobileUrl":{
     "store": true,
     "type": "keyword"
     },
     "siteName": {
     "store": true,
     "type": "keyword"
     },
     "language":{
     "store": true,
     "type": "keyword"
     },
     "authorName":{
     "store": true,
     "type": "keyword"
     },
     "pulishDate":{
     "store": true
     , "type": "date"
     , "format": ["yyyy-MM-dd HH:mm:ss,yyyy-MM-dd"]
     }

     }
     }
     }
     */
}
