package com.tiamo.es.index;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * ReadHub 的 ES 索引
 * @author wangjian
 * @version 1.0
 * @see com.tiamo.es.index.ReadHubIndex
 * @since JDK1.8
 */
public enum ReadHubIndex {
    READHUB_NEWS("readhub_news", "news","科技动态"),
    READHUB_TOPIC("readhub_topic", "topic","热点新闻"),
    READHUB_TECHNEWS("readhub_technews", "technews","开发者资讯"),
    READHUB_BLOCKCHAIN("readhub_blockchain", "blockchain","区块链资讯");
    private String index;
    private String topic;
    private String text;

    public static final Map<String, ReadHubIndex>  mapReadhubIndex = new HashMap<>();
    public static final Map<String, ReadHubIndex>  mapReadhubTopic = new HashMap<>();
    static {
        EnumSet<ReadHubIndex> readHubIndices = EnumSet.allOf(ReadHubIndex.class);
        readHubIndices.forEach(entity->{
            mapReadhubIndex.put(entity.getIndex(), entity);
            mapReadhubTopic.put(entity.getTopic(), entity);
        });
    }

    ReadHubIndex(String index, String topic,String text) {
        this.index = index;
        this.topic = topic;
        this.text = text;
    }

    public String getIndex() {
        return index;
    }

    public String getTopic() {
        return topic;
    }

    public String getText() {
        return text;
    }
}
