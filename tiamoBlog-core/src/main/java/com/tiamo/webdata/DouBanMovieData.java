package com.tiamo.webdata;

/**
 *
 * @author wangjian
 * @version 1.0
 * @see com.tiamo.webdata.DouBanMovieData
 * @since JDK1.8
 */
public class DouBanMovieData {
    /**
     可选 tag 「
         热门  最新  经典  可播放
         豆瓣高分  冷门佳片  华语
         欧美  韩国  日本  动作 喜剧
         爱情  科幻  悬疑  恐怖  治愈
     」
     可选 sort(排序) 「
        按评价：rank
        按热度: recommend
        按时间: time
     」
     */
    private static final String url = "https://movie.douban.com/j/search_subjects?type=movie&tag=热门&page_limit=1000&page_start=0";
}
