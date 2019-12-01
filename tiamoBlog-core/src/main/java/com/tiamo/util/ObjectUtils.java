package com.tiamo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 对象转换工具类
 * @author wangjian
 * @version 1.0
 * @see com.tiamo.util.ObjectUtils
 * @since JDK1.8
 */
public class ObjectUtils {

    /**
     * 将传入的对象转换成 map
     * @param object
     * @return
     */
    public static Map<String, Object> toMap(Object object) {
        String jsonValue = JSONObject.toJSONString(object);
        HashMap<String, Object> resultValue = JSON.parseObject(jsonValue, HashMap.class);
        return resultValue;
    }
}
