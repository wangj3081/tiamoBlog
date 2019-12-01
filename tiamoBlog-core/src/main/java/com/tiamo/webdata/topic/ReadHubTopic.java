package com.tiamo.webdata.topic;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * readhub 主题
 */
public enum ReadHubTopic {
  NEWS("news","科技动态"),
  TOPIC("topic", "热门话题"),
  TECHNEWS("technews", "开发者资讯"),
  BLOCKCHAIN("blockchain", "区块链快讯");
//  JOBS("jobs", "招聘行情"); // readhub 关掉了这个数据源

  private String code;

  private String text;

  public static final Map<String, ReadHubTopic> mapReadHub = new HashMap<>();

  static {
    EnumSet<ReadHubTopic> readHubTopics = EnumSet.allOf(ReadHubTopic.class);
    readHubTopics.forEach(entity->{
      mapReadHub.put(entity.getCode(), entity);
    });
  }

  ReadHubTopic(String code, String text) {
    this.code = code;
    this.text = text;
  }

  public String getCode() {
    return code;
  }

  public String getText() {
    return text;
  }
}
