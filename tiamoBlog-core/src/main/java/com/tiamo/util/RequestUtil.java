package com.tiamo.util;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @Auther: wangjian
 * @Date: 2019-03-13 16:31:06
 */
public abstract class RequestUtil extends PageUtil{

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
