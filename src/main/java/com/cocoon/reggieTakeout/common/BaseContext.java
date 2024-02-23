package com.cocoon.reggieTakeout.common;

/** 基于ThreadLocal的封装工具类，用于当前线程内共享用户id **/
public class BaseContext {
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
