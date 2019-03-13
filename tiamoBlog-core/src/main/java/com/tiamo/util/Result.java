package com.tiamo.util;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * 返回工具类
 * @Auther: wangjian
 * @Date: 2019-03-13 15:59:19
 */
public class Result<T> implements Serializable {

    private String code;

    private String message;

    private T date;

    public Result() {
    }

    public static final String ERROR_CODE = "-1";


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getDate() {
        return date;
    }

    public Result<T> error(String code, String message) {
        this.code = code;
        this.message = message;
        return this;
    }

    public Result<T> success(T data) {
        this.code = "0";
        this.date = data;
        this.message = "SUCCESS";
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
