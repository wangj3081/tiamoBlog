package com.tiamo.search.dto.response;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Readhub 搜索返回
 * @author wangjian
 * @version 1.0
 * @see ReadHubResponse
 * @since JDK1.8
 */
public class ReadHubResponse<T> implements Serializable {

    private List<T> listVal;

    private Map<String, Object> groupMap;

    private Long total;

    public List<T> getListVal() {
        return listVal;
    }

    public void setListVal(List<T> listVal) {
        this.listVal = listVal;
    }

    public Map<String, Object> getGroupMap() {
        return groupMap;
    }

    public void setGroupMap(Map<String, Object> groupMap) {
        this.groupMap = groupMap;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
