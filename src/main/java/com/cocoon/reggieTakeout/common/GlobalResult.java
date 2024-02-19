package com.cocoon.reggieTakeout.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果，服务端响应的数据最终都会封装成此对象
 **/
@Data
public class GlobalResult<T> {
    private Integer code;

    private String msg;

    private T data;

    /**
     * 动态数据
     **/
    private Map map = new HashMap();

    public static <T> GlobalResult<T> success(T object) {
        GlobalResult<T> globalResult = new GlobalResult<T>();
        globalResult.data = object;
        globalResult.code = 1;
        return globalResult;
    }

    public static <T> GlobalResult<T> error(String msg) {
        GlobalResult globalResult = new GlobalResult();
        globalResult.msg = msg;
        globalResult.code = 0;
        return globalResult;
    }

    public GlobalResult<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
