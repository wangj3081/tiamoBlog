package com.tiamo.es;

/**
 * 创建 readhub 索引 mapping
 *
 * @author wangjian
 * @version 1.0
 * @see com.tiamo.es.ReadHubMapping
 * @since JDK1.8
 */
public class ReadHubMapping {

    // 科技新闻: news
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
     "publishDate":{
     "store": true
     , "type": "date"
     , "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
     }

     }
     }
     }
     */

    //  开发者资讯:technews
    /**

     PUT /readhub_technews
     {
     "settings": {
     "refresh_interval": "2s",
     "translog.durability": "async",
     "translog.sync_interval": "100s",
     "index": {
     "number_of_shards": 5,
     "number_of_replicas": 0,
     "max_result_window": 100000
     }
     },
     "mappings": {
     "properties": {
     "id": {
     "store": true,
     "type": "long"
     },
     "summary": {
     "store": true,
     "type": "text",
     "analyzer": "ik_smart"
     },
     "title": {
     "store": true,
     "type": "text",
     "analyzer": "ik_smart",
     "fields": {
     "keyword": {
     "type": "keyword",
     "ignore_above": 256
     }
     }
     },
     "summaryAuto": {
     "store": true,
     "type": "text",
     "analyzer": "ik_smart"
     },
     "url": {
     "store": true,
     "type": "keyword"
     },
     "mobileUrl": {
     "store": true,
     "type": "keyword"
     },
     "siteName": {
     "store": true,
     "type": "keyword"
     },
     "language": {
     "store": true,
     "type": "keyword"
     },
     "authorName": {
     "store": true,
     "type": "keyword"
     },
     "publishDate": {
     "store": true,
     "type": "date",
     "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"

     }
     }
     }
     }
     */

    // 热门话题:topic，mapping
    /**
     PUT /readhub_topic
     {
     "settings": {
     "refresh_interval": "2s",
     "translog.durability": "async",
     "translog.sync_interval": "100s",
     "index": {
     "number_of_shards": 5,
     "number_of_replicas": 0,
     "max_result_window": 100000
     }
     },
     "mappings": {
     "properties": {
     "id": {
     "store": true,
     "type": "text"
     },
     "summary": {
     "store": true,
     "type": "text",
     "analyzer": "ik_smart"
     },
     "title": {
     "store": true,
     "type": "text",
     "analyzer": "ik_smart",
     "fields": {
     "keyword": {
     "type": "keyword",
     "ignore_above": 256
     }
     }
     },
     "url": {
     "store": true,
     "type": "keyword"
     },
     "siteName": {
     "store": true,
     "type": "keyword"
     },
     "publishDate": {
     "store": true,
     "type": "date",
     "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
     },
     "createdAt": {
     "store": true,
     "type": "date",
     "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
     },
     "timeline": {
     "store": true,
     "type": "keyword"
     },
     "newsArray":{
     "properties": {
     "id":{
     "type":"long",
     "store": true
     },
     "url":{
     "type":"keyword",
     "store": true
     },
     "title":{
     "type":"keyword",
     "store": true
     },
     "siteName":{
     "type":"keyword",
     "store": true
     },
     "mobileUrl":{
     "type":"keyword",
     "store": true
     },
     "autherName":{
     "type":"keyword",
     "store": true
     },
     "duplicateId":{
     "type":"long",
     "store": true
     },
     "publishDate":{
     "type":"date",
     "store": true,
     "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
     },
     "language":{
     "type":"keyword",
     "store": true
     },
     "statementType":{
     "type":"long",
     "store": true
     }

     }
     }
     }
     }
     }
     */
}
