package com.tiamo.search.dto.vo;

import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * 搜索结果返回
 * @author wangjian
 * @version 1.0
 * @see com.tiamo.search.dto.vo.ReadHubVo
 * @since JDK1.8
 */
@Data
public class ReadHubVo implements Serializable {

    /**
     * 该条新闻 ID
     */
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容摘要
     */
    private String summaryAuto;
    /**
     * 总内容
     */
    private String summary;
    /**
     * pc 链接
     */
    private String url;
    /**
     * 手机读取连接
     */
    private String mobileUrl;
    /**
     * 网站名称
     */
    private String siteName;
    /**
     * 使用语言
     */
    private String language;
    /**
     * 作者
     */
    private String authorName;
    /**
     * 发布日期
     */
    private String publishDate;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
