package com.tiamo.search.dto.vo;

import com.tiamo.entity.ReadHubNewsEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * Readhub 热点话题返回对象
 * @author wangjian
 * @version 1.0
 * @see com.tiamo.search.dto.vo.ReadHubTopicVo
 * @since JDK1.8
 */
@Data
public class ReadHubTopicVo implements Serializable {
    /**
     * 热点新闻的 ID 拼接上 readhub 的 url 实际就是该新闻的相关详情内容
     * 该条新闻 ID
     */
    private String id;
    /**
     * 标题
     */
    private String title;
    /**
     * 网站名称
     */
    private String siteName;

    /**
     * 总内容
     */
    private String summary;
    /**
     * pc 链接
     */
    private String url;
    /**
     * 媒体新闻
     */
    private List<ReadHubNewsEntity>  newsArray;

    /**
     * 发布日期
     */
    private String publishDate;
    /**
     * 创建日期
     */
    private String createdAt;
    /**
     * 时间线
     */
    private String timeline;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
