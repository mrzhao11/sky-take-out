package com.sky.context;

public class BaseContext {

    // ThreadLocal 用于存储当前线程的用户ID
    // ThreadLocal 提供了线程局部变量，每个线程都有自己的独立变量副本，互不干扰
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
